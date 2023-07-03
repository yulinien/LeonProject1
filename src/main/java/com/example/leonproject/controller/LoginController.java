package com.example.leonproject.controller;

import com.example.leonproject.controller.pojo.LoginDTO;
import com.example.leonproject.controller.pojo.LoginResponseDTO;
import com.example.leonproject.controller.service.LoginService;
import com.example.leonproject.util.JWTUtil;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@PermitAll
@RestController
public class LoginController {

    private final LoginService loginService;

    private final JWTUtil jwtUtil;

    @Autowired
    public LoginController(LoginService loginService, JWTUtil jwtUtil) {
        this.loginService = loginService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> userLogin(@RequestBody LoginDTO loginDTO) {
        LoginResponseDTO responseDTO = loginService.userLogin(loginDTO);
        if (responseDTO.getStatus() == -1) {
            return ResponseEntity.badRequest().body(responseDTO);
        } else {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + jwtUtil.generateToken(loginDTO.getUsername()));
            return ResponseEntity.ok().headers(headers).body(new LoginResponseDTO(1, "Login Success"));
        }
    }
}
