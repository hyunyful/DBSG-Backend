package com.dbsg.backend.service;

import java.util.List;
import java.util.Map;

import com.dbsg.backend.domain.Menu;

public interface MenuService {

	//메뉴,레시피 등록
	public boolean menuInsert(Map<String,Object> param);
	
	//전체 메뉴 조회
	public List<Menu> menuList();
	
	//문자열로 메뉴 검색
	public List<Menu> menuStringSearch(String keyword);
	
	//태그로 메뉴 검색
	public List<Menu> menuTagSearch(int tagNo);
	
	//테스트
	public boolean test(String menu_name);
	
}
