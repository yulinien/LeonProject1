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
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        MockitoAnnotations.openMocks(this);
        loginDTO = new LoginDTO();
        loginDTO.setUsername("testUser");
        loginDTO.setPassword("testPassword");
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

        LoginResponseDTO response = loginService.userLogin(loginDTO);

        assertEquals(-1, response.getStatus());
        assertEquals("user not found", response.getErrorMessage());
    }

    @Test
    public void userLogin_PasswordIncorrect_ReturnsFailure() {
        try (MockedStatic<BCryptUtil> mocked = Mockito.mockStatic(BCryptUtil.class)) {
            LoginDTO loginDTO = new LoginDTO();
            loginDTO.setUsername("testUser");
            loginDTO.setPassword("wrongPassword"); // 密碼不正確

            AccountDO accountDO = new AccountDO();
            accountDO.setUsername("testUser");
            accountDO.setPassword("hashedPassword");
            when(accountMapper.getUser(anyString())).thenReturn(accountDO);

            mocked.when(() -> BCryptUtil.passwordCheck("wrongPassword", accountDO.getPassword())).thenReturn(false);

            LoginResponseDTO response = loginService.userLogin(loginDTO);

            assertEquals(-1, response.getStatus());
            assertEquals("passwordInvalid", response.getErrorMessage());
        }
    }


    @Test
    public void userLogin_SuccessfulLogin_ReturnsSuccess() {

        AccountDO accountDO = new AccountDO();
        accountDO.setUsername("testUser");
        // 假設此是經過加密的密碼
        String fakeHashedPassword = "fakeHashedPassword";
        accountDO.setPassword(fakeHashedPassword);

        when(accountMapper.getUser(anyString())).thenReturn(accountDO);

        try (MockedStatic<BCryptUtil> mocked = Mockito.mockStatic(BCryptUtil.class)) {

            // 模擬 BCryptUtil.passwordCheck 的行為，讓它在收到任何兩個字串參數時，都回傳 true
            mocked.when(() -> BCryptUtil.passwordCheck(anyString(), anyString())).thenReturn(true);

            LoginDTO loginDTO = new LoginDTO();
            loginDTO.setUsername("testUser");
            loginDTO.setPassword("testPassword");

            LoginResponseDTO response = loginService.userLogin(loginDTO);

            assertEquals(1, response.getStatus());
            assertEquals("Login Success", response.getErrorMessage());
        }
    }
}
