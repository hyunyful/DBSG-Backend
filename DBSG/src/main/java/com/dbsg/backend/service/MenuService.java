package com.dbsg.backend.service;

import java.util.List;
import java.util.Map;

import com.dbsg.backend.domain.Menu;
import com.dbsg.backend.domain.MenuDisplay;

public interface MenuService {

	//메뉴,레시피 등록
	public Map<String,Object> menuInsert(Map<String,Object> param);
	
	//전체 메뉴 조회
	public Map<String,Object> menuList();
	
	//문자열로 메뉴 검색
	public Map<String,Object> searchMenuByString(String keyword);
	
	//태그로 메뉴 검색
	public Map<String,Object> searchMenuByTag(int tagNo);
	
	//조회수로 메뉴 추천
	public Map<String,Object> menuRecommendByreadCnt();
	
	//메뉴 삭제
	public Map<String,Object> deleteMenu(int menu_no);
}
