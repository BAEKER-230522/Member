package com.baeker.member.member.in.reqDto;

import lombok.Data;

@Data
public class SolvedCountReqDto {
    private Long id;
    private int bronze;
    private int silver;
    private int gold;
    private int diamond;
    private int ruby;
    private int platinum;
    private int add;
}
