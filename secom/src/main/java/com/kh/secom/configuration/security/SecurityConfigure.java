package com.kh.secom.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfigure {

	@Bean // Bean에노테이션을 이용해서 빈을 등록하는 경우 동일한 이름의 메소드가 존재해서는 안됨!
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		
		/* 
		return httpSecurity
				.formLogin().disable()
				.build();
		*/
		/*
		return httpSecurity
				.formLogin(new Customizer<FormLoginConfigurer<HttpSecurity>>() {
					@Override
					public void customize(FormLoginConfigurer<HttpSecurity> formLogin) {
						formLogin.disable();
					}
				}).httpBasic(null).csrf(null).cors(null).build();
		*/
		return httpSecurity
				.formLogin(AbstractHttpConfigurer::disable)	// form로그인방식은 사용하지 않겠다.
				.httpBasic(AbstractHttpConfigurer::disable)	// httpBasic 사용하지 않겠다.
				.csrf(AbstractHttpConfigurer::disable)		// csrf 비활성화
				.cors(AbstractHttpConfigurer::disable)		// 얘는 일단 꺼넣고 나중에 ngnix붙이기
				.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
		return authenticationConfiguration.getAuthenticationManager();
	}
	
}
