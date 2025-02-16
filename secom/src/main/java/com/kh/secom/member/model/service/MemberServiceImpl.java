package com.kh.secom.member.model.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kh.secom.auth.model.vo.CustomUserDetails;
import com.kh.secom.exception.DuplicateUserException;
import com.kh.secom.exception.InvalidParameterException;
import com.kh.secom.exception.MismatchPasswordException;
import com.kh.secom.member.model.mapper.MemberMapper;
import com.kh.secom.member.model.vo.ChangePasswordDTO;
import com.kh.secom.member.model.vo.Member;
import com.kh.secom.member.model.vo.MemberDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

	private final MemberMapper memberMapper;
	private final PasswordEncoder passwordEncoder;

	@Override
	public void save(MemberDTO requestMember) { // 일반 사용자용 가입 메소드

		// 빈 문자열인지 유효성 검사
		if ("".equals(requestMember.getUserId()) || "".equals(requestMember.getUserPwd())) {
			throw new InvalidParameterException("유효하지 않은 값입니다.");
		}

		// DB에 이미 사용자가 입력한 사용자가 존재해서는 안됨
		Member searched = memberMapper.findByUserId(requestMember.getUserId());
		if (searched != null) {
			throw new DuplicateUserException("이미 존재하는 아이디입니다.");
		}

		// 비밀번호 평문이라 그냥 들어가면 안됨
		// + ROLE == USER라고 저장할 예정

		Member member = Member.builder().userId(requestMember.getUserId())
				.userPwd(passwordEncoder.encode(requestMember.getUserPwd())).role("ROLE_USER") // ROLE_을 넣어야함
				.build();

		memberMapper.save(member);
		log.info("회원가입 성공!");
	}

	@Override
	public void changePassword(ChangePasswordDTO changeEntity) {
		
		// 비밀번호 변경
		// 제 현재 비밀번호는 currentPassword이고
		// 만약 맞다면 newPassword로 바꾸고 싶어요.
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails user =(CustomUserDetails)auth.getPrincipal();
		
		if(!(passwordEncoder.matches(changeEntity.getCurrentPassword(), 
									 user.getPassword()))) {
			throw new MismatchPasswordException("비밀번호 오류");
		}
		
		String encodePassword = passwordEncoder.encode(changeEntity.getNewPassword());
		
		Map<String, String> changeRequest = new HashMap();
		changeRequest.put("userNo", String.valueOf(user.getUserNo()));
		changeRequest.put("password", encodePassword);
		
		memberMapper.changePassword(changeRequest);
	}

}
