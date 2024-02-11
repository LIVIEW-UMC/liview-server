package umc.liview.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import umc.liview.auth.application.UserCommandService;
import umc.liview.auth.application.UserQueryService;
import umc.liview.config.jwt.JwtAuthFilter;
import umc.liview.config.jwt.factory.JwtFactory;
import umc.liview.config.jwt.handler.JwtAccessDeniedHandler;
import umc.liview.config.jwt.handler.JwtAuthenticationEntryPoint;
import umc.liview.config.jwt.utils.JwtExtractor;
import umc.liview.config.jwt.utils.JwtVerifier;
import umc.liview.config.oauth2.OAuthFailureHandler;
import umc.liview.config.oauth2.OAuthLoader;
import umc.liview.config.oauth2.OAuthSuccessHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final CorsConfig corsConfig;
    private final OAuthLoader oAuthLoader;
    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;
    private final JwtExtractor jwtExtractor;
    private final JwtVerifier jwtVerifier;
    private final JwtFactory jwtFactory;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final JwtAccessDeniedHandler accessDeniedHandler;

    private static final String[] AUTH_WHITELIST = {"/swagger-resources/**", "/swagger-ui/**", "/v3/api-docs/**", "/login", "/oauth2/**", "/", "/favicon.ico" };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // CSRF, CORS
        http.addFilter(corsConfig.getCorsFilter());
        http.csrf(AbstractHttpConfigurer::disable);

        // STATELESS 설정
        http.sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // FormLogin, BasicHttp 비활성화
        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);

        // 권한 규칙 설정
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(AUTH_WHITELIST).permitAll()
                .anyRequest().authenticated());

        // JwtAuthFilter 추가
        http.addFilterBefore(new JwtAuthFilter(jwtExtractor, jwtVerifier), UsernamePasswordAuthenticationFilter.class);
        http.exceptionHandling((exceptionHandling) -> exceptionHandling
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
        );

        // oauth
        http.oauth2Login(oauth2 -> oauth2
                .successHandler(new OAuthSuccessHandler(jwtFactory, userQueryService, userCommandService))
                .failureHandler(new OAuthFailureHandler())
                .userInfoEndpoint(userInfo -> userInfo.userService(oAuthLoader)));

        return http.build();
    }
}
