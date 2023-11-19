package com.baeker.member.base.security.oauth2.users.dto;

public record SocialLoginDto(String accessToken, String refreshToken, Long memberId, boolean isBaekJoonConnect) {
}
