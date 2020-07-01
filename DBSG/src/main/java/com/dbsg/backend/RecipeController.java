package com.dbsg.backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbsg.backend.domain.MenuDisplay;
import com.dbsg.backend.domain.Recipe;
import com.dbsg.backend.service.RecipeService;

@RestController
public class RecipeController {
	
	@Autowired
	private RecipeService recipeService;

	//해당 메뉴의 전체 레시피 조회
	@GetMapping("/recipe/{menu_no}")
	public Map<String,Object> showRecipe(@PathVariable int menu_no){
		Map<String,Object> map = new HashMap<>();
		List<Recipe> list = new ArrayList<>();
		
		map.put("con", "success");
		map.put("keyword", menu_no);
		
		try {
			list = recipeService.showRecipe(menu_no);
		}catch(Exception e) {
			e.printStackTrace();
			map.put("recipe", "fail");
			map.put("error", e.getMessage());
			return map;
		}
		
		map.put("recipe", "success");
		map.put("size", list.size());
		map.put("data", list);
		
		return map;
	}
}
