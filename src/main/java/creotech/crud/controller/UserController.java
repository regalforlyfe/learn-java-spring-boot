package creotech.crud.controller;

import creotech.crud.entity.User;
import creotech.crud.model.GeneralResponse;
import creotech.crud.model.UpdateUserRequest;
import creotech.crud.model.UserResponse;
import creotech.crud.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    // GET /api/v1/users
    @GetMapping(
            path = "",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public GeneralResponse<List<UserResponse>> getAllUsers(User user) {
        List<UserResponse> data = userService.getAllUsers();
        return GeneralResponse.<List<UserResponse>>builder()
                .status("success")
                .message("Users fetched")
                .data(data) // bisa kosong []
                .timestamp(LocalDateTime.now())
                .build();
    }

    @PatchMapping(
            path = "/{uuid}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public GeneralResponse<UserResponse> updateUser(
            @PathVariable String uuid,
            @Valid @RequestBody UpdateUserRequest request,
            User user) {
        UserResponse data = userService.update(uuid, request);
        return  GeneralResponse.<UserResponse>builder()
                .status("success")
                .message("User updated successfully")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @DeleteMapping(path = "/{uuid}")
    public GeneralResponse<String> deleteUser(@PathVariable String uuid, User user) {
        userService.deleteById(uuid);

        return GeneralResponse.<String>builder()
                .status("success")
                .message("User deleted successfully")
                .data("OK")
                .timestamp(LocalDateTime.now())
                .build();
    }
}
