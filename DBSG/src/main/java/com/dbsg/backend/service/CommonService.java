package com.dbsg.backend.service;

import org.json.JSONObject;

public class CommonService {

	//json에 해당 키가 있는지 확인하고 없으면 기본값을 주는 메소드
	public static boolean jsonCheck(JSONObject json, String key) {
		
		//있으면 
		if(json.has(key)) {
			return true;
		}else {
			return false;
		}
	}
	
}
