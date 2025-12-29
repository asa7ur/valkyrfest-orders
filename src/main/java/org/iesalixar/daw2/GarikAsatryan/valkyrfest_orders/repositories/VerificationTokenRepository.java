package org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.repositories;

import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.entities.User;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.entities.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    /**
     * Busca un token en la base de datos mediante su cadena UUID.
     * Retorna un Optional para manejar de forma segura si el token no existe.
     */
    Optional<VerificationToken> findByToken(String token);

    /**
     * Permite encontrar el token asociado a un usuario concreto.
     * Útil si quisiéramos implementar un "Reenviar email de confirmación".
     */
    Optional<VerificationToken> findByUser(User user);
}