package com.example.leonproject.controller.service;

import com.example.leonproject.controller.pojo.ChangePasswordDTO;
import com.example.leonproject.controller.pojo.ChangePasswordResponseDTO;
import com.example.leonproject.dao.entity.AccountDO;
import com.example.leonproject.dao.repository.AccountRepository;
import com.example.leonproject.exception.InputValidationException;
import com.example.leonproject.util.BCryptUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ChangePasswordServiceTest {

    @InjectMocks
    ChangePasswordService changePasswordService;

    @Mock
    AccountRepository accountRepository;

    private ChangePasswordDTO changePasswordDTO;

    @BeforeEach
    public void setup() {
        changePasswordDTO = new ChangePasswordDTO();
        changePasswordDTO.setUsername("testUser");
        changePasswordDTO.setOldPassword("oldPassword");
        changePasswordDTO.setNewPassword("newPassword");
    }


    @Test
    public void changePassword_AccountNotFound_ThrowsException() {

        when(accountRepository.findAccountByUsername(changePasswordDTO.getUsername())).thenReturn(Optional.empty());
        assertThrows(InputValidationException.class, () -> changePasswordService.changePassword(changePasswordDTO));

    }

    @Test
    public void changePassword_OldPasswordIncorrect_ReturnsFailure() {
        try (MockedStatic<BCryptUtil> mocked = Mockito.mockStatic(BCryptUtil.class)) {

            AccountDO accountDO = new AccountDO();
            accountDO.setUsername("testUser");
            accountDO.setPassword("hashedPassword");

            when(accountRepository.findAccountByUsername(changePasswordDTO.getUsername())).thenReturn(Optional.of(accountDO));
            mocked.when(() -> BCryptUtil.passwordCheck("oldPassword", "hashedPassword")).thenReturn(false);

            ChangePasswordResponseDTO response = changePasswordService.changePassword(changePasswordDTO);
            assertAll(() -> assertEquals(-1, response.getStatus()),
                    () -> assertEquals("old password incorrect", response.getErrorMessage()));
        }
    }

    @Test
    public void changePassword_UserChangePassword_ReturnsSuccess() {
        try (MockedStatic<BCryptUtil> mocked = Mockito.mockStatic(BCryptUtil.class)) {

            AccountDO accountDO = new AccountDO();
            accountDO.setUsername("testUser");
            accountDO.setPassword("hashedPassword");


            when(accountRepository.findAccountByUsername(changePasswordDTO.getUsername())).thenReturn(Optional.of(accountDO));
            mocked.when(() -> BCryptUtil.passwordCheck("oldPassword", "hashedPassword")).thenReturn(true);
            mocked.when(() -> BCryptUtil.passwordEncode(changePasswordDTO.getNewPassword())).thenReturn("hashedPassword");

            ChangePasswordResponseDTO response = changePasswordService.changePassword(changePasswordDTO);
            assertAll("User ChangePassword", () -> assertEquals(1, response.getStatus()),
                    () -> assertEquals("ChangePassword Success", response.getErrorMessage()));
        }
    }
}
