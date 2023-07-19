package com.example.leonproject.controller.service;

import com.example.leonproject.controller.pojo.LoginDTO;
import com.example.leonproject.controller.pojo.LoginResponseDTO;
import com.example.leonproject.dao.entity.AccountDO;
import com.example.leonproject.dao.mybatis.AccountMapper;
import com.example.leonproject.exception.InputValidationException;
import com.example.leonproject.util.BCryptUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {

    @InjectMocks
    private LoginService loginService;

    @Mock
    private AccountMapper accountMapper;

    private LoginDTO loginDTO;

    @BeforeEach
    public void setup() {
        loginService = new LoginService(accountMapper);
        loginDTO = new LoginDTO();
        loginDTO.setUsername("testUser");
        loginDTO.setPassword("testPassword");
        assertNotNull(accountMapper);
    }

    @Test
    public void userLogin_UsernameIsEmpty_ThrowsException() {
        loginDTO.setUsername("");
        assertThrows(InputValidationException.class, () -> loginService.userLogin(loginDTO));
    }

    @Test
    public void userLogin_PasswordIsEmpty_ThrowsException() {
        loginDTO.setPassword("");
        assertThrows(InputValidationException.class, () -> loginService.userLogin(loginDTO));
    }

    @Test
    public void userLogin_UserNotFound_ReturnsFailure() {
        when(accountMapper.getUser(loginDTO.getUsername())).thenReturn(null);
        LoginResponseDTO response = loginService.userLogin(loginDTO);

        assertAll("User Not Found Response",
                () -> assertEquals(-1, response.getStatus()),
                () -> assertEquals("user not found", response.getErrorMessage()));
    }

    @Test
    public void userLogin_PasswordIncorrect_ReturnsFailure() {
        try (MockedStatic<BCryptUtil> mocked = Mockito.mockStatic(BCryptUtil.class)) {

            LoginDTO loginDTO = new LoginDTO();
            loginDTO.setUsername("testUser");
            loginDTO.setPassword("wrongPassword");

            AccountDO accountDO = new AccountDO();
            accountDO.setUsername("testUser");
            accountDO.setPassword("hashedPassword");

            when(accountMapper.getUser(loginDTO.getUsername())).thenReturn(accountDO);
            mocked.when(() -> BCryptUtil.passwordCheck("wrongPassword", "hashedPassword")).thenReturn(false);

            LoginResponseDTO response = loginService.userLogin(loginDTO);

            assertEquals(-1, response.getStatus());
            assertEquals("passwordInvalid", response.getErrorMessage());
        }
    }


    @Test
    public void userLogin_SuccessfulLogin_ReturnsSuccess() {

        try (MockedStatic<BCryptUtil> mocked = Mockito.mockStatic(BCryptUtil.class)) {

            AccountDO accountDO = new AccountDO();
            accountDO.setId(1);
            accountDO.setUsername("testUser");
            accountDO.setPassword("fakeHashedPassword");

            when(accountMapper.getUser(anyString())).thenReturn(accountDO);
            mocked.when(() -> BCryptUtil.passwordCheck("testPassword", "fakeHashedPassword")).thenReturn(true);

            LoginResponseDTO response = loginService.userLogin(loginDTO);

            assertEquals(1, response.getStatus());
            assertEquals("Login Success", response.getErrorMessage());
        }
    }
}
