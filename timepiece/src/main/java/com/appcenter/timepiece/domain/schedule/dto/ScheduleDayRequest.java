package com.appcenter.timepiece.domain.schedule.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

// todo: 중첩클래스로 작성
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ScheduleDayRequest {

    @NotEmpty(message = "스케줄은 적어도 하나 이상의 값을 넣어야 저장할 수 있습니다: Null")
    @Valid
    private List<ScheduleDto> schedule;

    public ScheduleDayRequest(List<ScheduleDto> schedule) {
        this.schedule = schedule;
    }
}