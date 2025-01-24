package com.kh.secom.member.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.secom.auth.service.AuthenticationService;
import com.kh.secom.member.model.service.MemberService;
import com.kh.secom.member.model.vo.ChangePasswordDTO;
import com.kh.secom.member.model.vo.LoginResponse;
import com.kh.secom.member.model.vo.MemberDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value= "members", produces="application/json; charset=UTF-8")
public class MemberController {

	private final MemberService memberService;
	private final AuthenticationService authService;

	// 새롭게 데이터를 만들어내는 요청 == POST
	@PostMapping
	public ResponseEntity<?> save(@Valid @RequestBody MemberDTO requestMember) {

		// log.info("요청한 사용자의 데이터 : {}", requestMember);
		memberService.save(requestMember);

		return ResponseEntity.ok("회원가입에 성공했습니다");
	}

	// 로그인!
	@PostMapping("login")
	public ResponseEntity<LoginResponse> login(@Valid @RequestBody MemberDTO requestMember) {

		/*
		 * 로그인 구현 로그인에 성공했다는 것은 -> 인증(Authentication) => 본래 개발자가 함 아이디 / 비밀번호(평문) 아이디 /
		 * 비밀번호(암호문)
		 */

		// postman의 body에 담은(@RequestBody) requestMember에 담아

		Map<String, String> tokens = authService.login(requestMember);

		/*
		 * 로그인에 성공했을때 AccessToken RefreshToken 반환
		 */

		LoginResponse response = LoginResponse.builder().username(requestMember.getUserId()).tokens(tokens).build();
		
		// log.info("{}", response);
		return ResponseEntity.ok(response);
	}
	
	// 비밀번호 변경 기능 구역
	// 기존 비번 / 바꾸고 싶은 비밀번호
	@PutMapping
	public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordDTO changeEntity){
		// log.info("{}", changeEntity);
		memberService.changePassword(changeEntity);
		return ResponseEntity.ok("업데이트에 성공");
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
