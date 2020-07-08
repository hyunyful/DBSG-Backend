package com.dbsg.backend.service;

import java.util.Map;

import com.dbsg.backend.domain.Prefer;

public interface PreferService {

	//즐겨찾기 수가 높은 3개 메뉴 추천
	public Map<String,Object> preferRecommend();
	
	//즐겨찾기 추가
	public Map<String,Object> addPrefer(Prefer prefer);
	
	//즐겨찾기 삭제
	public Map<String,Object> deletePrefer(Prefer prefer);
}
