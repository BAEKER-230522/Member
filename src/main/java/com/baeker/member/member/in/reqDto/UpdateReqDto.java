package com.baeker.member.member.in.reqDto;

import lombok.Data;

@Data
public class UpdateReqDto {
    private Long id;
    private String nickname;
    private String about;
}
