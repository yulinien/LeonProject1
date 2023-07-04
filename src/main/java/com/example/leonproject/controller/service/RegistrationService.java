package com.example.leonproject.controller.service;


import com.example.leonproject.controller.pojo.RegistrationDTO;
import com.example.leonproject.controller.pojo.RegistrationResponseDTO;
import com.example.leonproject.dao.entity.AccountDO;
import com.example.leonproject.dao.repository.AccountRepository;
import com.example.leonproject.util.BCryptUtil;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {
    private final AccountRepository accountRepository;

    public RegistrationService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public RegistrationResponseDTO createUser(RegistrationDTO registrationDTO) {

        if (accountRepository.existsByUsername(registrationDTO.getUsername())) {
            return new RegistrationResponseDTO(-1, "Username already existed");
        }

        String encodedPassword = BCryptUtil.passwordEncode(registrationDTO.getPassword());

        AccountDO newRegistration = new AccountDO();
        newRegistration.setUsername(registrationDTO.getUsername());
        newRegistration.setPassword(encodedPassword);
        accountRepository.save(newRegistration);

        return new RegistrationResponseDTO(1, "Registration Success!");
    }
}
