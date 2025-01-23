package com.kh.secom.auth.util;

import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtUtil {
	
	// 애플리케이션 설정파일 (application.properties / application.yml)에 정의된
	// 속성의 값들을 @Value에노테이션을 이용해서 값을 주입받을 수 있음
	@Value("${jwt.secret}")
	private String secretKey;
	// javax.crypto.SecretKey타입의 필드로 JWT서명에 사용할 수 있음
	private SecretKey key;
	
	// 유효기간이 필요
	private long ACCESS_TOKEN_EXPIRED = 3600000L * 24; // 1일
	private long REFRESH_TOKEN_EXPIRED = 3600000L * 72; // 3일
	
	@PostConstruct // 빈 초기화 시 필요한 추가 설정들을 할 수 있음
	public void init() { // 초기화용 메소드
		byte[] keyArr = Base64.getDecoder().decode(secretKey);
		this.key = Keys.hmacShaKeyFor(keyArr);
		
	}
	
	public String getAccessToken(String username) {
		
		return Jwts.builder()
					.subject(username)	// 사용자 이름
					.issuedAt(new Date())	// 발급일
					.expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRED)) // 만료일
					.signWith(key)	// 비밀키로 만든 서명 
					.compact();
	}
	
	
	
	
	/* 토큰생성과정
	public void abc() {
		Jwts.builder()
			.subject("어쩌고")
			.issuedAt(new Date())
			.expiration(new Date() + 35435434)
			.build();
	}
	public void vvv() {
		Jwts.parser()
			.verifyWith(null)
	}
	*/

}
