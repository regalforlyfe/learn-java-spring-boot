package creotech.crud.controller;

import creotech.crud.entity.User;
import creotech.crud.model.GeneralResponse;
import creotech.crud.model.UpdateUserRequest;
import creotech.crud.model.UserResponse;
import creotech.crud.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<GeneralResponse<List<UserResponse>>> getAllUsers(User currentUser) {
        List<UserResponse> data = userService.getAllUsers();
        return ResponseEntity.ok(GeneralResponse.success(data));
    }

    @PatchMapping(value = "/{uuid}")
    public ResponseEntity<GeneralResponse<UserResponse>> updateUser(
            @PathVariable String uuid,
            @Valid @RequestBody UpdateUserRequest request,
            User currentUser
    ) {
        UserResponse data = userService.update(uuid, request);
        return ResponseEntity.ok(GeneralResponse.success(data));
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<GeneralResponse<String>> deleteUser(@PathVariable String uuid, User currentUser) {
        userService.deleteById(uuid);
        return ResponseEntity.ok(GeneralResponse.success("User deleted successfully"));
    }
}
