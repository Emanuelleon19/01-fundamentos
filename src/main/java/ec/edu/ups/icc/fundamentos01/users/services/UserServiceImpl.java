package ec.edu.ups.icc.fundamentos01.users.services;

import java.util.List;
import org.springframework.stereotype.Service;

import ec.edu.ups.icc.fundamentos01.users.dtos.CreateUserDto;
import ec.edu.ups.icc.fundamentos01.users.dtos.PartialUpdateUserDto;
import ec.edu.ups.icc.fundamentos01.users.dtos.UpdateUserDto;
import ec.edu.ups.icc.fundamentos01.users.dtos.UserResponseDto;
import ec.edu.ups.icc.fundamentos01.users.entities.UserEntity;
import ec.edu.ups.icc.fundamentos01.users.mappers.UserMapper;
import ec.edu.ups.icc.fundamentos01.users.models.UserModel;
import ec.edu.ups.icc.fundamentos01.users.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    // Inyección de dependencias por constructor
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /*
     * Retorna todos los usuarios almacenados en PostgreSQL.
     * El repositorio devuelve entidades, el mapper las convierte a modelos
     * y posteriormente a DTOs de respuesta seguros.
     */
    @Override
    public List<UserResponseDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toModelFromEntity)
                .map(UserMapper::toResponse)
                .toList();
    }

    /*
     * Busca un usuario por id en PostgreSQL usando Optional.
     * Si no existe, lanza un error simple como pide la guía.
     */
    @Override
    public UserResponseDto findOne(Long id) {
        return userRepository.findById(id)
                .map(UserMapper::toModelFromEntity)
                .map(UserMapper::toResponse)
                .orElseThrow(() -> new IllegalStateException("User not found"));
    }

    /*
     * Crea un nuevo usuario en la base de datos real.
     * Convierte DTO -> Model -> Entity -> Guardado -> Model -> ResponseDto.
     */
    @Override
    public UserResponseDto create(CreateUserDto dto) {

        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalStateException("Email already registered");
        }

        UserModel model = UserMapper.toModelFormDTO(dto);

        UserEntity entity = UserMapper.toEntityFromModel(model);

        UserEntity savedEntity = userRepository.save(entity);

        UserModel savedModel = UserMapper.toModelFromEntity(savedEntity);

        return UserMapper.toResponse(savedModel);
    }

    /*
     * Actualiza completamente un usuario existente.
     * Modifica los campos en la entidad recuperada y guarda los cambios.
     */
    @Override
    public UserResponseDto update(Long id, UpdateUserDto dto) {
        UserEntity entity = userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());

        UserEntity savedEntity = userRepository.save(entity);

        UserModel model = UserMapper.toModelFromEntity(savedEntity);

        return UserMapper.toResponse(model);
    }

    /*
     * Actualiza parcialmente un usuario (PATCH).
     * Solo reemplaza los campos que no vengan nulos en el DTO.
     */
    @Override
    public UserResponseDto partialUpdate(Long id, PartialUpdateUserDto dto) {
        UserEntity entity = userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        if (dto.getName() != null) {
            entity.setName(dto.getName());
        }

        if (dto.getEmail() != null) {
            entity.setEmail(dto.getEmail());
        }

        UserEntity savedEntity = userRepository.save(entity);

        UserModel model = UserMapper.toModelFromEntity(savedEntity);

        return UserMapper.toResponse(model);
    }

    /*
     * Elimina lógicamente un usuario por id.
     * Cambia la bandera "deleted" a true sin borrar físicamente la fila.
     */
    @Override
    public void delete(Long id) {
        UserEntity entity = userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        entity.setDeleted(true);

        userRepository.save(entity);
    }
}
