package com.baeker.member.base.security.oauth2.service;


import com.baeker.member.base.security.oauth2.model.converters.ProviderUserRequest;
import com.baeker.member.base.security.oauth2.model.providers.ProviderUser;
import com.baeker.member.base.security.oauth2.repository.UserRepository;
import com.baeker.member.base.security.oauth2.users.PrincipalUser;
import com.baeker.member.base.security.oauth2.users.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService extends AbstractOAuth2UserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);

        if(user == null){
            user = User.builder()
                    .id("1")
                    .username("onjsdnjs")
                    .password("{noop}1234")
                    .authorities(AuthorityUtils.createAuthorityList("ROLE_USER"))
                    .email("onjsdnjs@gmail.com")
                    .build();
        }

        ProviderUserRequest providerUserRequest = new ProviderUserRequest(user);
        ProviderUser providerUser = providerUser(providerUserRequest);

        selfCertificate(providerUser);

        return new PrincipalUser(providerUser);
    }
}