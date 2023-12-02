package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class SessionServiceTest {

    private SessionRepository sessionRepository;
    private UserRepository userRepository;
    private SessionService sessionService;

    @BeforeEach
    void setUp() {
        sessionRepository = Mockito.mock(SessionRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        sessionService = new SessionService(sessionRepository, userRepository);
    }

    @Test
    void shouldCreateSession() {
        Session session = new Session();
        session.setId(1L);

        when(sessionRepository.save(session)).thenReturn(session);

        Session createdSession = sessionService.create(session);

        assertThat(createdSession.getId()).isEqualTo(1L);
    }

    @Test
    void shouldGetSessionById() {
        Session session = new Session();
        session.setId(1L);

        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        Session foundSession = sessionService.getById(1L);

        assertThat(foundSession.getId()).isEqualTo(1L);
    }

    @Test
    void shouldUpdateSession() {
        Session session = new Session();
        session.setId(1L);

        when(sessionRepository.save(session)).thenReturn(session);

        Session updatedSession = sessionService.update(1L, session);

        assertThat(updatedSession.getId()).isEqualTo(1L);
    }

    @Test
    void shouldParticipateInSession() {
        Session session = new Session();
        session.setId(1L);
        session.setUsers(new ArrayList<>());

        User user = new User();
        user.setId(1L);

        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        sessionService.participate(1L, 1L);

        assertThat(session.getUsers()).contains(user);
    }

    @Test
    void shouldNoLongerParticipateInSession() {
        Session session = new Session();
        session.setId(1L);
        session.setUsers(new ArrayList<>());

        User user = new User();
        user.setId(1L);
        session.getUsers().add(user);

        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        sessionService.noLongerParticipate(1L, 1L);

        assertThat(session.getUsers()).doesNotContain(user);
    }
    @Test
    void shouldDeleteSession() {
        Long sessionId = 1L;
        doNothing().when(sessionRepository).deleteById(sessionId);
        sessionService.delete(sessionId);
        verify(sessionRepository, times(1)).deleteById(sessionId);
    }

    @Test
    void shouldFindAllSessions() {
        List<Session> sessions = new ArrayList<>();
        sessions.add(new Session());
        sessions.add(new Session());

        when(sessionRepository.findAll()).thenReturn(sessions);

        List<Session> foundSessions = sessionService.findAll();

        assertThat(foundSessions).isEqualTo(sessions);
    }
}