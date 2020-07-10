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

import com.dbsg.backend.dao.MenuDao;
import com.dbsg.backend.dao.RecipeDao;
import com.dbsg.backend.domain.MenuDisplay;
import com.dbsg.backend.domain.Recipe;

@Service
public class RecipeServiceImpl implements RecipeService {

	@Autowired
	private RecipeDao recipeDao;
	
	@Autowired
	private MenuDao menuDao;
	
	@Autowired
	private DataSourceTransactionManager tm;
	
	//해당 메뉴의 전체 레시피 보기
	@Override
	public Map<String,Object> showRecipe(int menu_no) {
		Map<String,Object> map = new HashMap<>();
		Map<String,Object> data = new HashMap<>();
		List<Recipe> list = new ArrayList<>();
		MenuDisplay menuInfo = new MenuDisplay();
		
		DefaultTransactionDefinition def = new DefaultTransactionDefinition(); 
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = tm.getTransaction(def); 
		
		try {
			//해당 메뉴번호에 해당하는 정보 불러오고, 레시피 불러오고 조회수 증가
			menuInfo = menuDao.searchMenuInfoByNo(menu_no);
			
			//해당하는 정보가 있으면
			if(menuInfo != null) {
				//해당 메뉴의 레시피 불러오기
				list = recipeDao.showRecipe(menu_no);
				
				//리스트에 데이터가 있으면 조회수 증가
				if(list.size() >0) {
					int r = recipeDao.readCnt(menu_no);
					
					//조회수 증가에 성공하면
					if(r>0) {
						//menuInfo와 list를 data라는 이름의 map에 담기
						data.put("menuInfo", menuInfo);
						data.put("recipe", list);
						
						map.put("recipe", "success");
						map.put("data", data);
						map.put("size", list.size());
					}else {		//조회수 증가에 실패하면
						map.put("recipe", "fail");
						map.put("error", "readCnt Error");
					}
				}else {		//리스트에 데이터가 없으면
					//조회수 증가 안하고 그냥 데이터 보내기
					map.put("recipe", "success");
					map.put("data", list);
					map.put("size", list.size());
				}
			}else {			//해당하는 번호의 메뉴정보가 없으면
				map.put("recipe", "fail");
				map.put("error", "Invalid menu_no");
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
