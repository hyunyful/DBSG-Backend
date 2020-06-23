package com.dbsg.backend.service;

import java.util.List;
import java.util.Map;

import com.dbsg.backend.domain.Menu;
import com.dbsg.backend.domain.MenuDisplay;

public interface MenuService {

	//메뉴,레시피 등록
	public boolean menuInsert(Map<String,Object> param);
	
	//전체 메뉴 조회
	public List<MenuDisplay> menuList();
	
	//문자열로 메뉴 검색
	public List<MenuDisplay> searchMenuByString(String keyword);
	
	//태그로 메뉴 검색
	public List<MenuDisplay> searchMenuByTag(int tagNo);
	
}
