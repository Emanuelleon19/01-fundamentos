package ec.edu.ups.icc.fundamentos01.users.dtos;

/*
 * DTO utilizado para recibir los datos necesarios
 * para actualizar completamente un usuario existente.
 * 
 * No incluye id porque el id llega por la URL.
 * No incluye createdAt porque la fecha de creación no debe modificarse.
 */

public class UpdateUserDto {

    private String name;
    private String email;

    public UpdateUserDto() {
    }

    public UpdateUserDto(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
}
