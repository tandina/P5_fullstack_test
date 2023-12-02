package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.FilterChain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AuthTokenFilterTest {
    private JwtUtils jwtUtils;
    private UserDetailsServiceImpl userDetailsService;
    private AuthTokenFilter authTokenFilter;

    @BeforeEach
    void setUp() {
        jwtUtils = mock(JwtUtils.class);
        userDetailsService = mock(UserDetailsServiceImpl.class);
        authTokenFilter = new AuthTokenFilter();
        ReflectionTestUtils.setField(authTokenFilter, "jwtUtils", jwtUtils);
        ReflectionTestUtils.setField(authTokenFilter, "userDetailsService", userDetailsService);
    }

    @Test
    void shouldAuthenticateWhenValidTokenIsProvided() throws Exception {
        String testToken = "Bearer testToken";
        String testUsername = "testUsername";
        UserDetails testUserDetails = mock(UserDetails.class);

        when(jwtUtils.validateJwtToken(anyString())).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken(anyString())).thenReturn(testUsername);
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(testUserDetails);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", testToken);
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain mockFilterChain = mock(FilterChain.class);
        doNothing().when(mockFilterChain).doFilter(any(), any());

        authTokenFilter.doFilterInternal(request, response, mockFilterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).isEqualTo(testUserDetails);
    }
}