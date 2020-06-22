package com.dbsg.backend.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dbsg.backend.domain.Menu;
import com.dbsg.backend.domain.MenuDisplay;
import com.dbsg.backend.domain.Recipe;

@Repository
public class MenuDao {

	@Autowired
	private SqlSession sqlSession;
	
	//메뉴테이블 등록
	public int menuInsert(Menu menu) {
		return sqlSession.insert("menu.menuInsert", menu);
	}
	
	//메뉴 이름으로 메뉴 번호 찾기
	public Integer findNobyName(String menu_name) {
		return sqlSession.selectOne("menu.findNoByName", menu_name);
	}
	
	//메뉴 스탯 테이블 등록
	public int menuStatInsert(int menu_no) {
		return sqlSession.insert("menu.menuStatInsert", menu_no);
	}
	
	//레시피 등록
	public int recipeInsert(Recipe recipe) {
		return sqlSession.insert("menu.recipeInsert", recipe);
	}
	
	//전체 메뉴 조회
	public List<MenuDisplay> menuList(){
		return sqlSession.selectList("menu.menuList");
	}
	
	//문자열로 메뉴 검색
	public List<MenuDisplay> searchMenuByString(String menu_name){
		return sqlSession.selectList("menu.searchMenuByString", menu_name);
	}
	
	//태그로 메뉴 검색
	public List<MenuDisplay> searchMenuByTag(String tag){
		return sqlSession.selectList("menu.searchMenuByTag", tag);
	}
}
