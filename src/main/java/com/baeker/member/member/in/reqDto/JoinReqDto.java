package com.baeker.member.member.in.reqDto;

import lombok.Data;

@Data
public class JoinReqDto {

    private String username;
    private String nickName;
    private String password;
    private String provider;
    private String email;
    private String token;
    private String profileImage;

    public static JoinReqDto createJoinDto(String username, String nickName, String password, String provider, String email, String token, String profileImage) {
        JoinReqDto dto = new JoinReqDto();
        dto.username = username;
        dto.nickName = nickName;
        dto.password = password;
        dto.provider = provider;
        dto.email = email;
        dto.token = token;
        dto.profileImage = profileImage;
        return dto;
    }
}
