package com.dbsg.backend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InfoController {

	/* 컨트롤러 정보 페이지 관련 메소드들 */
	
	@GetMapping("/controller")
	public String Controller() {
		
		return "controller";
	}
	
	@GetMapping("/controller/menuInsert")
	public String menuInsertInfo() {
		
		return "/controller/menuInsert";
	}
	
	@GetMapping("/controller/menuList")
	public String menuListInfo() {
		
		return "/controller/menuList";
	}
	
	@GetMapping("/controller/stringSearch")
	public String stringSearchInfo() {
		
		return "/controller/stringSearch";
	}
	
	@GetMapping("/controller/tagSearch")
	public String tagSearchInfo() {
		
		return "/controller/tagSearch";
	}
	
	@GetMapping("/controller/showRecipe")
	public String showRecipeInfo() {
		
		return "/controller/showRecipe";
	}
	
	@GetMapping("/controller/login/kakao")
	public String kakaoLoginInfo() {
		
		return "/controller/kakaoLogin";
	}
	
	@GetMapping("/controller/setNickname")
	public String setNicknameInfo() {
		
		return "/controller/setNickname";
	}
	
	@GetMapping("/controller/login/naver")
	public String naverLoginInfo() {
		
		return "/controller/naverLogin";
	}
	
	@GetMapping("/controller/login/google")
	public String googleLoginInfo() {
		
		return "/controller/googleLogin";
	}
}
