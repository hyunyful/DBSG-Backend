package com.dbsg.backend;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbsg.backend.dao.UserDao;
import com.dbsg.backend.domain.User;
import com.dbsg.backend.domain.UserDisplay;

@RestController
@RequestMapping("/test")
public class TestController {
	
	@Autowired
	private UserDao userDao;
	
	@GetMapping
	public Map<String,Object> getTest1(){
		Map<String,Object> map = new HashMap<>();
		
		Map<String,Object> infoMap = new HashMap<>();
		Map<String,Object> dataMap = new HashMap<>();
		
		infoMap.put("이름", "최서현");
		infoMap.put("나이", "27");
		
		dataMap.put("1", "data1");
		dataMap.put("2", "data2");
		dataMap.put("3", "data3");
		
		map.put("info", infoMap);
		map.put("data", dataMap);
		
		return map;
	}
	
	
	@PostMapping
	public Map<String,Object> postTest1(@RequestBody Map<String,Object> param){
		Map<String,Object> map = new HashMap<>();
		
		JSONObject data = new JSONObject(param);
		String email = data.getString("email");
		String type = data.getString("type");
		
		User user = new User();
		user.setUser_email(email);
		user.setUser_type(type);
		
		UserDisplay result = userDao.userCheck(user);
		
		map.put("result", result);
		
		return map;
	}
}
