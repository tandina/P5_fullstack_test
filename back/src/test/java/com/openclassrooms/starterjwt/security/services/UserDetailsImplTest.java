package com.openclassrooms.starterjwt.security.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

class UserDetailsImplTest {
    private UserDetailsImpl userDetailsImpl;

    @BeforeEach
    void init(){
        userDetailsImpl = new UserDetailsImpl(1L,"username","firstname","lastname",true,"password");
    }

    @Test
    void testUserDetailsImpl() {
        assertThat(userDetailsImpl.getAuthorities()).isInstanceOf(HashSet.class);
        assertThat(userDetailsImpl.isAccountNonExpired()).isTrue();
        assertThat(userDetailsImpl.isAccountNonLocked()).isTrue();
        assertThat(userDetailsImpl.isCredentialsNonExpired()).isTrue();
        assertThat(userDetailsImpl.isEnabled()).isTrue();
        assertThat(userDetailsImpl.equals(userDetailsImpl)).isTrue();
        assertThat(userDetailsImpl.equals(null)).isFalse();

        UserDetailsImpl user = new UserDetailsImpl(2L,"username","firstname","lastname",true,"password");
        assertThat(userDetailsImpl.equals(user)).isFalse();

        assertThat(userDetailsImpl.getId()).isEqualTo(1L);
        assertThat(userDetailsImpl.getUsername()).isEqualTo("username");
        assertThat(userDetailsImpl.getFirstName()).isEqualTo("firstname");
        assertThat(userDetailsImpl.getLastName()).isEqualTo("lastname");
        assertThat(userDetailsImpl.getAdmin()).isTrue();
        assertThat(userDetailsImpl.getPassword()).isEqualTo("password");
        assertThat(userDetailsImpl.builder()).isNotNull();
    }
}