package com.kh.secom.member.model.service;

import com.kh.secom.member.model.vo.ChangePasswordDTO;
import com.kh.secom.member.model.vo.MemberDTO;

import jakarta.validation.Valid;

public interface MemberService {

	void save(MemberDTO requestMember);

	void changePassword(@Valid ChangePasswordDTO changeEntity);

}
