package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class SessionControllerTest {

    @Mock
    private SessionMapper sessionMapper;
    @Mock
    private SessionService sessionService;

    private Session session;
    private SessionDto sessionDto;
    private SessionController sessionController;

    @BeforeEach
    void init() {
        User user = new User(10L, "yoga@yoga.com", "username", "username", "yoga!123", false, LocalDateTime.now(), LocalDateTime.now());
        Teacher teacher = new Teacher(1L, "teachername", "teachername", LocalDateTime.now(), LocalDateTime.now());
        session = new Session(1L, "morning", new Date(23, 07, 05), "cours yoga", teacher, Collections.singletonList(user), LocalDateTime.now(), LocalDateTime.now());
        sessionDto = new SessionDto(1L, "morning", new Date(23, 07, 05), 1L, "cours yoga", Collections.singletonList(10L), LocalDateTime.now(), LocalDateTime.now());

        sessionController = new SessionController(sessionService, sessionMapper);
    }

    @Test
    void findSessionById() {
        when(sessionService.getById(any(Long.class))).thenReturn(session);
        when(sessionMapper.toDto(any(Session.class))).thenReturn(sessionDto);

        ResponseEntity<?> response = sessionController.findById("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessionDto, response.getBody());
    }

    @Test
    void findAllSession() {
        when(sessionService.findAll()).thenReturn(Collections.singletonList(session));
        when(sessionMapper.toDto(any(List.class))).thenReturn(Collections.singletonList(sessionDto));

        ResponseEntity<?> response = sessionController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Collections.singletonList(sessionDto), response.getBody());
    }

    @Test
    void createSession() {
        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.create(any(Session.class))).thenReturn(session);
        when(sessionMapper.toDto(any(Session.class))).thenReturn(sessionDto);

        ResponseEntity<?> response = sessionController.create(sessionDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessionDto, response.getBody());
    }

    @Test
    void updateSession() {
        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.update(any(Long.class), any(Session.class))).thenReturn(session);
        when(sessionMapper.toDto(any(Session.class))).thenReturn(sessionDto);

        ResponseEntity<?> response = sessionController.update("1", sessionDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessionDto, response.getBody());
    }

    @Test
    void saveSession() {
        when(sessionService.getById(any(Long.class))).thenReturn(session);

        ResponseEntity<?> response = sessionController.save("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void participateInSession() {
        ResponseEntity<?> response = sessionController.participate("1", "10");

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void noLongerParticipate() {
        ResponseEntity<?> response = sessionController.noLongerParticipate("1", "10");

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    @Test
    void findByIdWithInvalidId() {
        ResponseEntity<?> response = sessionController.findById("invalid");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void updateWithInvalidId() {
        ResponseEntity<?> response = sessionController.update("invalid", sessionDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void saveWithInvalidId() {
        ResponseEntity<?> response = sessionController.save("invalid");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void participateWithInvalidId() {
        ResponseEntity<?> response = sessionController.participate("invalid", "10");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void noLongerParticipateWithInvalidId() {
        ResponseEntity<?> response = sessionController.noLongerParticipate("invalid", "10");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}