package com.baeker.member.member.in.resDto;

import lombok.Data;

import java.util.List;

@Data
public class SearchResDto {

    private List<MemberDto> members;
    private List<StudyDto> studies;
}
