package ec.edu.ups.icc.fundamentos01.security.filters;

import ec.edu.ups.icc.fundamentos01.security.services.UserDetailsServiceImpl;
import ec.edu.ups.icc.fundamentos01.security.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JwtAuthenticationFilter: Intercepta cada petición HTTP para validar el token
 */
@Component // Componente de Spring (se inyecta en SecurityConfig)
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    /**
     * Constructor: Inyección automática de dependencias
     * * @param jwtUtil: Utilidades de JWT
     * @param userDetailsService: Servicio para cargar usuarios desde BD
     */
    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    /**
     * doFilterInternal: MÉTODO CORE DEL FILTRO
     * * Se ejecuta en CADA PETICIÓN (HTTP Request) que llega a la API
     * * PROCESO:
     * 1. Extraer token de la cabecera "Authorization: Bearer <token>"
     * 2. Validar firma y expiración del token
     * 3. Si es válido, extraer el ID del usuario del token
     * 4. Cargar el usuario desde la base de datos (UserDetails)
     * 5. Crear objeto de autenticación y meterlo en el SecurityContext
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // 1. Extraer el token de la petición HTTP
            String jwt = parseJwt(request);

            // 2. Si el token existe y es válido, autenticar al usuario
            if (jwt != null && jwtUtil.validateToken(jwt)) {
                // 3. Extraer el ID de usuario desde el token (el subject)
                Long userId = jwtUtil.getUserIdFromToken(jwt);
                
                // NOTA: En este punto, como nuestro userDetailsService busca por email,
                // primero extraemos el email del token para poder consultar la BD.
                String email = jwtUtil.getEmailFromToken(jwt);

                // 4. Cargar el usuario de la BD en formato compatible con Spring Security
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                // 5. Crear el objeto principal de autenticación de Spring Security
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,      // Principal (usuario autenticado)
                        null,             // Credentials (no se necesitan, ya está validado por token)
                        userDetails.getAuthorities() // Roles/Permisos del usuario
                );

                // 6. Añadir detalles web adicionales de la petición (IP, Sesión, etc.)
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 7. GUARDAR EL USUARIO AUTENTICADO EN EL CONTEXTO DE SEGURIDAD
                //    A partir de esta línea, el usuario está "Logueado" para esta petición HTTP
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            // Si algo falla, no bloqueamos la app, solo registramos el error
            // El usuario simplemente no quedará autenticado y Spring rebotará la petición si es protegida
            logger.error("No se pudo configurar la autenticación de usuario: {}", e.getMessage());
        }

        // 8. Pasar al siguiente filtro en la cadena (Filter Chain)
        //    OBLIGATORIO: Si no se pone, la petición muere aquí y no llega al Controller
        filterChain.doFilter(request, response);
    }

    /**
     * parseJwt: Método auxiliar para extraer el token del Header Authorization
     * * @param request: Petición HTTP
     * @return String: El token limpio o null si no cumple el formato
     */
    private String parseJwt(HttpServletRequest request) {
        // Extraer la cabecera "Authorization"
        String headerAuth = request.getHeader("Authorization");

        // Validar que no esté vacía y que empiece con "Bearer "
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            // Cortar el String para sacar solo el token (elimina "Bearer " que son 7 caracteres)
            return headerAuth.substring(7);
        }

        return null;
    }
}