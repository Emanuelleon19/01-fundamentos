package ec.edu.ups.icc.fundamentos01.security.controllers;

import ec.edu.ups.icc.fundamentos01.security.dtos.AuthResponseDto;
import ec.edu.ups.icc.fundamentos01.security.dtos.LoginRequestDto;
import ec.edu.ups.icc.fundamentos01.security.dtos.RegisterRequestDto;
import ec.edu.ups.icc.fundamentos01.security.entities.RoleEntity;
import ec.edu.ups.icc.fundamentos01.security.enums.RoleName;
import ec.edu.ups.icc.fundamentos01.security.repositories.RoleRepository;
import ec.edu.ups.icc.fundamentos01.security.services.UserDetailsImpl;
import ec.edu.ups.icc.fundamentos01.security.utils.JwtUtil;
import ec.edu.ups.icc.fundamentos01.users.entities.UserEntity;
import ec.edu.ups.icc.fundamentos01.users.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * AuthController: Controlador REST para manejar la autenticación (Login y Registro)
 */
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    /**
     * Constructor: Inyección de dependencias por Spring
     */
    public AuthController(AuthenticationManager authenticationManager,
                          UserRepository userRepository,
                          RoleRepository roleRepository,
                          PasswordEncoder encoder,
                          JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
    }

    /**
     * authenticateUser: Endpoint para iniciar sesión (POST /api/auth/login)
     * * @param loginRequest: Credenciales del usuario (email y password)
     * @return ResponseEntity<AuthResponseDto>: Token generado y datos de usuario
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequestDto loginRequest) {

        // 1. Autenticar el email y la contraseña usando el AuthenticationManager
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        // 2. Establecer la autenticación en el contexto de seguridad global
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. Generar el token JWT cifrado
        String jwt = jwtUtil.generateToken(authentication);
        
        // 4. Obtener los detalles del usuario autenticado
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();    
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        // 5. Retornar respuesta 200 OK con el DTO que contiene el token JWT
        return ResponseEntity.ok(new AuthResponseDto(jwt, 
                                             userDetails.getId(), 
                                             userDetails.getName(), 
                                             userDetails.getEmail(), 
                                             roles));
    }

    /**
     * registerUser: Endpoint para registrar un nuevo usuario (POST /api/auth/register)
     * * @param signUpRequest: Datos de registro (name, email, password)
     * @return ResponseEntity<String>: Mensaje de éxito o error
     */
@PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequestDto signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: El email ya está en uso.");
        }

        UserEntity user = new UserEntity(signUpRequest.getName(), 
                                         signUpRequest.getEmail(),
                                         encoder.encode(signUpRequest.getPassword()));

        Set<RoleEntity> roles = new HashSet<>();
        RoleEntity userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: El rol ROLE_USER no fue encontrado en la base de datos."));
        roles.add(userRole);

        user.setRoles(roles);
        
        userRepository.save(user);

        
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signUpRequest.getEmail(), signUpRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtil.generateToken(authentication);
        
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();    
        List<String> strRoles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new AuthResponseDto(jwt, 
                                                     userDetails.getId(), 
                                                     userDetails.getName(), 
                                                     userDetails.getEmail(), 
                                                     strRoles));
    }
}