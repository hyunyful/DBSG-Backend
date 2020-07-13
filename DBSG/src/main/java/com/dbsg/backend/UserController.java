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

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	String myState = "";		//네이버 로그인할 때 상태토큰

	//카카오 로그인 redirect_uri
	//인증 코드 받기
	//@GetMapping("/user/login/kakao")
	@GetMapping("/login/kakao")
	public Map<String,Object> kakaoLogin(@RequestParam("code") String code){
		Map<String,Object> map = new HashMap<>();				//결과로 리턴될 map
		
		map = userService.kakaoLogin(code);
		
		map.put("con", "success");
		
		return map;
	}
	
	@GetMapping("/naverState")
	public Map<String,Object> naverLoginState(){
		Map<String,Object> map = new HashMap<>();
		
		map = userService.naverState();
		
		myState = (String) map.get("state");
		
		map.put("con", "success");
		
		return map;
	}
	
	//@GetMapping("/user/login/naver")
	@GetMapping("/login/naver")
	public Map<String,Object> naverLogin(@RequestParam("state") String state, @RequestParam("code") String code){
		Map<String,Object> map = new HashMap<>();
		
		//System.out.println("myState는 "+myState);
		//System.out.println("state는 "+state);
		//System.out.println("code는 "+code);
		
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
	//@PostMapping("/user/nicknameCheck")
	@PostMapping("/nicknameCheck")
	public Map<String,Object> nicknameCheck(@RequestBody Map<String,Object> param){
		Map<String,Object> map = new HashMap<>();
		
		map = userService.nicknameCheck(param);
		
		map.put("con", "success");
		map.put("sendData", param);
		
		return map;
	}
	
	//닉네임 설정 요청
	//@PostMapping("/user/nickname")
	@PostMapping("/nickname")
	public Map<String,Object> setNickname(@RequestBody Map<String,Object> param){
		Map<String,Object> map = new HashMap<>();
		map.put("con", "success");
		
		Map<String,Object> user_info = userService.setNickname(param);
		
		String sendNickname = (String) user_info.get("sendNickname");
		int sendUser_no = (int) user_info.get("sendUser_no");
		String result = (String) user_info.get("result");
		
		map.put("sendNickname", sendNickname);
		map.put("sendUser_no", sendUser_no);
		
		//닉네임 설정에 성공한 경우
		if(result == "success") {
			//성공했다고 전송
			map.put("data", result);
			//map 안에 있는 회원정보 전송
			UserDisplay info = (UserDisplay) user_info.get("ud");
			map.put("user_info", info);
		}
		//닉네임 설정에 실패한 경우
		else if(result == "fail") {
			map.put("data", result);
		}
		
		return map;
	}
}
