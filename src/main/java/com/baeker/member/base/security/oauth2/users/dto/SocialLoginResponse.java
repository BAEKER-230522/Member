package com.baeker.member.base.security.oauth2.users.dto;

public record SocialLoginResponse(String accessToken, Long memberId, boolean isBaekJoonConnect) {
}
