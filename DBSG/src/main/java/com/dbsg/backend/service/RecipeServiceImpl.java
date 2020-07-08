package com.dbsg.backend.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.dbsg.backend.dao.RecipeDao;
import com.dbsg.backend.domain.Recipe;

@Service
public class RecipeServiceImpl implements RecipeService {

	@Autowired
	private RecipeDao recipeDao;
	
	@Autowired
	private DataSourceTransactionManager tm;
	
	//해당 메뉴의 전체 레시피 보기
	@Override
	public Map<String,Object> showRecipe(int menu_no) {
		Map<String,Object> map = new HashMap<>();
		List<Recipe> list = new ArrayList<>();
		
		DefaultTransactionDefinition def = new DefaultTransactionDefinition(); 
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = tm.getTransaction(def); 
		
		try {
			//menu_no를 dao에 넣기
			list = recipeDao.showRecipe(menu_no);
			
			//리스트에 데이터가 있으면 조회수 증가
			if(list.size() >0) {
				int r = recipeDao.readCnt(menu_no);
				
				//조회수 증가에 성공하면
				if(r>0) {
					map.put("recipe", "success");
					map.put("data", list);
					map.put("size", list.size());
				}else {		//조회수 증가에 실패하면
					map.put("recipe", "fail");
					map.put("error", "조회수 증가 실패! 조회수 결과 "+r);
				}
			}else {		//리스트에 데이터가 없으면
				map.put("recipe", "success");
				map.put("data", list);
				map.put("size", list.size());
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			tm.rollback(status);
			map.put("recipe", "fail");
			map.put("error", e.getMessage());
			throw e;
		}
		
		tm.commit(status);
		
		return map;
	}

}
