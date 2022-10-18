package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;


    @Test
    public void authenticateUserOk() {

        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .username("test@email.com")
                .firstName("hello")
                .lastName("world")
                .id(1L)
                .password("password123")
                .build();

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null);

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtUtils.generateJwtToken(any())).thenReturn("jwt");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(User.builder().id(1L).email("test@email.com").password("password123").firstName("hello").lastName("world").admin(false).build()));


        AuthController authController = new AuthController(authenticationManager, passwordEncoder, jwtUtils, userRepository);
        ResponseEntity<?> response = authController.authenticateUser(new LoginRequest("test@email.com", "password123"));
        JwtResponse responseBody = (JwtResponse) response.getBody();


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("test@email.com", responseBody.getUsername());
        assertEquals("hello", responseBody.getFirstName());
        assertEquals("world", responseBody.getLastName());
        assertEquals(1L, responseBody.getId());
        assertFalse(responseBody.getAdmin());
        assertEquals("Bearer", responseBody.getType());
        assertNotNull(responseBody.getToken());
    }


    @Test
    public void registerOk() {

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hashed");
        when(userRepository.save(any())).thenReturn(new User());

        AuthController authController = new AuthController(authenticationManager, passwordEncoder, jwtUtils, userRepository);
        ResponseEntity<?> response = authController.registerUser(new SignupRequest("test@email.com", "", "", "password123"));

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void registerEmailTaken() {

        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        AuthController authController = new AuthController(authenticationManager, passwordEncoder, jwtUtils, userRepository);
        ResponseEntity<?> response = authController.registerUser(new SignupRequest("test@email.com", "", "", "password123"));

        MessageResponse messageResponse = (MessageResponse) response.getBody();
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error: Email is already taken!", messageResponse.getMessage());
    }
}