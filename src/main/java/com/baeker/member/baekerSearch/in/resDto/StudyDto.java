package com.baeker.member.baekerSearch.in.resDto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StudyDto {
    private Long id;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
    private String name;
    private String about;
    private Long leader;
    private Integer capacity;
    private Integer studyMember;
    private Integer xp;
    int bronze;
    int silver;
    int gold;
    int diamond;
    int ruby;
    int platinum;
    int solvedCount;
}
