package ec.edu.ups.icc.fundamentos01.security.filters;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * JwtAuthEntryPoint: Maneja errores de usuarios NO AUTENTICADOS (401 Unauthorized)
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

    /**
     * commence: Se activa automáticamente cuando un usuario anónimo intenta
     * acceder a un endpoint protegido por seguridad sin enviar un token válido.
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        // 1. Registrar el error en la consola del servidor
        logger.error("Error de no autorizado: {}", authException.getMessage());

        // 2. Responder al cliente con un estado 401 Unauthorized y un mensaje claro
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: No autorizado. Debes proveer un token válido.");
    }
}