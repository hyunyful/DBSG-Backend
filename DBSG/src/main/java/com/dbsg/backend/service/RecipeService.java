package com.dbsg.backend.service;

import java.util.List;

import com.dbsg.backend.domain.Recipe;

public interface RecipeService {

	//해당 메뉴의 전체 레시피
	public List<Recipe> showRecipe(int menu_no);
	
}
