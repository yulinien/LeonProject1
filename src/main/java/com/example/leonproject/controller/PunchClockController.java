package com.example.leonproject.controller;


import com.example.leonproject.controller.pojo.PunchClockDTO;
import com.example.leonproject.controller.pojo.PunchClockResponseDTO;
import com.example.leonproject.controller.service.PunchClockService;
import jakarta.annotation.security.PermitAll;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@PermitAll
@RestController
public class PunchClockController {

    private final PunchClockService punchClockService;

    public PunchClockController(PunchClockService punchClockService) {
        this.punchClockService = punchClockService;
    }

    @PostMapping("/clock_in")
    public ResponseEntity<PunchClockResponseDTO> clockIn(@RequestBody PunchClockDTO punchClockDTO) {

        PunchClockResponseDTO punchClockResponseDTO = punchClockService.punchClock(punchClockDTO);

        return ResponseEntity.ok().body(punchClockResponseDTO);
    }
}
