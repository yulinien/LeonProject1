package com.example.leonproject.controller;


import com.example.leonproject.controller.pojo.RegistrationDTO;
import com.example.leonproject.controller.pojo.RegistrationResponseDTO;
import com.example.leonproject.controller.service.RegistrationService;
import com.example.leonproject.dao.entity.AccountDO;
import com.example.leonproject.dao.repository.AccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.invocation.InvocationOnMock;
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
@WebMvcTest(RegistrationController.class)
@ExtendWith(MockitoExtension.class)
public class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegistrationService registrationService;

    @Autowired
    private AccountRepository accountRepository;

    private AccountDO accountDO;

    private RegistrationDTO registrationDTO;

    private RegistrationResponseDTO registrationResponseDTO;


    @Test
    public void registrationController_Registration_ReturnResponseDTO() throws Exception {

        RegistrationDTO registrationDTO = new RegistrationDTO();
        registrationDTO.setUsername("testUser");
        registrationDTO.setPassword("testPassword");

        String jsonContent = new ObjectMapper().writeValueAsString(registrationDTO);

        given(registrationService.createUser(ArgumentMatchers.any(), ArgumentMatchers.any())).willAnswer((InvocationOnMock::getArguments));

        ResultActions response = mockMvc.perform(post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", CoreMatchers.is(registrationDTO.getUsername())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password", CoreMatchers.is(registrationDTO.getPassword())));

    }


}
