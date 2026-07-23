package ec.edu.ups.icc.fundamentos01.security.services;

import ec.edu.ups.icc.fundamentos01.core.exceptions.domain.BadRequestException; // IMPORTADO
import ec.edu.ups.icc.fundamentos01.security.config.JwtProperties;
import ec.edu.ups.icc.fundamentos01.security.entities.RefreshTokenEntity;
import ec.edu.ups.icc.fundamentos01.security.repositories.RefreshTokenRepository;
import ec.edu.ups.icc.fundamentos01.security.utils.JwtUtil;
import ec.edu.ups.icc.fundamentos01.users.entities.UserEntity;
import ec.edu.ups.icc.fundamentos01.security.services.UserDetailsImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;
    private final JwtProperties jwtProperties;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, JwtUtil jwtUtil, JwtProperties jwtProperties) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtUtil = jwtUtil;
        this.jwtProperties = jwtProperties;
    }

    @Transactional
    public RefreshTokenEntity createRefreshToken(UserEntity user) {
        revokeAllByUser(user.getId());

        UserDetailsImpl userDetails = UserDetailsImpl.build(user);
        String token = jwtUtil.generateRefreshToken(userDetails);
        LocalDateTime expiresAt = LocalDateTime.now().plusWeeks(1);

        RefreshTokenEntity refreshToken = new RefreshTokenEntity(user, token, expiresAt);
        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshTokenEntity validateAndGetActiveToken(String token) {
        RefreshTokenEntity refreshToken = refreshTokenRepository.findByTokenAndRevokedFalse(token)
                .orElseThrow(() -> new BadRequestException("Refresh token no encontrado o revocado"));

        if (refreshToken.isExpired() || !jwtUtil.validateRefreshToken(token)) {
            refreshToken.setRevoked(true);
            refreshTokenRepository.save(refreshToken);
            throw new BadRequestException("Refresh token expirado o inválido");
        }

        return refreshToken;
    }

    @Transactional
    public void revoke(String token) {
        refreshTokenRepository.findByTokenAndRevokedFalse(token)
                .ifPresent(refreshToken -> {
                    refreshToken.setRevoked(true);
                    refreshTokenRepository.save(refreshToken);
                });
    }

    @Transactional
    public void revokeAllByUser(Long userId) {
        List<RefreshTokenEntity> activeTokens = refreshTokenRepository.findByUserIdAndRevokedFalse(userId);
        activeTokens.forEach(token -> token.setRevoked(true));
        refreshTokenRepository.saveAll(activeTokens);
    }
}