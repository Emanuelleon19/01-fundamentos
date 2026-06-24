package ec.edu.ups.icc.fundamentos01.core.dto;

/*
 * DTO (Data Transfer Object) para representar un error en las respuestas HTTP.
 *
 * Este DTO se utiliza para enviar mensajes de error al cliente cuando
 * una operación no puede ser completada, como cuando un usuario no es encontrado.
 *
 * Contiene un campo "message" que describe el error ocurrido.
 */

public class ErrorResponseDto {
    private String message;

    public ErrorResponseDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}