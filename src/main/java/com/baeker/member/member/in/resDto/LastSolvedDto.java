package com.baeker.member.member.in.resDto;

import com.baeker.member.member.domain.entity.Member;
import lombok.Data;

@Data
public class LastSolvedDto {
    private Long id;
    private int problemId;

    public LastSolvedDto(Member member) {
        this.id = member.getId();
        this.problemId = member.getLastSolvedProblemId();
    }
}
