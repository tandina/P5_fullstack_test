package com.openclassrooms.starterjwt.security.jwt;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import ch.qos.logback.classic.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;



import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JwtUtilsTest {
    private JwtUtils jwtUtils;

    private ListAppender<ILoggingEvent> listAppender;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "testSecret");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 60000);

        Logger logger = (Logger) org.slf4j.LoggerFactory.getLogger(JwtUtils.class);
        listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);
    }

    @Test
    void shouldGenerateJwtToken() {
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(userDetails.getUsername()).thenReturn("testUser");
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);

        String token = jwtUtils.generateJwtToken(authentication);

        assertThat(token).isNotEmpty();
    }

    @Test
    void shouldGetUsernameFromJwtToken() {
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(userDetails.getUsername()).thenReturn("testUser");
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);

        String token = jwtUtils.generateJwtToken(authentication);
        String username = jwtUtils.getUserNameFromJwtToken(token);

        assertThat(username).isEqualTo("testUser");
    }

    @Test
    void shouldValidateJwtToken() {
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(userDetails.getUsername()).thenReturn("testUser");
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);

        String token = jwtUtils.generateJwtToken(authentication);
        boolean isValid = jwtUtils.validateJwtToken(token);

        assertThat(isValid).isTrue();
    }
    @Test
    void shouldLogErrorWhenTokenIsInvalid() {
        jwtUtils.validateJwtToken("invalid token");

        assertThat(listAppender.list)
                .extracting(ILoggingEvent::getFormattedMessage, ILoggingEvent::getLevel)
                .contains(tuple("Invalid JWT token: JWT strings must contain exactly 2 period characters. Found: 0", Level.ERROR));
    }

    @Test
    void shouldLogErrorWhenSignatureIsInvalid() {
        // Générer un token JWT avec une signature invalide
        String invalidSignatureToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0VXNlciIsImlhdCI6MTYxNzI0ODQ2MywiZXhwIjoxNjE3MjUyMDYzfQ.invalid_signature";

        jwtUtils.validateJwtToken(invalidSignatureToken);

        assertThat(listAppender.list)
                .extracting(ILoggingEvent::getFormattedMessage, ILoggingEvent::getLevel)
                .contains(tuple("Invalid JWT signature: JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted.", Level.ERROR));
    }
    @Test
    public void shouldLogErrorWhenClaimsStringIsEmpty() {

        String invalidToken = "";
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "testSecret");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 60000);

        jwtUtils.validateJwtToken(invalidToken);

        assertThat(listAppender.list)
                .extracting(ILoggingEvent::getFormattedMessage, ILoggingEvent::getLevel)
                .contains(tuple("JWT claims string is empty: JWT String argument cannot be null or empty.", Level.ERROR));
    }


}