package ec.edu.ups.icc.fundamentos01.security.controllers;

// imports packages y clases....

import jakarta.validation.Valid;

import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ec.edu.ups.icc.fundamentos01.security.dtos.AuthResponseDto;
import ec.edu.ups.icc.fundamentos01.security.dtos.LoginRequestDto;
import ec.edu.ups.icc.fundamentos01.security.dtos.RefreshTokenRequestDto;
import ec.edu.ups.icc.fundamentos01.security.dtos.RegisterRequestDto;
import ec.edu.ups.icc.fundamentos01.security.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Autenticación", description = "Endpoints públicos para registro e inicio de sesión")
@RestController
@RequestMapping("/auth") // Prefijo para todos los endpoints de autenticación
public class AuthController {

    private final AuthService authService; // Servicio de lógica de autenticación

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Login - Endpoint público (configurado en SecurityConfig)
     * POST /auth/login
     */
    @Operation(summary = "Inicio de sesión", description = "Permite iniciar sesión y obtener un JWT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Inicio de sesión exitoso"),
        @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequest) {
        // @Valid valida anotaciones en LoginRequestDto (email, password requeridos)
        AuthResponseDto response = authService.login(loginRequest);
        return ResponseEntity.ok(response); // 200 OK con JWT
    }

    /**
     * Registro - Endpoint público (configurado en SecurityConfig)
     * POST /auth/register
     */
    @Operation(summary = "Registro de usuario", description = "Permite registrar un nuevo usuario y obtener un JWT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
        @ApiResponse(responseCode = "409", description = "El correo electrónico ya está registrado")
    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@Valid @RequestBody RegisterRequestDto registerRequest) {
        // @Valid valida anotaciones en RegisterRequestDto
        AuthResponseDto response = authService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response); // 201 Created con JWT
    }

    /*
     * Refresh.
     *
     * Recibe un refresh token válido y devuelve nuevos tokens.
     */
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDto> refresh(
            @Valid @RequestBody RefreshTokenRequestDto request
    ) {
        AuthResponseDto response = authService.refresh(request);

        return ResponseEntity.ok(response);
    }

    /*
     * Logout.
     *
     * Revoca el refresh token recibido.
     */
    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(
            @Valid @RequestBody RefreshTokenRequestDto request
    ) {
        authService.logout(request);
    }

}