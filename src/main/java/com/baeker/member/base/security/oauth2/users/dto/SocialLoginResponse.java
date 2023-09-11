package com.baeker.member.base.security.oauth2.users.dto;

public record SocialLoginResponse(String accessToken, String refreshToken, Long memberId, boolean isBaekJoonConnect) {
}
