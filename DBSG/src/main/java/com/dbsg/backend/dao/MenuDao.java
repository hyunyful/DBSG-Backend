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
	
	//방금 insert된 primary key 찾기
	public Integer findNo() {
		return sqlSession.selectOne("menu.findNo");
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
	
	//문자열로 메뉴번호 검색
	public List<Integer> searchMenuNoByString(String keyword){
		return sqlSession.selectList("menu.searchMenuNoByString", keyword);
	}
	
	//메뉴 번호로 해당 메뉴 정보 검색
	public MenuDisplay searchMenuInfoByNo(int menu_no){
		return sqlSession.selectOne("menu.searchMenuInfoByNo", menu_no);
	}
	
	//태그로 메뉴 검색
	public List<MenuDisplay> searchMenuByTag(String tag){
		return sqlSession.selectList("menu.searchMenuByTag", tag);
	}
	
	//조회수로 메뉴 추천
	public List<MenuDisplay> menuRecommendByreadCnt(){
		return sqlSession.selectList("menu.menuRecommendByreadCnt");
	}
	
	//menu_stat 테이블과 menu 테이블에서 해당 메뉴 번호 삭제
	public int deleteMenu(int menu_no) {
		return sqlSession.delete("menu.deleteMenu",menu_no);
	}
}
