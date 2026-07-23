package ec.edu.ups.icc.fundamentos01.security.repositories;

import ec.edu.ups.icc.fundamentos01.security.entities.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {

    Optional<RefreshTokenEntity> findByTokenAndRevokedFalse(String token);

    List<RefreshTokenEntity> findByUserIdAndRevokedFalse(Long userId);
}