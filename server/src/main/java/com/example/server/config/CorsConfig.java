package com.example.server.config;


import com.example.server.config.jwt.JwtProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
       	config.addAllowedOrigin("http://ec2-52-9-214-153.us-west-1.compute.amazonaws.com");
	config.addAllowedOrigin("http://52.9.214.153"); 
	config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.addExposedHeader(JwtProperties.HEADER_STRING); // 노출시킬 헤더 -> 응답 시에 프론트에서 받았을 때 노출될 헤더를 설정
        config.addExposedHeader(JwtProperties.REFRESH_HEADER_STRING); // 노출시킬 헤더 -> 응답 시에 프론트에서 받았을 때 노출될 헤더를 설정
        source.registerCorsConfiguration("/**",config);
        return new CorsFilter(source);
    }
}
