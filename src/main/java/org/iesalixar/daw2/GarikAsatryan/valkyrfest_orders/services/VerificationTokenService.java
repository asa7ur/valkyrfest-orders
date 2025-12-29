package org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.services;

import lombok.RequiredArgsConstructor;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.entities.User;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.entities.VerificationToken;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.repositories.VerificationTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationTokenService {

    private final VerificationTokenRepository tokenRepository;

    /**
     * Crea un nuevo token de verificación para un usuario y lo guarda.
     * Genera una cadena aleatoria única (UUID).
     */
    @Transactional
    public String createVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken myToken = new VerificationToken(token, user);
        tokenRepository.save(myToken);
        return token;
    }

    /**
     * Recupera un token de la base de datos.
     */
    public Optional<VerificationToken> getVerificationToken(String token) {
        return tokenRepository.findByToken(token);
    }

    /**
     * Elimina el token (se usará una vez que el usuario haya activado su cuenta).
     */
    @Transactional
    public void deleteToken(VerificationToken token) {
        tokenRepository.delete(token);
    }

    /**
     * Busca si un usuario ya tiene un token (útil para limpiezas o reenvíos).
     */
    public Optional<VerificationToken> getTokenByUser(User user) {
        return tokenRepository.findByUser(user);
    }
}