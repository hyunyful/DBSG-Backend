package com.dbsg.backend.service;

import java.util.Map;

import com.dbsg.backend.domain.User;
import com.dbsg.backend.domain.UserDisplay;

public interface UserService {

	//카카오 로그인 접근 토큰 받기
	public Map<String,Object> getAccessToken(String code);
	
	//카카오 로그인 사용자 정보 요청
	public Map<String,Object> getUserInfo(String accessToken);
	
	//해당 정보의 회원이 있는지 확인
	public Map<String,Object> join(User user);
	
	//닉네임 중복검사
	public Map<String,Object> nicknameCheck(Map<String,Object> param);
	
	//닉네임 설정
	public Map<String,Object> setNickname(Map<String,Object> param);
	
	//회원번호로 회원정보 찾아서 리턴
	public UserDisplay findInfoByNo(int user_no);
}
