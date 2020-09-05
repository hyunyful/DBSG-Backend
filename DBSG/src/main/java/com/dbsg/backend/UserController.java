package com.dbsg.backend;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dbsg.backend.domain.User;
import com.dbsg.backend.domain.UserDisplay;
import com.dbsg.backend.service.UserService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	String myState = "";		//네이버 로그인할 때 상태토큰

	//카카오 로그인
	@ApiOperation(value="카카오 로그인")
	@GetMapping("/login/kakao")
	public Map<String,Object> kakaoLogin(@RequestParam("code") String code){
		Map<String,Object> map = new HashMap<>();				//결과로 리턴될 map
		
		//접근 토큰 받아오기
		map = userService.kakaoLogin(code);
		
		map.put("con", "success");
		
		return map;
	}
	
	@ApiOperation(value="네이버 상태토큰 받기")
	@GetMapping("/naverState")
	public Map<String,Object> naverLoginState(){
		Map<String,Object> map = new HashMap<>();
		
		map = userService.naverState();
		
		myState = (String) map.get("state");
		
		map.put("con", "success");
		
		return map;
	}
	
	@ApiOperation(value="네이버 로그인")
	@GetMapping("/login/naver")
	public Map<String,Object> naverLogin(@RequestParam("state") String state, @RequestParam("code") String code){
		Map<String,Object> map = new HashMap<>();
		
		//상태토큰을 먼저 검증하고 접근토큰 생성
		if(myState.equals(state)) {		//일치하면
			//접근 토큰 생성
			map = userService.naverLogin(state, code);
		}else {										//일치하지 않으면
			map.put("naverLogin", "fail");
			map.put("error", "state Error");
		}
		
		map.put("con", "success");
		
		return map;
	}
	
	//닉네임 중복검사
	@ApiOperation(value="닉네임 중복검사")
	@PostMapping("/nicknameCheck")
	public Map<String,Object> nicknameCheck(@RequestBody Map<String,Object> param){
		Map<String,Object> map = new HashMap<>();
		
		map = userService.nicknameCheck(param);
		
		map.put("con", "success");
		map.put("sendData", param);
		
		return map;
	}
	
	//닉네임 설정 요청
	@ApiOperation(value="닉네임 설정")
	@PostMapping("/nickname")
	public Map<String,Object> setNickname(@RequestBody Map<String,Object> param){
		Map<String,Object> map = new HashMap<>();
		
		map = userService.setNickname(param);
		
		map.put("con", "success");
		map.put("sendData", param);
		
		return map;
	}
}
