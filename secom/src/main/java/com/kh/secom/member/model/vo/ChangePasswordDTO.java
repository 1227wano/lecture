package com.kh.secom.member.model.vo;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChangePasswordDTO {
	@NotBlank(message="현재 비밀번호를 꼭 입력해주세요")
	private String currentPassword;
	@NotBlank(message="새 비밀번호를 꼭 입력해주세요")
	private String newPassword;
}
