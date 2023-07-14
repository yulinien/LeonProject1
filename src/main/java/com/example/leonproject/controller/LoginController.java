package com.example.leonproject.controller;

import com.example.leonproject.controller.pojo.LoginDTO;
import com.example.leonproject.controller.pojo.LoginResponseDTO;
import com.example.leonproject.controller.service.LoginService;
import com.example.leonproject.util.JWTUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "登入API")
@RestController
public class LoginController {

    private final LoginService loginService;

    private final JWTUtil jwtUtil;

    public LoginController(LoginService loginService, JWTUtil jwtUtil) {
        this.loginService = loginService;
        this.jwtUtil = jwtUtil;
    }

    @Operation(summary = "登入後會回傳JWT Token", description = "帳號密碼不得為空值")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> userLogin(@RequestBody LoginDTO loginDTO) {

        LoginResponseDTO responseDTO = loginService.userLogin(loginDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwtUtil.generateToken(loginDTO.getUsername()));

        return ResponseEntity.ok().headers(headers).body(responseDTO);

    }
}
