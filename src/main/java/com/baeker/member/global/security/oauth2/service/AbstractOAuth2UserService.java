package com.baeker.member.global.security.oauth2.service;



import com.baeker.member.global.security.jwt.JwtTokenProvider;
import com.baeker.member.global.security.oauth2.certification.SelfCertification;
import com.baeker.member.global.security.oauth2.model.converters.ProviderUserConverter;
import com.baeker.member.global.security.oauth2.model.converters.ProviderUserRequest;
import com.baeker.member.global.security.oauth2.model.providers.ProviderUser;
import com.baeker.member.global.security.oauth2.repository.UserRepository;
import com.baeker.member.global.security.oauth2.users.User;
import com.baeker.member.member.domain.entity.Member;
import com.baeker.member.member.in.resDto.JwtTokenResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Getter
@Slf4j
public abstract class AbstractOAuth2UserService {


    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SelfCertification certification;
    @Autowired
    private MemberService memberService;
    @Autowired
    private ProviderUserConverter<ProviderUserRequest, ProviderUser> providerUserConverter;

    @Autowired
    private JwtTokenProvider tokenProvider;
    public void selfCertificate(ProviderUser providerUser){
        certification.checkCertification(providerUser);
    }
    public void register(ProviderUser providerUser, OAuth2UserRequest userRequest){

        User user = userRepository.findByUsername(providerUser.getUsername());

        if(user == null){
            ClientRegistration clientRegistration = userRequest.getClientRegistration();
            userService.register(clientRegistration.getRegistrationId(),providerUser);
            String profileImg = providerUser.getAttributes().getOrDefault("picture", "").toString();
            String provider = providerUser.getProvider();
            String email = providerUser.getEmail();
            String username = providerUser.getUsername();

            Member member = memberService.whenSocialLogin(provider, username, email, profileImg);
//            Map<String, String> token = tokenProvider.genAccessTokenAndRefreshToken(member);
        }else{
            memberService.whenSocialLogin(user.getProvider(), user.getUsername(), user.getEmail(), user.getPicture());
        }
    }
    public ProviderUser providerUser(ProviderUserRequest providerUserRequest){
        return providerUserConverter.convert(providerUserRequest);
    }

    private JwtTokenResponse getTokenDto(Map<String, String> token) {
        return new JwtTokenResponse(token.get("accessToken"), token.get("refreshToken"));
    }
}