package creotech.crud.controller;

import creotech.crud.model.GeneralResponse;
import creotech.crud.model.LoginRequest;
import creotech.crud.model.RegisterUserRequest;
import creotech.crud.model.UserResponse;
import creotech.crud.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping(path = "/register")
    public GeneralResponse<String> register(@Valid @RequestBody RegisterUserRequest request) {
        authService.register(request);
        return GeneralResponse.success("User registered successfully");
    }

    @PostMapping(path = "/login")
    public GeneralResponse<UserResponse> login(@Valid @RequestBody LoginRequest request) {
        UserResponse userResponse = authService.login(request);
        return GeneralResponse.success(userResponse);
    }
}
