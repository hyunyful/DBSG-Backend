package com.dbsg.backend;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "home";
	}
	
	
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
