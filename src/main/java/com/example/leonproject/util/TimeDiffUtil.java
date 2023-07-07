package com.example.leonproject.util;

import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class TimeDiffUtil {

/**
 *此方法是來計算每個user當日上下班打卡的時間差
   */
public static LocalDateTime calculateTimeDiff(LocalDateTime clockInTime , LocalDateTime clockOutTime ) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        Duration duration = Duration.between(clockOutTime, clockInTime);
        LocalDateTime resultDateTime = LocalDateTime.of(0,1,1,0,0,0).plus(duration);

        return LocalDateTime.parse(resultDateTime.format(formatter));
    }
    /**
     *此方法是用來找出使用者當日是否有打過卡，回傳True則表示使用者當日已經打過卡
     */
    public static boolean isSameDate(LocalDateTime targetTime) {

        LocalDate currentDate = LocalDateTime.now().toLocalDate();
        LocalDate targetDate = targetTime.toLocalDate();

        return currentDate.equals(targetDate);
    }
}
