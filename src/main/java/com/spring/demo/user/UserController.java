package com.spring.demo.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/users")
    public ResponseEntity<?> save(UserRequest.JoinDTO joinDTO) {
        UserResponse.UsersDTO users= userService.getUsers(joinDTO);

        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> user(@PathVariable("id") int id) {
        UserResponse.UserDTO user = userService.getUser(id);

        return ResponseEntity.ok(user);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserResponse.UserDTO> update(@PathVariable("id") int id, UserRequest.UserDTO dto){
        UserResponse.UserDTO user = userService.updateUser(id, dto);

        return ResponseEntity.ok(user);
    }

}
