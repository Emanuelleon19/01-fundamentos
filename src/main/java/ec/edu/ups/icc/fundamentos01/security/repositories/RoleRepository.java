package ec.edu.ups.icc.fundamentos01.security.repositories;

import ec.edu.ups.icc.fundamentos01.security.entities.RoleEntity;
import ec.edu.ups.icc.fundamentos01.security.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * RoleRepository: Repositorio JPA para la entidad RoleEntity
 */
@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    /**
     * findByName: Busca un rol por su nombre en el enum (ROLE_USER, ROLE_ADMIN)
     * * @param name: Nombre del rol
     * @return Optional<RoleEntity>: Contenedor con el rol si existe
     */
    Optional<RoleEntity> findByName(RoleName name);
}