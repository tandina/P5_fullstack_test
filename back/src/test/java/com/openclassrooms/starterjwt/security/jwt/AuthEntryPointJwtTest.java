package com.openclassrooms.starterjwt.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthEntryPointJwtTest {
    private AuthEntryPointJwt authEntryPointJwt = new AuthEntryPointJwt();

    @Test
    void shouldReturnUnauthorizedStatusWhenCommenceIsCalled() throws IOException, ServletException {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        MockHttpServletResponse mockResponse = new MockHttpServletResponse();
        AuthenticationException mockAuthException = mock(AuthenticationException.class);

        when(mockAuthException.getMessage()).thenReturn("Unauthorized error message");

        authEntryPointJwt.commence(mockRequest, mockResponse, mockAuthException);

        assertThat(mockResponse.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> responseBody = mapper.readValue(mockResponse.getContentAsString(), Map.class);

        assertThat(responseBody.get("status")).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(responseBody.get("error")).isEqualTo("Unauthorized");
        assertThat(responseBody.get("message")).isEqualTo("Unauthorized error message");
        assertThat(responseBody.get("path")).isEqualTo(mockRequest.getServletPath());
    }
}