package com.dbsg.backend;

import java.util.HashMap;
import java.util.Map;

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

	//카카오 로그인 redirect_uri
	//인증 코드 받기
	@GetMapping("/user/login/kakao")
	public Map<String,Object> kakaoLogin(@RequestParam("code") String code){
		Map<String,Object> map = new HashMap<>();
		int resultCode;
		
		map.put("con", "success");
		
		//System.out.println("접근 성공~ code는 "+code);
		Map<String,Object> tokenMap = userService.getAccessToken(code);
		
		String tokenError = (String) tokenMap.get("error");
		//System.out.println("tokenError는 "+tokenError);
		String accessToken = (String) tokenMap.get("accessToken");
		//System.out.println("accessToken는 "+accessToken);
		
		//접근 토큰을 얻는 과정에서 에러가 없었으면
		if(tokenError == null) {
			//사용자 정보 요청하기
			Map<String,Object> infoMap = userService.getUserInfo(accessToken);
			
			String infoError = (String) infoMap.get("error");
			//System.out.println("controller에서 infoE는 "+infoE);
			String result = (String) infoMap.get("result");
			//map.put("result", result);
			
			//사용자 정보 요청에서 에러가 났으면 
			if(infoError != null) {
				resultCode = (int) infoMap.get("resultCode");
				map.put("resultCode", resultCode);
				//error 내용 담아서 리턴
				map.put("error", infoError);
				return map;
			}
			//사용자 정보 요청에서 에러가 안났으면
			else {
				//infoMap에서 user 정보를 담을 user 객체 추출
				User user = (User) infoMap.get("user");
				
				//회원정보 작업
				Map<String,Object> userMap = userService.join(user);
				
				//msg에 회원정보 작업의 결과가 담겨있음
				String msg = (String) userMap.get("msg");
				
				//error가 있으면
				if("error".equals(msg)) {
					map.put("data", "error");
					map.put("error", msg);
					
					return map;
				}
				//msg가 needNickname이면
				else if("needNickname".equals(msg)) {
					//user_no 랑 같이 리턴
					//해당 유저 번호 다시 보내달라고..
					int no = (int) userMap.get("user_no");
				
					//System.out.println("controller : "+no);
					
					map.put("data", msg);
					map.put("user_no", no);
					
					return map;
				}
				//해당 회원의 정보가 있으면
				else if("exist".equals(msg)){
					
					UserDisplay ud = (UserDisplay) userMap.get("ud");
					int user_no = ud.getUser_no();
					
					//user_no로 회원정보 찾아서 리턴
					UserDisplay userInfo = userService.findInfoByNo(user_no);
					
					map.put("user_info", userInfo);
					map.put("data", msg);
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
	
	//닉네임 중복검사
	@PostMapping("/user/nicknameCheck")
	public Map<String,Object> nicknameCheck(@RequestBody Map<String,Object> param){
		Map<String,Object> map = new HashMap<>();
		map.put("con", "success");
		
		boolean result = userService.nicknameCheck(param);
		
		map.put("nicknameResult", result);
		
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
