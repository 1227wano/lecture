package com.kh.secom.member.model.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.kh.secom.auth.util.JwtUtil;
import com.kh.secom.token.model.dto.RefreshTokenDTO;
import com.kh.secom.token.model.mapper.TokenMapper;
import com.kh.secom.token.model.service.TokenService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

	private final JwtUtil tokenUtil;
	private final TokenMapper tokenMapper;

	@Override
	public Map<String, String> generateToken(String username, Long userNo) {

		// 1. AccessToken 만들기
		// 2. RefreshToken 만들기
		Map<String, String> tokens = createTokens(username);
		// 3. RefreshToken DB에 저장하기
		saveToken(tokens.get("refreshToken"), userNo);
		// 4. 만료기간이 지나 RefreshToken이 있다면 삭제하기

		// 5. 사용자가 RefreshToken을 증명하려 할때 DB가서 조회해오기
		// 위 5개는 무적권
		return tokens;
	}

	// 1, 2번
	private Map<String, String> createTokens(String username) {
		String accessToken = tokenUtil.getAccessToken(username);
		String refreshToken = tokenUtil.getRefreshToken(username);

		Map<String, String> tokens = new HashMap();
		tokens.put("accessToken", accessToken);
		tokens.put("refreshToken", refreshToken);

		return tokens;
	}
	
	// 3번
	private void saveToken(String refreshToken, Long userNo) {
		
		RefreshTokenDTO token = RefreshTokenDTO.builder()
											  .token(refreshToken)
											  .userNo(userNo)
											  .expiration(System.currentTimeMillis() + 3600000 * 72)
											  .build();
		tokenMapper.saveToken(token);
	}
	
	
	
	
	
	
	
	
	
	
	
	

}
