package creotech.crud.service;

import creotech.crud.entity.User;
import creotech.crud.model.UpdateUserRequest;
import creotech.crud.model.UserResponse;
import creotech.crud.repository.UserRepository;
import creotech.crud.security.BCrypt;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public UserResponse update(String uuid, UpdateUserRequest request) {
        validationService.validate(request);

        User user = userRepository.findById(uuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found"));

        if (StringUtils.hasText(request.getName())) {
            String newName = request.getName().trim();
            user.setName(newName);
        }

        if (StringUtils.hasText(request.getPassword())) {
            String hashed = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt());
            user.setPassword(hashed);
        }

        userRepository.save(user);

        return UserResponse.builder()
                .uuid(user.getUuid())
                .email(user.getEmail())
                .name(user.getName())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();

        if (users.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No users found");
        }

        return users.stream().map(user -> UserResponse.builder()
                .uuid(user.getUuid())
                .name(user.getName())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build()
        ).collect(Collectors.toList());
    }

    @Transactional
    public void deleteById(String uuid) {
        if (!userRepository.existsById(uuid)) {
           throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        userRepository.deleteById(uuid);
    }
}
