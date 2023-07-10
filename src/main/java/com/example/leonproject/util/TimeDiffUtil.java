package com.example.leonproject.util;

import org.springframework.stereotype.Component;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
public class TimeDiffUtil {

    /**
     * 此方法是來計算每個user當日上下班打卡的時間差
     */
    public static Time calculateTimeDiff(LocalDateTime clockInTime, LocalDateTime clockOutTime) {

        Duration duration = Duration.between(clockInTime, clockOutTime);
        long hours = duration.toHours();
        int minutes = duration.toMinutesPart();
        int seconds = duration.toSecondsPart();

        return Time.valueOf(LocalTime.of((int) hours,minutes,seconds));
    }

    /**
     * 此方法是用來找出使用者當日是否有打過卡，回傳True則表示使用者當日已經打過卡
     */
    public static boolean isSameDate(LocalDateTime targetTime) {

        LocalDate currentDate = LocalDateTime.now().toLocalDate();
        LocalDate targetDate = targetTime.toLocalDate();

        return currentDate.equals(targetDate);
    }
}
