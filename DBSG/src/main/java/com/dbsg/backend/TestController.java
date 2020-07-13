package com.dbsg.backend;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dbsg.backend.dao.UserDao;
import com.dbsg.backend.domain.Menu;
import com.dbsg.backend.domain.Recipe;
import com.dbsg.backend.domain.User;
import com.dbsg.backend.domain.UserDisplay;

@RestController
@RequestMapping("/test")
public class TestController {
	
	@Autowired
	private UserDao userDao;
	
	@GetMapping("/{word}")
	public Map<String,Object> getTest1(@PathVariable String word){
		Map<String,Object> map = new HashMap<>();

		map.put("data", word);
		
		return map;
	}
	
	
	@PostMapping
	public Map<String,Object> postTest1(@ModelAttribute("r") Recipe r, @ModelAttribute("menu") Menu menu){
		Map<String,Object> map = new HashMap<>();
		
		System.out.println("접근은 됨");
		System.out.println(menu.toString());
		System.out.println(r.toString());
		
		map.put("menuData", menu);
		map.put("recipeData", r);
		
		return map;
	}
}
