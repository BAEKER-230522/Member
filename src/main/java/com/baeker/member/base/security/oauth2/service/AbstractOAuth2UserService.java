package com.baeker.member.base.security.oauth2.service;



import com.baeker.member.base.security.oauth2.certification.SelfCertification;
import com.baeker.member.base.security.oauth2.model.converters.ProviderUserConverter;
import com.baeker.member.base.security.oauth2.model.converters.ProviderUserRequest;
import com.baeker.member.base.security.oauth2.model.providers.ProviderUser;
import com.baeker.member.base.security.oauth2.repository.UserRepository;
import com.baeker.member.base.security.oauth2.users.User;
import com.baeker.member.member.domain.service.MemberService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.stereotype.Service;

@Service
@Getter
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

    public void selfCertificate(ProviderUser providerUser){
        certification.checkCertification(providerUser);
    }
    public void register(ProviderUser providerUser, OAuth2UserRequest userRequest){

        User user = userRepository.findByUsername(providerUser.getUsername());

        if(user == null){
            ClientRegistration clientRegistration = userRequest.getClientRegistration();
            userService.register(clientRegistration.getRegistrationId(),providerUser);
            providerUser.getEmail();
            providerUser.getPicture();
            memberService.whenSocialLogin(providerUser.getProvider(), providerUser.getUsername(),providerUser.getEmail());

        }else{
            System.out.println("userRequest = " + userRequest);
        }
    }
    public ProviderUser providerUser(ProviderUserRequest providerUserRequest){
        return providerUserConverter.convert(providerUserRequest);
    }
}