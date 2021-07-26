package com.getitcheap.API.Users;

import com.getitcheap.API.DTO.JwtResponse;
import com.getitcheap.API.DTO.MessageResponse;
import com.getitcheap.API.Security.JwtTokenService;
import com.getitcheap.API.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenService jwtTokenService;

    @Autowired
    PasswordEncoder encoder;

    public ResponseEntity<?> signIn(UserEntity signInRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    signInRequest.getUsername(),
                                    signInRequest.getPassword()
                            )
                    );

            UserEntity user = (UserEntity) authentication.getPrincipal();

            String jwt = jwtTokenService.createJwtToken(user);

            return ResponseEntity.ok(new JwtResponse(jwt,
                    user.getId(),
                    user.getFullName(),
                    user.getEmail()));
        } catch (Exception e) {
            return ResponseEntity.status(403).body(new MessageResponse("Invalid credentials"));
        }
    }

    public ResponseEntity<?> signUp(UserEntity signupRequest) {
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity.status(400).body(new MessageResponse("An account with this Email Id already exists"));
        }

        boolean successful = userRepository.signUp(signupRequest.getFirstName(), signupRequest.getLastName(),
                signupRequest.getEmail(), encoder.encode(signupRequest.getPassword()));

        return successful ? ResponseEntity.ok().body(new MessageResponse("Signup Successful")) :
                Utilities.getSomethingWentWrongResponse();
    }

    /**
     *  Method used by Spring Security
     * @param email
     * @return UserDetails Instance
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User with email %s is not found", email));
        }
        return user;
    }
}
