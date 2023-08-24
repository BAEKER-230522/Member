package com.baeker.member.baekerSearch.in.resDto;

import com.baeker.member.baekerSearch.in.resDto.StudyDto;
import com.baeker.member.member.in.resDto.MemberDto;
import lombok.Data;

import java.util.List;

@Data
public class SearchResDto {

    private List<MemberDto> members;
    private List<StudyDto> studies;
}
