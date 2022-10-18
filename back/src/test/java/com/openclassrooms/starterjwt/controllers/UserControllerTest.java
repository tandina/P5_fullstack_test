package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc

class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Mock
    private UserMapper userMapper;
    @MockBean
    private UserService userService;

    private User user;
    private UserDto userDto;
    private UserController userController;

    @BeforeEach
    void setUp() {
        LocalDateTime date = LocalDateTime.now();
        user = new User(10L,"yoga@yoga.com","lastname","firstname","yoga!123",true, date,date);
        userDto = new UserDto(10l,"yoga@yoga.com","lastname","firstname",true,"yaoga!123", date,date);

        userController = new UserController(userService, userMapper);
    }

    @Test
    void findUserById() {
        Long testUserId = 10L;
        when(userService.findById(testUserId)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        ResponseEntity<?> response = userController.findById(String.valueOf(testUserId));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDto, response.getBody());
    }
    @Test
    @WithMockUser(username = "yoga@yoga.com", roles = "ADMIN")
    void cannotFindById() throws Exception {
        String testUserId = "invalid";

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/user/" + testUserId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @Test
    @WithMockUser(username = "yoga@yoga.com", roles = "ADMIN")
    public void testDeleteUser() throws Exception {
        Long testUserId = 10L;

        User user = new User();
        user.setEmail("yoga@yoga.com");

        when(userService.findById(testUserId)).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/user/" + testUserId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }



    @Test
    @WithMockUser(username = "yoga@yoga.com", roles = "ADMIN")
    public void testDeleteUserWithInvalidId() throws Exception {
        String testUserId = "invalid";

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/user/" + testUserId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}