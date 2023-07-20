package com.example.leonproject.controller;


import com.example.leonproject.config.security.JWTAuthenticationFilter;
import com.example.leonproject.controller.pojo.LoginDTO;
import com.example.leonproject.controller.pojo.LoginResponseDTO;
import com.example.leonproject.controller.service.LoginService;
import com.example.leonproject.exception.GlobalExceptionHandler;
import com.example.leonproject.util.JWTUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest
@ExtendWith(MockitoExtension.class)
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    LoginController loginController;

    @MockBean
    private LoginService loginService;

    @MockBean
    private JWTUtil jwtUtil;

    @MockBean
    ChangePasswordController changePasswordController;

    @MockBean
    PunchClockController punchClockController;

    @MockBean
    RegistrationController registrationController;

    @MockBean
    private JWTAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    public void loginController_UserLogin_ReturnsSuccessAndJwtToken() throws Exception {

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("testUser");
        loginDTO.setPassword("testPassword");

        String jsonContent = new ObjectMapper().writeValueAsString(loginDTO);

        LoginResponseDTO loginResponseDTO = new LoginResponseDTO(1, "Login Success");

        String expectedToken = "mockedToken";


        given(jwtUtil.generateToken(loginDTO.getUsername())).willReturn(expectedToken);
        given(loginService.userLogin((ArgumentMatchers.any()))).willReturn(loginResponseDTO);

        ResultActions response = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonContent));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Authorization", "Bearer " + expectedToken))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(loginResponseDTO.getStatus())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage", CoreMatchers.is((loginResponseDTO.getErrorMessage()))));

    }
}
