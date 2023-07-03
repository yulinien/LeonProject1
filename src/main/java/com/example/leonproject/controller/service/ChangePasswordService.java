package com.example.leonproject.controller.service;

import com.example.leonproject.controller.pojo.ChangePasswordDTO;
import com.example.leonproject.controller.pojo.ChangePasswordResponseDTO;
import com.example.leonproject.dao.entity.AccountDO;
import com.example.leonproject.dao.repository.AccountRepository;
import com.example.leonproject.util.BCryptUtil;
import org.springframework.stereotype.Service;

@Service
public class ChangePasswordService {
    private final AccountRepository accountRepository;

    public ChangePasswordService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    public ChangePasswordResponseDTO changePassword(ChangePasswordDTO changePasswordDTO){

        AccountDO accountDO = accountRepository.findAccountByUsernameAndPassword(changePasswordDTO.getUsername(), changePasswordDTO.getOldPassword())
                .orElseThrow(() -> new RuntimeException("Account not found"));
        accountDO.setPassword(BCryptUtil.passwordEncode(changePasswordDTO.getNewPassword()));
        accountRepository.save(accountDO);
        return new ChangePasswordResponseDTO(1,"ChangePassword Success");

    }
}
