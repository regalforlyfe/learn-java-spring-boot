package creotech.crud.controller;

import creotech.crud.model.GeneralResponse;
import creotech.crud.model.LoginRequest;
import creotech.crud.model.RegisterUserRequest;
import creotech.crud.model.UserResponse;
import creotech.crud.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // POST /api/v1/auth/register
    @PostMapping(
            path = "/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public GeneralResponse<String> register(@Valid @RequestBody RegisterUserRequest request) {
        authService.register(request);
        return GeneralResponse.<String>builder()
                .status("success")
                .message("User registered successfully")
                .data("OK")
                .timestamp(LocalDateTime.now())
                .build();
    }

    @PostMapping(
            path = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public GeneralResponse<UserResponse> login(@Valid @RequestBody LoginRequest request) {
        UserResponse userResponse = authService.login(request);
        return GeneralResponse.<UserResponse>builder()
                .status("success")
                .message("User login successfully")
                .data(userResponse)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
