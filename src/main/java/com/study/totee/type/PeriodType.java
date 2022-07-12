package com.study.totee.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum PeriodType {
    VeryShortTerm("1개월 미만"),
    ShortTerm("1~3개월"),
    MidTerm("3~6개월"),
    LongTerm("6개월 이상");

    private final String period;

    public static PeriodType of(String period) {
        return Arrays.stream(PeriodType.values())
                .filter(r -> r.getPeriod().equals(period))
                .findAny()
                .orElse(ShortTerm);
    }
}
