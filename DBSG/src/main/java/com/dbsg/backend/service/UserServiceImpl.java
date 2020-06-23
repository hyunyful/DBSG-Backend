package com.dbsg.backend.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.dbsg.backend.dao.UserDao;
import com.dbsg.backend.domain.User;
import com.dbsg.backend.domain.UserDisplay;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private DataSourceTransactionManager tm;

	//카카오 로그인 접근 토큰 받기
	@Override
	public Map<String,Object> getAccessToken(String code) {
		Map<String,Object> map = new HashMap<>();
		
		String accessToken = "";		//모든 작업이 성공적으로 수행된 경우 리턴
		String error = "";					//중간에 작업이 실패하는 경우 리턴
		
		//requestURI
		String uri = "https://kauth.kakao.com/oauth/token";
		
		try {
			URL url = new URL(uri);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			
			//method 설정
			con.setRequestMethod("POST");		
			//header에는 setRequestProperty로 설정
			con.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
			// POST 파라미터 전달을 위한 설정
			con.setDoOutput(true);
			
			//파라미터로는 grant_type, client_id, redirect_uri, code 를 전송
			//grant_type은 authorization_code로 고정, client_id와 redirect_uri는 변경 가능하므로 변수로 설정, code는 매개변수
			String client_id = "51c7c8f63345a28a25a4b28fff7048ef";
			String redirect_uri = "http://localhost:8080/user/login/kakao";
			
			//전체 파라미터 (전송용)
			String parameter = "grant_type=authorization_code&client_id="+client_id;
			parameter += "&redirect_uri="+redirect_uri+"&code="+code;
			
			//전송 보내기
			DataOutputStream output = new DataOutputStream(con.getOutputStream());
			output.writeBytes(parameter);
			output.flush();
			
			//결과 코드가 200이면 성공
			int resultCode = con.getResponseCode();
			
			//성공이면
			if(resultCode == 200) {
				BufferedReader input = new BufferedReader(new InputStreamReader(con.getInputStream()));
				
				//결과 한줄 한줄을 담을 문자열
				String line = "";
				//총 결과를 담을 문자열
				String result = "";
				
				while(true) {
					//결과 읽기
					line = input.readLine();
					
					//읽을 결과가 없으면 = 다 읽었으면
					if(line == null) {
						//반복문 나가기
						break;
					}
					
					//결과가 있으면 result에 추가
					result = result + line;
				}
				
				//stream 닫기
				output.close();
				input.close();
				
				//System.out.println(result);
				
				//결과를 JSONObject로 파싱
				JSONObject data = new JSONObject(result);
				//access_token 값을 map에 담아서 리턴
				accessToken = data.getString("access_token");
				
				map.put("accessToken", accessToken);
				
				return map;
				
			}
			//실패하면
			else {
				error = "통신 실패링";
				map.put("error", error);
				return map;
			}
			
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
			error = "URL 만들기에 실패링";
			map.put("error", error);
			return map;
		} catch (IOException e) {
			e.printStackTrace();
			error = "URLConnection에 실패링";
			map.put("error", error);
			return map;
		}
		
	}

	//카카오 로그인 사용자 정보 요청
	@Override
	public Map<String, Object> getUserInfo(String accessToken) {
		Map<String,Object> map = new HashMap<>();
		
		//작업에 실패하는 경우 반환되는 error 내용
		String error = "";
		
		String uri = "https://kapi.kakao.com/v2/user/me";
		
		try {
			URL url = new URL(uri);
			
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			
			con.setRequestMethod("GET");
			con.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
			con.setRequestProperty("Authorization","Bearer "+accessToken);
			//con.setRequestProperty("Accept", "application/json");
			
			con.connect();
			
			//결과 코드가 200이면 성공
			int resultCode = con.getResponseCode();
			
			//성공하면
			if(resultCode == 200) {
				BufferedReader input = new BufferedReader(new InputStreamReader(con.getInputStream()));
				
				//결과 한줄한줄을 읽을 문자열
				String line = "";
				//총 결과를 담을 문자열
				String result = "";
				
				while(true) {
					//결과 읽기
					line = input.readLine();
					
					//만약 읽은 결과가 없으면
					if(line == null) {
						//반복문 나가기
						break;
					}
					
					//읽은 결과가 있으면 result에 붙여넣기
					result = result + line;
				}
				
				//stream 닫기
				input.close();
				
				//System.out.println(result);
				
				//읽은 결과 JSONObject로 파싱
				JSONObject data = new JSONObject(result);
				JSONObject kakao_account = data.getJSONObject("kakao_account");
				JSONObject profile = kakao_account.getJSONObject("profile");
				
				//필요한 사용자 정보 추출
				//프로필 사진 url 정보
				String profile_image_url = profile.getString("profile_image_url");
				//이메일
				String email = kakao_account.getString("email");
				//연령대
				String age_range = kakao_account.getString("age_range");
				//생일
				String birthday = kakao_account.getString("birthday");
				//성별
				String gender = kakao_account.getString("gender");
				int user_gender = 0;
				
				//성별이 male이면 1 female이면 2
				if("male".equals(gender)) {
					user_gender = 1;
				}else if("female".equals(gender)) {
					user_gender = 2;
				}
				
				//사용자 정보를 User 객체에 담아서 리턴
				User user = new User();
				user.setUser_image(profile_image_url);
				user.setUser_email(email);
				user.setUser_age(age_range);
				user.setUser_birthday(birthday);
				user.setUser_gender(user_gender);
				user.setUser_type("kakao");
				
				map.put("user", user);
				
				return map;
				
			}
			//실패하면
			else {
				error = "통신 실패링";
				map.put("error", error);
				return map;
			}
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
			error = "URL 만들기에 실패링";
			map.put("error", error);
			return map;
		} catch (IOException e) {
			e.printStackTrace();
			error = "URLConnection에 실패링";
			map.put("error", error);
			return map;
		}
		
	}

	//회원 정보로 DB 작업
	@Override
	public Map<String,Object> join(User user) {		
		Map<String,Object> map = new HashMap<>();
		
		String result = "";
		
		DefaultTransactionDefinition def = new DefaultTransactionDefinition(); 
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = tm.getTransaction(def); 
		
		try {
			
			//1.해당 정보의 회원이 DB에 있는지 확인
			//2.없으면 정보 insert 후 닉네임 필요하다고 보내기
			//3.있으면 정보 보내주기
			
			//1.해당 정보의 회원이 DB에 있는지 확인
			//정보는 user에 들어있음
			//System.out.println("user에 들어있는 정보 중 user_email은 "+user.getUser_email());
			//System.out.println("user에 들어있는 정보 중 user_image_url은 "+user.getUser_image());
			//System.out.println("user에 들어있는 정보 중 user_age는 "+user.getUser_age());
			//System.out.println("user에 들어있는 정보 중 user_gender는 "+user.getUser_gender());
			//System.out.println("user에 들어있는 정보 중 user_birthday는 "+user.getUser_birthday());
			//System.out.println("user에 들어있는 정보 중 user_type은 "+user.getUser_type());
			
			UserDisplay ud = userDao.userCheck(user);
			
			//회원이 있으면
			if(ud != null) {
				//System.out.println("회원 정보가 있네");
				
				//2.해당 회원의 user_no을 문자열로 리턴
				result = "exist";
				
				map.put("msg", result);
				map.put("ud", ud);
				
				tm.commit(status);
				return map;
			}
			//회원이 없으면
			else {
				//System.out.println("회원 정보가 없네");
				
				//2.해당 정보로 회원가입
				int result1 = userDao.userJoin(user);
				
				//회원가입에 성공하면
				if(result1 > 0) {
					//3.닉네임이 필요하다는 것을 알려주기
					result = "needNickname";
					
					map.put("msg", result);
					
					//4.회원번호 찾아서 같이 리턴
					int user_no = userDao.findNoByInfo(user);
					
					//System.out.println(user_no);
					
					map.put("user_no", user_no);
					
					tm.commit(status);
					return map;
				}
				//회원가입에 실패한 경우
				else {
					result = "error";
					tm.rollback(status); 
					map.put("msg", result);
					return map;
				}
			}
			
		}catch(Exception e) {
			e.printStackTrace(); 
			tm.rollback(status); 
			throw e; 
		}

	}

	//닉네임 중복검사
	@Override
	public boolean nicknameCheck(Map<String, Object> param) {
		boolean result = false;
		
		//param json으로 파싱
		JSONObject data = new JSONObject(param);
		
		//nickname 정보 추출
		String nickname = data.getString("nickname");
		
		//중복검사 수행
		String nicknameResult = userDao.nicknameCheck(nickname);
		
		//닉네임결과가 있으면 false 리턴, 없으면 true 리턴
		if(nicknameResult == null) {
			result = true;
		}else {
			result = false;
		}
		
		return result;
	}

	//닉네임 설정 요청
	@Override
	public Map<String,Object> setNickname(Map<String, Object> param) {
		Map<String,Object> map = new HashMap<>();
		
		String result = "";
		
		DefaultTransactionDefinition def = new DefaultTransactionDefinition(); 
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = tm.getTransaction(def); 
		
		//json 파싱
		JSONObject data = new JSONObject(param);
		
		//정보 추출
		String nickname = data.getString("nickname");
		int no = Integer.parseInt(data.getString("no"));
		
		User user = new User();
		user.setUser_nickname(nickname);
		user.setUser_no(no);
		
		try {
			
			//dao 호출
			int result2 = userDao.setNickname(user);
			
			//System.out.println(result2);
			
			//닉네임 설정에 성공하면
			if(result2 > 0) {
				
				//System.out.println("성공쓰");
				
				//회원번호로 해당 회원 정보 불러오기
				UserDisplay ud = userDao.findInfoByNo(no);
				
				//회원이 있으면
				if(ud != null) {
					map.put("ud", ud);
					
					result = "success";
				}
				//회원이 없으면
				else {
					//System.out.println("회원없쓰");
					
					result = "fail";
				}
				
			}
			//닉네임 설정에 실패하면
			else {
				//System.out.println("실패쓰");
				
				result = "fail";
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			tm.rollback(status);
			result = "error";
			throw e;
		}
		
		tm.commit(status);
		
		map.put("result", result);
		
		return map;
	}

	//회원번호로 회원정보 찾기
	@Override
	public UserDisplay findInfoByNo(int user_no) {
		UserDisplay userInfo = userDao.findInfoByNo(user_no);
		
		return userInfo;
	}

}
