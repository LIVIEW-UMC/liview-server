package umc.liview.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter getCorsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("*");     // 모든 ip에 응답 허용
        config.addAllowedHeader("*");     // 모든 header 에 응답 허용
        config.addAllowedMethod("*");     // 모든 post,get,put,delete,patch 요청 허용
        config.setAllowCredentials(true); // 내 서버가 응답을 할 때 json을 자바스트립트에서 처리할 수 있도록

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
