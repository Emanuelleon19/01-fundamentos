package ec.edu.ups.icc.fundamentos01.users.dtos;

/*
 * DTO utilizado para recibir los datos necesarios
 * para crear un nuevo usuario desde una petición HTTP.
 * 
 * No incluye id porque el backend lo genera.
 * No incluye createdAt porque el backend asigna la fecha de creación.
 */

public class CreateUserDto {

    private String name;
    private String email;
    private String password;

    public CreateUserDto() {
    }

    public CreateUserDto(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}