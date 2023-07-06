package com.example.leonproject.controller;


import com.example.leonproject.controller.pojo.RegistrationDTO;
import com.example.leonproject.controller.pojo.RegistrationResponseDTO;
import com.example.leonproject.controller.service.RegistrationService;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

@PermitAll
@RestController
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/registration")
    public ResponseEntity<RegistrationResponseDTO> createUser(@RequestBody RegistrationDTO registrationDTO, HttpServletRequest httpServletRequest) {
        
        RegistrationResponseDTO registrationResponseDTO = registrationService.createUser(registrationDTO, httpServletRequest);

        if (registrationResponseDTO.getStatus() == 1) {
            return ResponseEntity.ok(registrationResponseDTO);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(registrationResponseDTO); //可以改用throw new RuntimeException();
        }
    }
}
