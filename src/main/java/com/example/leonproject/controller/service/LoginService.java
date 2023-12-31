package com.example.leonproject.controller.service;

import com.example.leonproject.controller.pojo.LoginDTO;
import com.example.leonproject.controller.pojo.LoginResponseDTO;
import com.example.leonproject.dao.entity.AccountDO;
import com.example.leonproject.dao.mybatis.AccountMapper;
import com.example.leonproject.util.BCryptUtil;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final AccountMapper accountMapper;

    public LoginService(AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
    }

    public LoginResponseDTO userLogin(LoginDTO loginDTO) {
        if (loginDTO.getUsername() == null || loginDTO.getUsername().isEmpty()) {
            return new LoginResponseDTO(-1, "please type the username");
        }

        AccountDO accountDO = accountMapper.getUser(loginDTO.getUsername());

        if (accountDO == null) {
            return new LoginResponseDTO(-1, "user not found");
        } else {
            return BCryptUtil.passwordCheck(loginDTO.getPassword(), accountDO.getPassword())
                    ? new LoginResponseDTO(1, "Login Success")
                    : new LoginResponseDTO(-1, "passwordInvalid");
        }
    }
}