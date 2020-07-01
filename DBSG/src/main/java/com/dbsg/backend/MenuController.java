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
		boolean result = false;		//결과를 담을 boolean 변수
		
		map.put("con", "success");		//controller에 연결된 것을 알려주는 key,value
		
		try {
			result = menuService.menuInsert(param);
		}catch(Exception e) {
			e.printStackTrace();
			map.put("menuInsert", "fail");
			map.put("error", e.getMessage());
			return map;
		}
		
		//System.out.println("controller "+result);
		
		//성공하면 success 보내기
		if(result == true) {
			map.put("menuInsert", "success");
		}
		
		return map;
	}
	
	//전체 메뉴 조회
	@GetMapping("/menu/list")
	public Map<String,Object> menuList(){
		Map<String,Object> map = new HashMap<>();
		List<MenuDisplay> list = new ArrayList<>();
		
		map.put("con", "success");
		
		try {
			list = menuService.menuList();
		}catch(Exception e) {
			e.printStackTrace();
			map.put("menuList", "fail");
			map.put("error", e.getMessage());
			return map;
		}
		
		//실행에 에러가 없으면
		map.put("menuList", "success");		//성공 알려주기
		map.put("size", list.size());				//data size 알려주기
		map.put("data", list);						//data 보내주기
		
		return map;
	}
	
	//문자열로 메뉴 검색
	@GetMapping("/menu/search/{keyword}")
	public Map<String,Object> menuStringSearch(@PathVariable String keyword){
		Map<String,Object> map = new HashMap<>();
		List<MenuDisplay> list = new ArrayList<>();
		
		map.put("con", "success");
		map.put("keyword", keyword);
		
		try {
			list = menuService.searchMenuByString(keyword);
		}catch(Exception e) {
			e.printStackTrace();
			map.put("menuList", "fail");
			map.put("error", e.getMessage());
			return map;
		}
		
		map.put("menuList", "success");
		map.put("size", list.size());
		map.put("data", list);
		
		return map;
	}
	
	//태그로 메뉴 검색
	@GetMapping("/menu/tag/{tagNo}")
	public Map<String,Object> menuTagSearch(@PathVariable int tagNo){
		Map<String,Object> map = new HashMap<>();
		List<MenuDisplay> list = new ArrayList<>();
		
		map.put("con", "success");
		map.put("keyword", tagNo);
		
		try {
			list = menuService.searchMenuByTag(tagNo);
		}catch(Exception e) {
			e.printStackTrace();
			map.put("menuList", "fail");
			map.put("error", e.getMessage());
			return map;
		}
		
		map.put("menuList", "success");
		map.put("size", list.size());
		map.put("data", list);
		
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
