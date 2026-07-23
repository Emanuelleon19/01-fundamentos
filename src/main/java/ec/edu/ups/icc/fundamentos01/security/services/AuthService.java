package ec.edu.ups.icc.fundamentos01.security.services;

import ec.edu.ups.icc.fundamentos01.security.dtos.AuthResponseDto;
import ec.edu.ups.icc.fundamentos01.security.dtos.LoginRequestDto;
import ec.edu.ups.icc.fundamentos01.security.dtos.RegisterRequestDto;
import ec.edu.ups.icc.fundamentos01.security.dtos.RefreshTokenRequestDto;
import ec.edu.ups.icc.fundamentos01.security.entities.RefreshTokenEntity;
import ec.edu.ups.icc.fundamentos01.security.entities.RoleEntity;
import ec.edu.ups.icc.fundamentos01.security.enums.RoleName;
import ec.edu.ups.icc.fundamentos01.security.repositories.RoleRepository;
import ec.edu.ups.icc.fundamentos01.security.utils.JwtUtil;
import ec.edu.ups.icc.fundamentos01.users.entities.UserEntity;
import ec.edu.ups.icc.fundamentos01.users.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    public AuthService(AuthenticationManager authenticationManager, 
                       UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder, 
                       JwtUtil jwtUtil, 
                       RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
    }

    @Transactional
    public AuthResponseDto login(LoginRequestDto loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        String accessToken = jwtUtil.generateToken(authentication);

        UserEntity user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("Error: Usuario no encontrado."));

        RefreshTokenEntity refreshTokenEntity = refreshTokenService.createRefreshToken(user);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return new AuthResponseDto(
                accessToken,
                refreshTokenEntity.getToken(),
                userDetails.getId(),
                userDetails.getName(),
                userDetails.getEmail(),
                roles
        );
    }

    @Transactional
    public AuthResponseDto register(RegisterRequestDto registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Error: El email ya está en uso.");
        }

        UserEntity user = new UserEntity();
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setPasswordHash(passwordEncoder.encode(registerRequest.getPassword()));

        Set<RoleEntity> roles = new HashSet<>();
        RoleEntity userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado en la base de datos."));
        roles.add(userRole);
        user.setRoles(roles);

        UserEntity savedUser = userRepository.save(user);

        UserDetailsImpl userDetails = UserDetailsImpl.build(savedUser);
        String accessToken = jwtUtil.generateTokenFromUserDetails(userDetails);

        RefreshTokenEntity refreshTokenEntity = refreshTokenService.createRefreshToken(savedUser);

        List<String> rolesStr = savedUser.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toList());

        return new AuthResponseDto(
                accessToken,
                refreshTokenEntity.getToken(),
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                rolesStr
        );
    }

    @Transactional
    public AuthResponseDto refresh(RefreshTokenRequestDto refreshRequest) {
        RefreshTokenEntity refreshTokenEntity = refreshTokenService.validateAndGetActiveToken(refreshRequest.getRefreshToken());
        UserEntity user = refreshTokenEntity.getUser();

        UserDetailsImpl userDetails = UserDetailsImpl.build(user);
        String newAccessToken = jwtUtil.generateTokenFromUserDetails(userDetails);

        refreshTokenService.revoke(refreshTokenEntity.getToken());
        RefreshTokenEntity newRefreshTokenEntity = refreshTokenService.createRefreshToken(user);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return new AuthResponseDto(
                newAccessToken,
                newRefreshTokenEntity.getToken(),
                userDetails.getId(),
                userDetails.getName(),
                userDetails.getEmail(),
                roles
        );
    }

    @Transactional
    public void logout(RefreshTokenRequestDto logoutRequest) {
        refreshTokenService.revoke(logoutRequest.getRefreshToken());
    }
}