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
public class UserController {
	
	@Autowired
	private UserService userService;
	
	String myState = "";		//네이버 로그인할 때 상태토큰

	//카카오 로그인 redirect_uri
	//인증 코드 받기
	@GetMapping("/user/login/kakao")
	public Map<String,Object> kakaoLogin(@RequestParam("code") String code){
		Map<String,Object> map = new HashMap<>();				//결과로 리턴될 map
		Map<String,Object> tokenMap = new HashMap<>();		//접근 토큰을 얻어오는데 사용되는 map
		Map<String,Object> infoMap = new HashMap<>();			//사용자 정보를 얻어오는데 사용되는 map
		int resultCode;			//모든 resultCode를 담을 하나의 변수
		
		map.put("con", "success");
		
		//System.out.println("접근 성공~ code는 "+code);
		tokenMap = userService.getAccessToken(code);
		
		String tokenError = (String) tokenMap.get("error");
		
		//접근 토큰을 얻는 과정에서 에러가 없었으면
		if(tokenError == null) {
			String accessToken = (String) tokenMap.get("accessToken");
			
			//사용자 정보 요청하기
			 infoMap = userService.getUserInfo(accessToken);
			
			String infoError = (String) infoMap.get("error");
			
			//사용자 정보 요청에서 에러가 났으면 
			if(infoError != null) {
				resultCode = (int) infoMap.get("resultCode");
				
				//error 내용과 resultCode를 담아서 리턴
				map.put("resultCode", resultCode);
				map.put("error", infoError);
				return map;
			}else {				//사용자 정보 요청에서 에러가 안났으면
				//infoMap에서 user 정보를 담은 user 객체 추출
				User user = (User) infoMap.get("user");
				
				//회원정보 작업
				Map<String,Object> userMap = userService.join(user);
				
				//result에 회원정보 작업의 결과가 담겨있음
				String result = (String) userMap.get("result");
				
				//result가 error면
				if("error".equals(result)) {
					map.put("data", "error");
					map.put("error", result);
					
					return map;
				}
				//result가 needNickname이면
				else if("needNickname".equals(result)) {
					//user_no 랑 같이 리턴
					//해당 유저 번호 다시 보내달라고..
					int no = (int) userMap.get("user_no");
				
					//System.out.println("controller : "+no);
					
					map.put("data", result);
					map.put("user_no", no);
					
					return map;
				}
				//해당 회원의 정보가 있으면
				else if("exist".equals(result)){
					
					UserDisplay ud = (UserDisplay) userMap.get("ud");
					int user_no = ud.getUser_no();
					
					//user_no로 회원정보 찾아서 리턴
					UserDisplay userInfo = userService.findInfoByNo(user_no);
					
					map.put("user_info", userInfo);
					map.put("data", result);
				}
			}
		}
		//접근 토큰 얻는 과정에서 에러가 났으면
		else {
			resultCode = (int) tokenMap.get("resultCode");
			
			map.put("resultCode", resultCode);
			map.put("error", tokenError);
			return map;
		}
		
		//map.put("result", result);
		
		return map;
	}
	
	@GetMapping("/naver/state")
	public Map<String,Object> naverLoginState(){
		Map<String,Object> map = new HashMap<>();
		
		map = userService.naverState();
		
		myState = (String) map.get("state");
		
		map.put("con", "success");
		
		return map;
	}
	
	@GetMapping("/user/login/naver")
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
	@PostMapping("/user/nicknameCheck")
	public Map<String,Object> nicknameCheck(@RequestBody Map<String,Object> param){
		Map<String,Object> map = new HashMap<>();
		
		map = userService.nicknameCheck(param);
		
		map.put("con", "success");
		map.put("sendData", param);
		
		return map;
	}
	
	//닉네임 설정 요청
	@PostMapping("/user/nickname")
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
