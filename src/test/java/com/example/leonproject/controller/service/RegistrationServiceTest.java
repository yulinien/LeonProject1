package com.example.leonproject.controller.service;

import com.example.leonproject.controller.pojo.RegistrationDTO;
import com.example.leonproject.controller.pojo.RegistrationResponseDTO;
import com.example.leonproject.dao.repository.AccountRepository;
import com.example.leonproject.exception.AccountExistedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceTest {

    @InjectMocks
    private RegistrationService registrationService;

    @Mock
    private AccountRepository accountRepository;

    private RegistrationDTO registrationDTO;

    @BeforeEach
    public void setup() {
        registrationDTO = new RegistrationDTO();
        registrationDTO.setUsername("testUser");
        registrationDTO.setPassword("testPassword");
    }

    @Test
    public void createUser_AlreadyExisted_ThrowsException() {

        MockHttpServletRequest request = new MockHttpServletRequest();
        when(accountRepository.existsByUsername(registrationDTO.getUsername())).thenReturn(Boolean.TRUE);

        assertThrows(AccountExistedException.class, () -> registrationService.createUser(registrationDTO, request));
    }

    @Test
    public void createUser_UserRegistration_Success() {

        MockHttpServletRequest request = new MockHttpServletRequest();
        when(accountRepository.existsByUsername(registrationDTO.getUsername())).thenReturn(Boolean.FALSE);

        RegistrationResponseDTO responseDTO = registrationService.createUser(registrationDTO, request);

        assertAll("Registration Response",
                () -> assertEquals(1, responseDTO.getStatus()),
                () -> assertEquals("Registration Success!", responseDTO.getErrorMessage()));
    }
}