package com.example.leonproject.controller;


import com.example.leonproject.controller.service.ChangePasswordService;
import com.example.leonproject.dao.entity.AccountDO;
import com.example.leonproject.util.JWTUtil;
import com.example.leonproject.controller.pojo.ChangePasswordDTO;
import com.example.leonproject.controller.pojo.ChangePasswordResponseDTO;
import com.example.leonproject.dao.repository.AccountRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChangePasswordController {

    private final ChangePasswordService changePasswordService;

    public ChangePasswordController(ChangePasswordService changePasswordService) {
        this.changePasswordService = changePasswordService;
    }

    @Operation(security = {@SecurityRequirement(name = "JWT Token")})
    @PostMapping("/changePassword")
    public ResponseEntity<ChangePasswordResponseDTO> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {

        ChangePasswordResponseDTO changePasswordResponseDTO = changePasswordService.changePassword(changePasswordDTO);

        return ResponseEntity.ok().body(changePasswordResponseDTO);
    }
}


