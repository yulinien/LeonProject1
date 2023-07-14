package com.example.leonproject.controller;


import com.example.leonproject.controller.pojo.RegistrationDTO;
import com.example.leonproject.controller.pojo.RegistrationResponseDTO;
import com.example.leonproject.controller.service.RegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "註冊API")
@PermitAll
@RestController
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @Operation(summary = "註冊" , description = "需要攜帶JWT Token")
    @PostMapping("/registration")
    public ResponseEntity<RegistrationResponseDTO> createUser(@RequestBody RegistrationDTO registrationDTO, HttpServletRequest httpServletRequest) {

        RegistrationResponseDTO registrationResponseDTO = registrationService.createUser(registrationDTO, httpServletRequest);

        return ResponseEntity.ok().body(registrationResponseDTO); //可以改用throw new RuntimeException();
    }
}
