package creotech.crud.service;

import creotech.crud.entity.User;
import creotech.crud.model.UpdateUserRequest;
import creotech.crud.model.UserResponse;
import creotech.crud.repository.UserRepository;
import creotech.crud.security.BCrypt;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ValidationService validationService;

    @Transactional
    public UserResponse update(String uuid, UpdateUserRequest request) {
        validationService.validate(request);

        User user = userRepository.findById(uuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (StringUtils.hasText(request.name())) {
            user.setName(request.name().trim());
        }

        if (StringUtils.hasText(request.password())) {
            String hashed = BCrypt.hashpw(request.password(), BCrypt.gensalt());
            user.setPassword(hashed);
        }

        userRepository.save(user);

        return mapToResponse(user);
    }

    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();

        if (users.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No users found");
        }

        return users.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    public void deleteById(String uuid) {
        if (!userRepository.existsById(uuid)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        userRepository.deleteById(uuid);
    }

    private UserResponse mapToResponse(User user) {
        return new UserResponse(
                user.getUuid(),
                user.getEmail(),
                user.getName(),
                null,
                null,
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
