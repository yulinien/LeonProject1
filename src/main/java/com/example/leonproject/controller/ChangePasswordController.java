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
    private final JWTUtil jwtUtil;
    private final ChangePasswordService changePasswordService;
    private final AuthenticationManager authenticationManager;

    public ChangePasswordController(AuthenticationManager authenticationManager,JWTUtil jwtUtil, ChangePasswordService changePasswordService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.changePasswordService = changePasswordService;
    }

//        @PostMapping("/security/login")//將整個三個API合而為一 JWT統一由security管理
//    public ResponseEntity<?> login(@RequestBody ChangePasswordDTO changePasswordDTO) {
//
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        changePasswordDTO.getUsername(),
//                        changePasswordDTO.getOldPassword()));
// ** notice      SecurityContextHolder.getContext().setAuthentication(authentication);
//        String token = jwtUtil.generateToken(changePasswordDTO.getUsername());
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", "Bearer " + token);
//        return ResponseEntity.ok().headers(headers).body(new ChangePasswordResponseDTO(1, "Login Success checkout your headers"));
//    }

//    @PostMapping("/security/register")
//    public ResponseEntity<?> register(@RequestBody ChangePasswordDTO changePasswordDTO) {
//        if (accountRepository.existsByUsername(changePasswordDTO.getUsername())) {
//            return new ResponseEntity<>("Username is taken!", HttpStatus.BAD_REQUEST);
//        }
//
//        AccountDO accountDO = new AccountDO();
//        accountDO.setUsername(changePasswordDTO.getUsername());
//        accountDO.setPassword(passwordEncoder.encode(changePasswordDTO.getOldPassword()));
//        accountRepository.save(accountDO);
//
//        return new ResponseEntity<>("User registered success!", HttpStatus.OK);
//    }

    @Operation(security = {@SecurityRequirement(name = "JWT Token")})
    @PostMapping("/security/changePassword")
    public ResponseEntity<ChangePasswordResponseDTO> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        ChangePasswordResponseDTO changePasswordResponseDTO = changePasswordService.changePassword(changePasswordDTO);
        return ResponseEntity.ok().body(changePasswordResponseDTO);

    }
}


