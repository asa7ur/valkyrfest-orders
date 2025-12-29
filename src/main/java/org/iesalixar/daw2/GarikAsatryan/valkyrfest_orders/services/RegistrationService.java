package org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.dto.UserRegistrationDTO;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.entities.Role;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.entities.User;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.exceptions.AppException;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.repositories.RoleRepository;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenService verificationTokenService;
    private final EmailService emailService;

    @Transactional
    public void registerUser(UserRegistrationDTO dto) {
        // Verificar si el email ya existe
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new AppException("msg.login.emailExists", dto.getEmail());
        }

        // Crear la entidad User y mapear campos
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setBirthDate(dto.getBirthDate());
        user.setPhone(dto.getPhone());

        // El usuario nace desactivado
        user.setEnabled(false);

        // Cifrar la contraseña
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        // Asignar rol "User por defecto"
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Error: Role 'User' not found."));
        user.setRoles(Collections.singletonList(userRole));

        // Guardamos el usuario primero para que tenga un ID generado
        userRepository.save(user);

        // Generamos el token de verificación asociado a este usuario
        String token = verificationTokenService.createVerificationToken(user);

        // Enviamos el email
        emailService.sendRegistrationConfirmationEmail(user.getEmail(), user.getFirstName(), token);
    }
}