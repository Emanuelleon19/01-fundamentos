package ec.edu.ups.icc.fundamentos01.users.mappers;

import java.time.LocalDateTime;

import ec.edu.ups.icc.fundamentos01.users.dtos.CreateUserDto;
import ec.edu.ups.icc.fundamentos01.users.dtos.UserResponseDto;
import ec.edu.ups.icc.fundamentos01.users.entities.UserEntity;
import ec.edu.ups.icc.fundamentos01.users.models.UserModel;

/*
 * Clase encargada de convertir objetos entre DTOs y modelos.
 *
 * En esta práctica se usa para separar los datos que llegan desde la API
 * de los datos que maneja internamente la aplicación.
 *
 * El mapper evita que el controlador copie manualmente los campos
 * entre CreateUserDto, UserModel y UserResponseDto.
 */

public class UserMapper {

    /*
     * Convierte un CreateUserDto en un UserModel.
     *
     * Se usa cuando llega una petición POST para crear un usuario.
     * El DTO contiene los datos enviados por el cliente.
     * El modelo representa el usuario dentro de la aplicación.
     *
     * En este método también se asigna createdAt porque la fecha de creación
     * debe generarla el backend y no el cliente.
     */
    public static UserModel toModelFormDTO(CreateUserDto dto) {
        UserModel model = new UserModel();

        model.setName(dto.getName());
        model.setEmail(dto.getEmail());
        model.setPassword(dto.getPassword());
        model.setPasswordHash("HASH_" + dto.getPassword());
        model.setCreatedAt(LocalDateTime.now());

        return model;
    }

    /*
     * Convierte una entidad JPA en UserModel.
     * Se usa cuando el repositorio devuelve datos desde PostgreSQL.
     */
    public static UserModel toModelFromEntity(UserEntity entity) {
        UserModel model = new UserModel();

        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setEmail(entity.getEmail());
        model.setPasswordHash(entity.getPasswordHash());
        model.setCreatedAt(entity.getCreatedAt());
        model.setUpdatedAt(entity.getUpdatedAt());
        model.setDeleted(entity.isDeleted());

        return model;
    }

    /*
     * Convierte un UserModel en UserEntity.
     * Se usa antes de guardar datos en la base de datos.
     */
    public static UserEntity toEntityFromModel(UserModel model) {
        UserEntity entity = new UserEntity();

        entity.setId(model.getId());
        entity.setName(model.getName());
        entity.setEmail(model.getEmail());
        entity.setPasswordHash(model.getPasswordHash());

        return entity;
    }

    /*
     * Convierte un UserModel en UserResponseDto.
     * No se expone password ni passwordHash.
     */
    public static UserResponseDto toResponse(UserModel model) {
        UserResponseDto response = new UserResponseDto();

        response.setId(model.getId());
        response.setName(model.getName());
        response.setEmail(model.getEmail());
        response.setCreatedAt(model.getCreatedAt());

        return response;
    }

}
