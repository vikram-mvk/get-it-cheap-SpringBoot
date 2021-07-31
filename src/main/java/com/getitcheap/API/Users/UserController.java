package com.getitcheap.API.Users;

import com.getitcheap.API.DTO.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(new MessageResponse("Welcome!"));
    }

    @GetMapping("/unauthorized")
    public ResponseEntity<?> unauthorizedResponse() {
        return ResponseEntity.status(401).body(new MessageResponse("Unauthorized"));
    }

    @PostMapping(UserRoutes.SIGNIN)
    public ResponseEntity<?> signIn(@RequestBody UserEntity signInRequest) {
        return userService.signIn(signInRequest);
    }

    @PostMapping(UserRoutes.SIGNUP)
    public ResponseEntity<?> signUp(@RequestBody UserEntity signUpRequest) {
        return userService.signUp(signUpRequest);
    }

    @PostMapping(UserRoutes.UPDATE_PROFILE)
    public ResponseEntity<?> updateProfile(@RequestBody UserEntity updateProfileRequest) {
        return userService.updateProfile(updateProfileRequest);
    }


}
