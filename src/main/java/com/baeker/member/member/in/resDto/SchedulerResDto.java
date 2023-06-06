package com.baeker.member.member.in.resDto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class SchedulerResDto {

    private Long id;
    private String baekJoonName;
    private int bronze;
    private int sliver;
    private int gold;
    private int diamond;
    private int ruby;
    private int platinum;
    private int solvedCount;

    //-- get solved count --//
    private int getSolved() {
        return bronze + sliver + gold + diamond + ruby + platinum;
    }

    //-- mapping query dsl --//
    @QueryProjection
    public SchedulerResDto(Long id, String baekJoonName, int bronze, int sliver, int gold, int diamond, int ruby, int platinum) {
        this.id = id;
        this.baekJoonName = baekJoonName;
        this.bronze = bronze;
        this.sliver = sliver;
        this.gold = gold;
        this.diamond = diamond;
        this.ruby = ruby;
        this.platinum = platinum;
        this.solvedCount = getSolved();
    }
}
