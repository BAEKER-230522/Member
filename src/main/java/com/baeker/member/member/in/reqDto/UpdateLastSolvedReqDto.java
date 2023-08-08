package com.baeker.member.member.in.reqDto;

import lombok.Data;

@Data
public class UpdateLastSolvedReqDto {

    private Long memberId;
    private int problemId;
}
