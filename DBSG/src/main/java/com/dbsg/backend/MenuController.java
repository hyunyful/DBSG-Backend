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
public class MenuController {

	@Autowired
	private MenuService menuService;
	
	//메뉴,레시피 등록
	@PostMapping("/menu/insert")
	public Map<String,Object> menuInsert(@RequestBody Map<String,Object> param){
		Map<String,Object> map = new HashMap<>();		//리턴될 map
		
		map = menuService.menuInsert(param);
		
		map.put("con", "success");		//controller에 연결된 것을 알려주는 key,value
		
		return map;
	}
	
	//전체 메뉴 조회
	@GetMapping("/menu/list")
	public Map<String,Object> menuList(){
		Map<String,Object> map = new HashMap<>();
		
		map = menuService.menuList();
		
		map.put("con", "success");

		return map;
	}
	
	//문자열로 메뉴 검색
	@GetMapping("/menu/search/{word}")
	public Map<String,Object> stringSearch(@PathVariable String word){
		Map<String,Object> map = new HashMap<>();
		
		map = menuService.searchMenuByString(word);
		
		map.put("con", "success");
		map.put("keyword", word);
		
		return map;
	}
	
	//태그로 메뉴 검색
	@GetMapping("/menu/tag/{tagNo}")
	public Map<String,Object> tagSearch(@PathVariable int tagNo){
		Map<String,Object> map = new HashMap<>();
		
		map = menuService.searchMenuByTag(tagNo);

		map.put("con", "success");
		map.put("keyword", tagNo);
		
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
