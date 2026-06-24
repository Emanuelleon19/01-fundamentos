package ec.edu.ups.icc.fundamentos01.users.dtos;

/*
 * DTO utilizado para recibir los datos que se desean
 * actualizar parcialmente en un usuario existente.
 *
 * Los campos pueden venir nulos cuando no se desean actualizar.
 * No incluye createdAt porque la fecha de creación no debe modificarse.
 */

public class PartialUpdateUserDto {

    private String name;
    private String email;

    public PartialUpdateUserDto() {
    }

    public PartialUpdateUserDto(String name, String email) {
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
