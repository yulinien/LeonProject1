package com.example.leonproject.controller.service;


import com.example.leonproject.config.MyLocaleResolver;
import com.example.leonproject.controller.pojo.RegistrationDTO;
import com.example.leonproject.controller.pojo.RegistrationResponseDTO;
import com.example.leonproject.dao.entity.AccountDO;
import com.example.leonproject.dao.repository.AccountRepository;
import com.example.leonproject.util.BCryptUtil;
//import com.example.leonproject.config.MyLocaleResolver;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;


@Service
public class RegistrationService {

    private final AccountRepository accountRepository;

    private final MessageSource messageSource;

    private final MyLocaleResolver myLocaleResolver;

    public RegistrationService(AccountRepository accountRepository, MessageSource messageSource, MyLocaleResolver myLocaleResolver) {
        this.accountRepository = accountRepository;
        this.messageSource = messageSource;
        this.myLocaleResolver = myLocaleResolver;
    }

    public RegistrationResponseDTO createUser(RegistrationDTO registrationDTO, HttpServletRequest httpServletRequest) {

        if (accountRepository.existsByUsername(registrationDTO.getUsername())) {
            Locale locale = myLocaleResolver.resolveLocale(httpServletRequest);
            String errorMessage = messageSource.getMessage("error.usernameExists", null, locale);
            return new RegistrationResponseDTO(-1, errorMessage);
        }

        String encodedPassword = BCryptUtil.passwordEncode(registrationDTO.getPassword());

        AccountDO newRegistration = new AccountDO();
        newRegistration.setUsername(registrationDTO.getUsername());
        newRegistration.setPassword(encodedPassword);
        accountRepository.save(newRegistration);

        return new RegistrationResponseDTO(1, "Registration Success!");
    }
}
