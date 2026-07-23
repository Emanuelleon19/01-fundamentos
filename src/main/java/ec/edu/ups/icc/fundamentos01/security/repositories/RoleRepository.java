package ec.edu.ups.icc.fundamentos01.security.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ec.edu.ups.icc.fundamentos01.security.entities.RoleEntity;
import ec.edu.ups.icc.fundamentos01.security.enums.RoleName;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    // Buscar rol por nombre (ROLE_USER, ROLE_ADMIN, etc.)
    Optional<RoleEntity> findByName(RoleName name);
    
    // Verificar si existe un rol específico
    boolean existsByName(RoleName name);
}