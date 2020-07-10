package com.dbsg.backend;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dbsg.backend.domain.Prefer;
import com.dbsg.backend.service.PreferService;

import io.swagger.annotations.ApiOperation;

@RestController
public class PreferController {
	
	@Autowired
	private PreferService preferService;

	//메뉴 즐겨찾기 추가
	@ApiOperation(value="즐겨찾기 추가")
	@PostMapping("/prefer/add")
	public Map<String,Object> addPrefer(@RequestBody Prefer prefer){
		Map<String,Object> map = new HashMap<>();
		
		map = preferService.addPrefer(prefer);
		
		map.put("con", "success");
		map.put("sendData", prefer);
		
		return map;
	}
	
	//즐겨찾기 수가 많은 메뉴 3개 추천
	@ApiOperation(value="즐겨찾기 수가 높은 메뉴 추천")
	@GetMapping("/prefer/recommend")
	public Map<String,Object> preferRecommend(){
		Map<String,Object> map = new HashMap<>();
		
		map = preferService.preferRecommend();
		
		map.put("con", "success");
		
		return map;
	}
	
	//즐겨찾기 삭제
	@ApiOperation(value="즐겨찾기 삭제")
	@PostMapping("/prefer/delete")
	public Map<String,Object> deletePrefer(@RequestBody Prefer prefer){
		Map<String,Object> map = new HashMap<>();
		
		map = preferService.deletePrefer(prefer);
		
		map.put("con", "success");
		map.put("sendData", prefer);
		
		return map;
	}
}
