package com.example.leonproject.controller.service;


import com.example.leonproject.config.MyLocaleResolver;
import com.example.leonproject.controller.pojo.RegistrationDTO;
import com.example.leonproject.controller.pojo.RegistrationResponseDTO;
import com.example.leonproject.dao.entity.AccountDO;
import com.example.leonproject.dao.repository.AccountRepository;
import com.example.leonproject.exception.AccountExistedException;
import com.example.leonproject.util.BCryptUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;


@Service
public class RegistrationService {

    private final AccountRepository accountRepository;

    public RegistrationService(AccountRepository accountRepository, MessageSource messageSource, MyLocaleResolver myLocaleResolver) {
        this.accountRepository = accountRepository;
    }

    public RegistrationResponseDTO createUser(RegistrationDTO registrationDTO, HttpServletRequest httpServletRequest) {

        if (accountRepository.existsByUsername(registrationDTO.getUsername())) {
            throw new AccountExistedException(httpServletRequest);
        }

        String encodedPassword = BCryptUtil.passwordEncode(registrationDTO.getPassword());

        AccountDO newRegistration = new AccountDO();
        newRegistration.setUsername(registrationDTO.getUsername());
        newRegistration.setPassword(encodedPassword);
        accountRepository.save(newRegistration);

        return new RegistrationResponseDTO(1, "Registration Success!");
    }
}
