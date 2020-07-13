package com.dbsg.backend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/controller")
public class InfoController {

	/* 컨트롤러 정보 페이지 관련 메소드들 */
	
	//@GetMapping("/controller")
	@GetMapping
	public String Controller() {
		
		return "controller";
	}
	
	//@GetMapping("/controller/menuInsert")
	@GetMapping("/menuInsert")
	public String menuInsertInfo() {
		
		return "/controller/menuInsert";
	}
	
	//@GetMapping("/controller/menuList")
	@GetMapping("/menuList")
	public String menuListInfo() {
		
		return "/controller/menuList";
	}
	
	//@GetMapping("/controller/stringSearch")
	@GetMapping("/stringSearch")
	public String stringSearchInfo() {
		
		return "/controller/stringSearch";
	}
	
	//@GetMapping("/controller/tagSearch")
	@GetMapping("/tagSearch")
	public String tagSearchInfo() {
		
		return "/controller/tagSearch";
	}
	
	//@GetMapping("/controller/showRecipe")
	@GetMapping("/showRecipe")
	public String showRecipeInfo() {
		
		return "/controller/showRecipe";
	}
	
	//@GetMapping("/controller/login/kakao")
	@GetMapping("login/kakao")
	public String kakaoLoginInfo() {
		
		return "/controller/kakaoLogin";
	}
	
	//@GetMapping("/controller/setNickname")
	@GetMapping("/setNickname")
	public String setNicknameInfo() {
		
		return "/controller/setNickname";
	}
	
	//@GetMapping("/controller/login/naver")
	@GetMapping("/login/naver")
	public String naverLoginInfo() {
		
		return "/controller/naverLogin";
	}
	
	//@GetMapping("/controller/login/google")
	@GetMapping("/login/google")
	public String googleLoginInfo() {
		
		return "/controller/googleLogin";
	}
}
