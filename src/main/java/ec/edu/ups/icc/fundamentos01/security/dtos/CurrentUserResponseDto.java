package ec.edu.ups.icc.fundamentos01.security.dtos;

import java.util.Set;

/*
 * DTO usado para devolver la información básica
 * del usuario autenticado.
 */
public class CurrentUserResponseDto {

    private Long id;
    private String name;
    private String email;
    private Set<String> roles;

    public CurrentUserResponseDto() {
    }

    public CurrentUserResponseDto(Long id, String name, String email, Set<String> roles) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}