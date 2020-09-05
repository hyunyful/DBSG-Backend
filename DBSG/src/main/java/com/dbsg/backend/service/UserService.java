package com.dbsg.backend.service;

import java.util.Map;

import javax.servlet.http.HttpSession;

import com.dbsg.backend.domain.User;
import com.dbsg.backend.domain.UserDisplay;

public interface UserService {

	//카카오 로그인
	public Map<String,Object> kakaoLogin(String code);
	
	//닉네임 중복검사
	public Map<String,Object> nicknameCheck(Map<String,Object> param);
	
	//닉네임 설정
	public Map<String,Object> setNickname(Map<String,Object> param);
	
	//회원번호로 회원정보 찾아서 리턴
	public UserDisplay findInfoByNo(int user_no);
	
	//네이버 로그인 상태 토큰 랜덤 생성
	public Map<String,Object> naverState();
	
	//네이버 로그인 접근 토큰 생성
	public Map<String,Object> naverLogin(String state, String code);
	
}
