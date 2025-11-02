package creotech.crud.service;

import creotech.crud.entity.User;
import creotech.crud.model.LoginRequest;
import creotech.crud.model.RegisterUserRequest;
import creotech.crud.model.UserResponse;
import creotech.crud.repository.AuthRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthRepository authRepository;
    private final ValidationService validationService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void register(RegisterUserRequest request) {
        validationService.validate(request);

        // record accessor → email() / name() / password()
        if (authRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already registered");
        }

        User user = new User();
        user.setUuid(UUID.randomUUID().toString());
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));

        authRepository.save(user);
    }

    @Transactional
    public UserResponse login(LoginRequest request) {
        validationService.validate(request);

        User user = authRepository.findFirstByEmail(request.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password wrong"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password wrong");
        }

        user.setToken(UUID.randomUUID().toString());
        user.setTokenExpiredAt(next30Days());
        authRepository.save(user);

        // Ganti builder dengan constructor record atau helper
        return toResponse(user);
    }

    private UserResponse toResponse(User u) {
        // Sesuaikan urutan argumen dengan definisi record UserResponse kamu
        return new UserResponse(
                u.getUuid(),
                u.getEmail(),
                u.getName(),
                u.getToken(),
                u.getTokenExpiredAt(),
                u.getCreatedAt(),
                u.getUpdatedAt()
        );
    }

    private Long next30Days() {
        return System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 30);
    }
}
