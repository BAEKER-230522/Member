package com.baeker.member.global.security.oauth2.model.converters;



import com.baeker.member.global.security.oauth2.model.enums.OAuth2Config;
import com.baeker.member.global.security.oauth2.model.providers.ProviderUser;
import com.baeker.member.global.security.oauth2.users.KakaoOidcUser;
import com.baeker.member.global.security.oauth2.utils.OAuth2Utils;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

public final class OAuth2KakaoOidcProviderUserConverter implements ProviderUserConverter<ProviderUserRequest, ProviderUser> {
    @Override
    public ProviderUser convert(ProviderUserRequest providerUserRequest) {

        if (!providerUserRequest.clientRegistration().getRegistrationId().equals(OAuth2Config.SocialType.KAKAO.getSocialName())) {
            return null;
        }

        if (!(providerUserRequest.oAuth2User() instanceof OidcUser)) {
            return null;
        }

        return new KakaoOidcUser(OAuth2Utils.getMainAttributes(
                providerUserRequest.oAuth2User()),
                providerUserRequest.oAuth2User(),
                providerUserRequest.clientRegistration());
    }
}