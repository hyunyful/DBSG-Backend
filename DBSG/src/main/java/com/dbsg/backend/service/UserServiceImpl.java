package com.dbsg.backend.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

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
	public Map<String,Object> kakaoLogin(String code) {
		Map<String,Object> map = new HashMap<>();
		
		String accessToken = "";		//모든 작업이 성공적으로 수행된 경우 리턴
		String error = "";					//중간에 작업이 실패하는 경우 리턴
		
		DefaultTransactionDefinition def = new DefaultTransactionDefinition(); 
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = tm.getTransaction(def); 
		
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
			//String redirect_uri = "http://localhost:8080/user/login/kakao";
			String redirect_uri = "http://15.165.215.38:8080/DBSG/user/login/kakao";
			
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
				
				//사용자 정보 받아오기
				uri = "https://kapi.kakao.com/v2/user/me";		//변수 재활용
				
				url = new URL(uri);
				
				con = (HttpURLConnection) url.openConnection();
				
				con.setRequestMethod("GET");
				con.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
				con.setRequestProperty("Authorization","Bearer "+accessToken);
				
				con.connect();
				
				//결과 코드가 200이면 성공
				resultCode = con.getResponseCode();
				
				//성공하면
				if(resultCode == 200) {
					input = new BufferedReader(new InputStreamReader(con.getInputStream()));
					
					//재활용 변수 초기화
					line = "";
					result = "";
					
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
					
					//System.out.println("result는 "+result);
					
					//읽은 결과 JSONObject로 파싱
					data = new JSONObject(result);
					JSONObject kakao_account = data.getJSONObject("kakao_account");
					JSONObject properties = data.getJSONObject("properties");
					
					//필요한 사용자 정보
					String profile_image = "";		//프로필 사진 url 정보
					String email = "";					//이메일
					String age_range = "";			//연령대
					String birthday = "";				//생일
					String gender = "";				//성별
					int user_gender = 0;				//gender가 male이면 user_gender는 1, female이면 user_gender는 2
					
					//정보 추출할 때 해당 키가 없는 경우를 대비한 에러처리
					//없으면 기본값 할당
					
					//profile_image가 있으면
					if(properties.has("profile_image")) {
						profile_image = properties.getString("profile_image");
					}else {			//profile_image가 없으면 
						profile_image = "default.png";
					}
					
					//이메일이 있으면
					if(kakao_account.has("email")) {
						email = kakao_account.getString("email");
					}else {			//없으면
						email = "";
					}
					
					//연령대
					if(kakao_account.has("age_range")) {
						age_range = kakao_account.getString("age_range");
					}else {
						age_range = "";
					}

					//생일
					if(kakao_account.has("birthday")) {
						birthday = kakao_account.getString("birthday");
					}else {
						birthday = "";
					}

					//성별
					if(kakao_account.has("gender")) {
						gender = kakao_account.getString("gender");
						
						//gender가 male이면 1 female이면 2
						if("male".equals(gender)) {
							user_gender = 1;
						}else if("female".equals(gender)) {
							user_gender = 2;
						}
						
					}else {
						user_gender = 0;
					}
					
					//사용자 정보를 User 객체에 담아서 리턴
					User user = new User();
					user.setUser_image(profile_image);
					user.setUser_email(email);
					user.setUser_age(age_range);
					user.setUser_birthday(birthday);
					user.setUser_gender(user_gender);
					user.setUser_type("kakao");
					
					//회원정보로 회원가입 작업
					//1.해당 정보의 회원이 DB에 있는지 확인
					//2.없으면 정보 insert 후 닉네임 필요하다고 보내기
					//3.있으면 정보 보내주기
					
					UserDisplay ud = userDao.userCheck(user.getUser_email());
					
					//회원이 있으면
					if(ud != null) {
						//System.out.println("회원 정보가 있네");
					
						result = "exist";
						
						//있다는 말과 함께
						map.put("result", result);
						//회원 정보 보내주기
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
							
							//4.회원번호 찾아서 같이 리턴
							int user_no = userDao.findNoByInfo(user.getUser_email());
							
							//System.out.println(user_no);
							
							map.put("result", result);
							map.put("user_no", user_no);
							
							tm.commit(status);
							return map;
						}
						//회원가입에 실패한 경우
						else {
							result = "error";
							tm.rollback(status); 
							map.put("result", result);
							map.put("error", "회원가입에 실패");
							return map;
						}
					}
					
					
				}else {		//사용자 정보 불러오기의 resultCode가 200이 아니면
					error = "통신 실패링2";
					map.put("resultCode", resultCode);
					map.put("error", error);
					return map;
				}
				
			}
			//실패하면 (첫번째 resultCode가 200이 아니면)
			else {
				error = "통신 실패링1";
				map.put("resultCode", resultCode);
				map.put("error", error);
				return map;
			}
			
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
			System.out.println("URL 만들기에 실패링");
			error = e.getMessage();
			map.put("error", error);
			return map;
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("URLConnection에 실패링");
			error = e.getMessage();
			map.put("error", error);
			return map;
		}
		
	}

	//닉네임 중복검사
	@Override
	public Map<String,Object> nicknameCheck(Map<String, Object> param) {
		Map<String,Object> map = new HashMap<>();
		
		//1.해당 요청의 닉네임이 있는지 확인
		//2.없으면 선점
		//3.있으면 다른 닉네임 가져오라고 리턴
		
		DefaultTransactionDefinition def = new DefaultTransactionDefinition(); 
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = tm.getTransaction(def); 
		
		try {
			//param json으로 파싱
			JSONObject json = new JSONObject(param);
			
			//추출할 정보
			String nickname = "";
			int user_no = 0;
			
			//json에서 정보 추출
			if(json.has("nickname")) {
				nickname = json.getString("nickname");
			}else {
				map.put("nicknameCheck", "fail");
				map.put("error", "there is no nickname");
				return map;
			}
			
			if(json.has("user_no")) {
				user_no = json.getInt("user_no");
			}else {
				map.put("nicknameCheck", "fail");
				map.put("error", "there is no user_no");
				return map;
			}
			
			//중복검사 수행
			String result = userDao.nicknameCheck(nickname);
			
			//해당 닉네임이 있으면 있다고 리턴
			if(result != null) {
				map.put("nicknameCheck", "success");
				map.put("data", "exist");
				tm.commit(status);
				return map;
			}else {		//해당 닉네임이 없으면
				//User 객체에 정보를 담아서
				User user = new User();
				user.setUser_nickname(nickname);
				user.setUser_no(user_no);
				
				//닉네임 설정해두기
				int r1 = userDao.setNickname(user);
				
				//닉네임 선점에 성공하면
				if(r1>0) {
					map.put("nicknameCheck", "success");
					tm.commit(status);
					return map;
				}else {			//닉네임 설정에 실패하면
					map.put("nicknameCheck", "fail");
					map.put("error", "닉네임 선점에 실패");
					tm.rollback(status);
					return map;
				}
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			map.put("nicknameCheck", "fail");
			map.put("error", e.getMessage());
			throw e;
		}

	}

	//닉네임 설정 요청
	@Override
	public Map<String,Object> setNickname(Map<String, Object> param) {
		Map<String,Object> map = new HashMap<>();
		
		DefaultTransactionDefinition def = new DefaultTransactionDefinition(); 
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = tm.getTransaction(def); 
		
		try {
			
			//json 파싱
			JSONObject data = new JSONObject(param);
			
			String nickname = "";
			int user_no = 0;
			
			//정보 추출
			//뭐 하나라도 없으면 return
			if(data.has("nickname")) {
				nickname = data.getString("nickname");
			}else {
				map.put("setNickname", "fail");
				map.put("error", "there is no nickname value");
				return map;
			}
			
			if(data.has("user_no")) {
				user_no = data.getInt("user_no");
			}else {
				map.put("setNickname", "fail");
				map.put("error", "there is no user_no value");
				return map;
			}
			
			User user = new User();
			user.setUser_nickname(nickname);
			user.setUser_no(user_no);
			
			//dao 호출
			int result = userDao.setNickname(user);
			
			//닉네임 설정에 성공하면
			if(result > 0) {
				
				//System.out.println("성공쓰");
				
				//회원번호로 해당 회원 정보 불러오기
				UserDisplay ud = userDao.findInfoByNo(user_no);
				
				//회원이 있으면
				if(ud != null) {
					map.put("setNickname", "success");
					map.put("data", ud);
				}
				//회원이 없으면
				else {
					//System.out.println("회원없쓰");
					map.put("setNickname", "fail");
					map.put("error", "there is no user..!");
				}
				
			}
			//닉네임 설정에 실패하면
			else {
				//System.out.println("실패쓰");
				map.put("setNickname", "fail");
				map.put("error", "setNickname fail");
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			tm.rollback(status);
			map.put("setNickname", "fail");
			map.put("error", e.getMessage());
			throw e;
		}
		
		tm.commit(status);
		
		return map;
	}

	//회원번호로 회원정보 찾기
	@Override
	public UserDisplay findInfoByNo(int user_no) {
		UserDisplay userInfo = userDao.findInfoByNo(user_no);
		
		return userInfo;
	}

	//네이버 로그인을 위한 토큰 생성
	@Override
	public Map<String, Object> naverState() {
		Map<String,Object> map = new HashMap<>();
		String state = "";		//토큰을 저장할 변수

		try {
			
			//상태 토큰으로 사용할 랜덤 문자열 생성
			SecureRandom random = new SecureRandom();
			state = new BigInteger(130, random).toString(32);
			
		}catch(Exception e) {
			e.printStackTrace();
			map.put("naverToken", "fail");
			map.put("error", e.getMessage());
		}
		
		map.put("naverToken", "success");
		map.put("state", state);
		
		return map;
	}

	//네이버 로그인
	@Override
	public Map<String, Object> naverLogin(String state, String code) {
		Map<String,Object> map = new HashMap<>();
		
		String client_id = "m8QQGZXfACv5KdlFw8oI";
		String client_secret = "hYid659iHl";
		
		//https://nid.naver.com/oauth2.0/token?client_id={클라이언트 아이디}&client_secret={클라이언트 시크릿}&grant_type=authorization_code&state={상태 토큰}&code={인증 코드}
		String uri1 = "https://nid.naver.com/oauth2.0/token?";
		uri1 += "client_id="+client_id+"&client_secret="+client_secret;
		uri1 += "&grant_type=authorization_code&state="+state+"&code="+code;
		
		try {
			URL url = new URL(uri1);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			
			con.setRequestMethod("GET");
			
			con.connect();
			
			//결과 코드가 200이면 정상
			if(con.getResponseCode() == 200) {
				BufferedReader input = new BufferedReader(new InputStreamReader(con.getInputStream()));
				
				String line = "";				//결과 한줄씩
				String result = "";			//총 결과 담을 변수
				
				while(true) {
					line = input.readLine();
					
					if(line == null) {
						break;
					}
					
					result += line;
				}
				
				input.close();
				
				//System.out.println(result);
				
				//JSON으로 파싱
				JSONObject data = new JSONObject(result);
				
				//필요한 데이터만 추출
				String accessToken = data.getString("access_token");			//접근 토큰
				String token_type = data.getString("token_type");				//사용자 정보 요청할 때 필요
				
				//토큰으로 네이버 사용자의 정보 요청 uri
				String uri2 = "https://openapi.naver.com/v1/nid/me";
				
				URL url2 = new URL(uri2);
				HttpURLConnection con2 = (HttpURLConnection) url2.openConnection();
				
				con2.setRequestMethod("GET");
				//헤더에 정보 담아서
				con2.setRequestProperty("Authorization", token_type+" "+accessToken);
				
				//보내기
				con2.connect();
				
				//결과 코드가 200이면 성공
				if(con2.getResponseCode() == 200) {
					input = new BufferedReader(new InputStreamReader(con2.getInputStream()));
					
					line = "";
					result = "";		//두개 변수 초기화
					
					while(true) {
						line = input.readLine();
						
						if(line == null) {
							break;
						}
						
						result += line;
					}
					
					input.close();
					
					//System.out.println(result);
					
					//JSON 파싱
					data = new JSONObject(result);

					//response가 있으면
					if(data.has("response")) {
						JSONObject response = data.getJSONObject("response");
						
						//추출할 정보
						String profile_image = "";		//사진
						String age = "";						//나이대
						String gender = "";				//성별 (M,F로 리턴됨)
						int user_gender = 0;				//gender가 M이면 1, F면 2
						String email = "";					//이메일
						String birthday = "";				//생일 (02-13 형태로 오니까 0213으로 변경)
						
						//값이 있는것만 추출
						if(response.has("profile_image")) {
							profile_image = response.getString("profile_image");
						}
						
						if(response.has("age")) {
							age = response.getString("age");
						}
						
						if(response.has("gender")) {
							gender = response.getString("gender");
							
							if("M".equals(gender)) {
								user_gender = 1;
							}else if("F".equals(gender)) {
								user_gender = 2;
							}
						}
						
						if(response.has("email")) {
							email = response.getString("email");
						}
						
						if(response.has("birthday")) {
							String tempBirthday = response.getString("birthday");
							
							birthday = tempBirthday.substring(0,2) + tempBirthday.substring(3,5);
						}
						
						//User 객체에 저장
						User user = new User();
						user.setUser_age(age);
						user.setUser_birthday(birthday);
						user.setUser_email(email);
						user.setUser_gender(user_gender);
						user.setUser_image(profile_image);
						user.setUser_type("naver");
						
						//System.out.println(user.toString());
						
						//email로 해당 유저가 이미 DB에 있는지 확인
						UserDisplay ud = userDao.userCheck(email);
						
						//해당 이메일의 유저가 없으면 회원가입 진행
						if(ud == null) {
							
							int r1 = userDao.userJoin(user);
							
							//회원가입에 성공하면
							if(r1>0) {
								//닉네임이 필요하다고 유저 번호와 함께 리턴
								
								//유저 번호 찾아오기
								int user_no = userDao.findNoByInfo(email);
								
								map.put("naverLogin", "success");
								map.put("data", "needNickname");
								map.put("user_no", user_no);
								
								return map;
								
								
							}else {		//실패하면
								map.put("naverLogin", "fail");
								map.put("error", "userJoin fail..");
								return map;
							}
							
						}else {			//해당 이메일의 유저가 있으면
							map.put("naverLogin", "success");
							map.put("data", "exist");
							map.put("user_info", ud);
							return map;
						}
						
						
					}else {			//response가 없으면
						map.put("naverLogin", "fail");
						map.put("error", "result json dosen't has response..try again");
						return map;
					}
					
				}
				
				
			}else {			//결과가 200이 아니면
				map.put("naverLogin", "fail");
				map.put("error", "resultCode is not 200, resultCode is "+con.getResponseCode());
				return map;
			}
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
			System.out.println("URL 만드는데 실패");
			map.put("naverLogin", "fail");
			map.put("error", "URL Make Error1");
			return map;
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("URL Connection 실패");
			map.put("naverLogin", "fail");
			map.put("error", "URL Connection Error1");
			return map;
		}
		
		
		return map;
	}

}
