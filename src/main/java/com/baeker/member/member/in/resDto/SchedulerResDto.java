package com.baeker.member.member.in.resDto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class SchedulerResDto {

    private Long id;
    private String baekJoonName;
    private int bronze;
    private int silver;
    private int gold;
    private int diamond;
    private int ruby;
    private int platinum;
    private int solvedCount;

    //-- get solved count --//
    private int getSolved() {
        return bronze + silver + gold + diamond + ruby + platinum;
    }

    //-- mapping query dsl --//
    @QueryProjection
    public SchedulerResDto(Long id, String baekJoonName, int bronze, int silver, int gold, int diamond, int ruby, int platinum) {
        this.id = id;
        this.baekJoonName = baekJoonName;
        this.bronze = bronze;
        this.silver = silver;
        this.gold = gold;
        this.diamond = diamond;
        this.ruby = ruby;
        this.platinum = platinum;
        this.solvedCount = getSolved();
    }
}
