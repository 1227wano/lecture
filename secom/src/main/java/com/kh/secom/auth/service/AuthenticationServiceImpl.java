package com.kh.secom.auth.service;

import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.kh.secom.auth.model.vo.CustomUserDetails;
import com.kh.secom.member.model.vo.MemberDTO;
import com.kh.secom.token.model.service.TokenService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

	private final AuthenticationManager authenticationManager;
	// private final JwtUtil jwt;
	private final TokenService tokenService;

	@Override
	public Map<String, String> login(MemberDTO requestMember) {

		// 넘어온 requestMember의 id와 pass를 토큰(UsernamePasswordAuthenticationToken)에
		// 뽑아담아서 authenticationManager의 authenticate메서드로
		// 인증한다
		// id,pass가 조회되면 authentication형이 반환되어 오고, 없으면 false가 반환되어옴
		// (조회되어 넘어온 UserDetail이 Authentication의 principal에 담겨옴)
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(requestMember.getUserId(), requestMember.getUserPwd()));
		/*
		 * UsernamePasswordAuthenticationToken : 사용자가 입력한 username과 password를 검증하는 용도로
		 * 사용하는 클래스 주로, SpringSecurity에서 인증을 시도할 때 사용함
		 */

		CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

		log.info("로그인절차 성공");
		log.info("DB에서 조회된 사용자의 정보 : {}", user); // 여기까지가 시큐리티의 역할

		// 인증에 성공한 다음은 JWT토큰의 영역
		// 여기로 넘어왔으면 토큰을 만들게된다. (라이브러리 추가해서 (build.gradle))
		// 이때 비밀키도 만들어야 하는데,
		/*
		String accessToken = jwt.getAccessToken(user.getUsername());
		String refreshToken = jwt.getRefreshToken(user.getUsername());
		log.info("발행된 액세스토큰 : {}", accessToken);
		log.info("발행된 리프레쉬토큰 : {}", refreshToken);
		토큰 패키지 만들어서 따로 만들기!
		*/ 
		
		Map<String, String> tokens = tokenService.generateToken(user.getUsername(),user.getUserNo());
		
		return tokens;
	}

}
