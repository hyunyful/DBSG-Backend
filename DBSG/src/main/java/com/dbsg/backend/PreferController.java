package com.dbsg.backend;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PreferController {

	//메뉴 즐겨찾기
	@PostMapping("/add/prefer")
	public Map<String,Object> addPrefer(@RequestBody Map<String,Object> param){
		Map<String,Object> map = new HashMap<>();
		
		map.put("con", "success");
		map.put("sendData", param);
		
		
		
		return map;
	}
}
