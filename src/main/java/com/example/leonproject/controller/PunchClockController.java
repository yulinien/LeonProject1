package com.example.leonproject.controller;


import com.example.leonproject.controller.pojo.PunchClockDTO;
import com.example.leonproject.controller.pojo.PunchClockResponseDTO;
import com.example.leonproject.controller.service.PunchClockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "打卡API")
@RestController
public class PunchClockController {

    private final PunchClockService punchClockService;

    public PunchClockController(PunchClockService punchClockService) {
        this.punchClockService = punchClockService;
    }

    @Operation(summary = "打卡" ,description = "當天第一次打卡會記錄到 clock_in 欄位 第二次打卡之後的紀錄則會記錄到 clock_out欄位 視為下班打卡")
    @PostMapping("/clock-in")
    public ResponseEntity<PunchClockResponseDTO> clockIn(@RequestBody PunchClockDTO punchClockDTO) {

        PunchClockResponseDTO punchClockResponseDTO = punchClockService.punchClock(punchClockDTO);

        return ResponseEntity.ok().body(punchClockResponseDTO);
    }

    @Operation(summary = "故意失敗的打卡" ,description = "當天的第一次紀錄 clock_out 欄位的時間和第二次紀錄 clock_out 欄位的時間差小於50秒 系統會故意產生錯誤 但仍然會讓打卡紀錄儲存")
    @PostMapping("/fail-clock-in")
    public ResponseEntity<PunchClockResponseDTO> failClockIn(@RequestBody PunchClockDTO punchClockDTO) {

        PunchClockResponseDTO punchClockResponseDTO = punchClockService.failPunchClock(punchClockDTO);

        return ResponseEntity.ok().body(punchClockResponseDTO);
    }

    @Operation(summary = "故意失敗的打卡(使用Transactional)" ,description = "當天的第一次紀錄 clock_out 欄位的時間和第二次紀錄 clock_out 欄位的時間差小於50秒 系統會故意產生錯誤 打卡記錄不會儲存到資料庫")
    @PostMapping("/fail-clock-in-transactional")
    public ResponseEntity<PunchClockResponseDTO> failPunchClockTransactional(@RequestBody PunchClockDTO punchClockDTO) {

        PunchClockResponseDTO punchClockResponseDTO = punchClockService.failPunchClockTransactional(punchClockDTO);

        return ResponseEntity.ok().body(punchClockResponseDTO);
    }
}
