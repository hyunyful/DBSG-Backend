package com.dbsg.backend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbsg.backend.dao.RecipeDao;
import com.dbsg.backend.domain.Recipe;

@Service
public class RecipeServiceImpl implements RecipeService {

	@Autowired
	private RecipeDao recipeDao;
	
	//해당 메뉴의 전체 레시피 보기
	@Override
	public List<Recipe> showRecipe(int menu_no) {
		List<Recipe> list = new ArrayList<>();
		
		//menu_no를 dao에 넣기
		list = recipeDao.showRecipe(menu_no);
		
		return list;
	}

}
