package com.example.leonproject.controller;


import com.example.leonproject.controller.pojo.ChangePasswordDTO;
import com.example.leonproject.controller.pojo.ChangePasswordResponseDTO;
import com.example.leonproject.controller.service.ChangePasswordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
@Tag(name = "更換密碼API")
@RestController
public class ChangePasswordController {

    private final ChangePasswordService changePasswordService;

    public ChangePasswordController(ChangePasswordService changePasswordService) {
        this.changePasswordService = changePasswordService;
    }

    @Operation(security = {@SecurityRequirement(name = "JWT Token")} , summary = "更換密碼", description = "需要攜帶 JWT Token ")
    @PostMapping("/changePassword")
    public ResponseEntity<ChangePasswordResponseDTO> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {

        ChangePasswordResponseDTO changePasswordResponseDTO = changePasswordService.changePassword(changePasswordDTO);

        return ResponseEntity.ok().body(changePasswordResponseDTO);
    }
}


