package com.dbsg.backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbsg.backend.domain.Menu;
import com.dbsg.backend.domain.MenuDisplay;
import com.dbsg.backend.service.MenuService;

@RestController
@RequestMapping("/menu")
public class MenuController {

	@Autowired
	private MenuService menuService;
	
	//메뉴,레시피 등록
	@PostMapping("/insert")
	public Map<String,Object> menuInsert(@RequestBody Map<String,Object> param){
		Map<String,Object> map = new HashMap<>();
		map.put("connect", "true");
		
		boolean result = false;
		
		try {
			result = menuService.menuInsert(param);
		}catch(Exception e) {
			e.printStackTrace();
			map.put("menuInsert", "error");
		}
		
		//System.out.println("controller "+result);
		
		if(result == true) {
			map.put("menuInsert", "success");
		}else {
			map.put("menuInsert", "fail");
		}
		
		return map;
	}
	
	//전체 메뉴 조회
	@GetMapping("/list")
	public Map<String,Object> menuList(){
		Map<String,Object> map = new HashMap<>();
		map.put("connection", "success");
		
		List<MenuDisplay> list = new ArrayList<>();
		
		try {
			list = menuService.menuList();
		}catch(Exception e) {
			e.printStackTrace();
			map.put("menuList", "error");
			return map;
		}
		
		map.put("size", list.size());
		
		if(list.size() == 0) {
			map.put("menuList", "empty");
		}else {
			map.put("menuList", list);
		}
		
		return map;
	}
	
	//문자열로 메뉴 검색
	@GetMapping("/search/{keyword}")
	public Map<String,Object> menuStringSearch(@PathVariable String keyword){
		Map<String,Object> map = new HashMap<>();
		map.put("connect", "true");
		
		List<MenuDisplay> list = new ArrayList<>();
		
		try {
			list = menuService.searchMenuByString(keyword);
		}catch(Exception e) {
			e.printStackTrace();
			map.put("menuList", "error");
			return map;
		}
		
		map.put("size", list.size());
		
		if(list.size() == 0) {
			map.put("menuList", "empty");
		}else {
			map.put("menuList", list);
		}
		
		return map;
	}
	
	//태그로 메뉴 검색
	@GetMapping("/tag/{tagNo}")
	public Map<String,Object> menuTagSearch(@PathVariable int tagNo){
		Map<String,Object> map = new HashMap<>();
		map.put("connect", "true");
		
		List<MenuDisplay> list = new ArrayList<>();
		
		try {
			list = menuService.searchMenuByTag(tagNo);
		}catch(Exception e) {
			e.printStackTrace();
			map.put("menuList", "error");
			return map;
		}
		
		map.put("size", list.size());
		
		if(list.size() == 0) {
			map.put("menuList", "empty");
		}else {
			map.put("menuList", list);
		}
		
		return map;
	}
	
	//상민 테스트
	@PostMapping("/sm/test")
	public Map<String,Object> test(@RequestBody Map<String,Object> param){
		Map<String,Object> map = new HashMap<>();
	
		System.out.println("접근 성공");
		
		JSONObject json = new JSONObject(param);
		int num = json.getInt("data");
		
		int resultNum = num+1;
		
		map.put("resultNum", resultNum);
		
		return map;
	}
}
