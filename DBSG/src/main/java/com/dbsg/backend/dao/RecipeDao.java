package com.dbsg.backend.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dbsg.backend.domain.Recipe;

@Repository
public class RecipeDao {

	@Autowired
	private SqlSession sqlSession;
	
	//해당 메뉴의 전체 레시피 보여주기
	public List<Recipe> showRecipe(int menu_no){
		return sqlSession.selectList("recipe.showRecipe", menu_no);
	}
}
