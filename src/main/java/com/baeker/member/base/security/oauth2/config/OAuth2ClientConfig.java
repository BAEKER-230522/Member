//package com.baeker.member.base.security.oauth2.config;//package com.example.gateway.oauth2.config;
//
//
//import com.example.gateway.oauth2.service.CustomOAuth2UserService;
//import com.example.gateway.oauth2.service.CustomOidcUserService;
//import com.example.gateway.oauth2.service.CustomUserDetailsService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.authorization.AuthenticatedAuthorizationManager;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
//
//@EnableWebSecurity
//@RequiredArgsConstructor
//public class OAuth2ClientConfig {
//
//    @Value("${custom.front}")
//    private final String FRONT_URL;
//
//    private final CustomOAuth2UserService customOAuth2UserService;
//    private final CustomOidcUserService customOidcUserService;
//    private final CustomUserDetailsService customUserDetailsService;
//
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.ignoring().requestMatchers("/static/js/**", "/static/images/**", "/static/css/**","/static/scss/**");
//    }
//
//    @Bean
//    SecurityFilterChain oauth2SecurityFilterChain(HttpSecurity http) throws Exception {
//        return http.csrf().disable()
//                .oauth2Client().and()
//                        .authorizeHttpRequests((requests) -> requests
////                    .requestMatchers("/api/user")
////                    .access(AuthenticatedAuthorizationManager.rememberMe())
////                    .requestMatchers("/api/oidc")
////                    .access(AuthenticatedAuthorizationManager.rememberMe())
//                    .requestMatchers("/")
////                                                    .permitAll()
//                                        .authenticated()
////                    .anyRequest().authenticated())
//                )
//                        .oauth2Login(
//                                oauth2 -> oauth2.userInfoEndpoint(
//                                    userInfoEndpointConfig -> userInfoEndpointConfig
//                                                            .userService(customOAuth2UserService)  // OAuth2
//                                                            .oidcUserService(customOidcUserService)))  // OpenID Connect
//                                                            .userDetailsService(customUserDetailsService) // Form
//                        .exceptionHandling(exception -> exception.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login")))
//                        .logout(logout -> logout
//                                            .deleteCookies("remove")
//                                            .logoutSuccessUrl(FRONT_URL)
//                                            .invalidateHttpSession(false)
//                                            .logoutUrl("/logoutUrl"))  //TODO: LogoutURL 입력
//                .build();
//    }
//
//}