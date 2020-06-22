package com.dbsg.backend.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.dbsg.backend.dao.MenuDao;
import com.dbsg.backend.domain.Menu;
import com.dbsg.backend.domain.MenuDisplay;
import com.dbsg.backend.domain.Recipe;

@Service
public class MenuServiceImpl implements MenuService {

	@Autowired
	private MenuDao menuDao;
	
	@Autowired
	private DataSourceTransactionManager tm;
	
	//메뉴,레시피 등록
	@Override
	public boolean menuInsert(Map<String, Object> param) {
		boolean result = false;
		
		DefaultTransactionDefinition def = new DefaultTransactionDefinition(); 
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = tm.getTransaction(def); 
		
		try {
			
			//param 파싱
			JSONObject jsonParam = new JSONObject(param);
			
			//menuJson과 recipeJson 뺴오기
			JSONObject menuJson = jsonParam.getJSONObject("menu");
			JSONArray recipeArr = jsonParam.getJSONArray("recipe");
			
			//트랜잭션 순서
			//1.MENU 테이블에 메뉴 정보 (메뉴 이름, 메뉴 태그 등) 등록
			//2.성공하면 MENU_Stat 테이블에 메뉴 번호 등록
			//3.2번도 성공하면 Recipe 테이블에 레시피 등록
			//4.모두 성공하면 true 리턴, 중간에 하나라도 실패하면 false 리턴
			
			//1.MENU 테이블에 메뉴 정보 등록
			//1-1.menuJson에서 메뉴에 대한 정보 추출
			//menu_name,menu_writer,menu_tag,menu_reqMaterial,menu_needlessMaterial,menu_description,menu_kids,menu_image,menu_totalTime
			String menu_name = menuJson.getString("menu_name");
			String menu_tag = menuJson.getString("menu_tag");
			String menu_reqMaterial = menuJson.getString("menu_reqMaterial");
			String menu_needlessMaterial = menuJson.getString("menu_needlessMaterial");
			String menu_description = menuJson.getString("menu_description");
			String menu_kids = menuJson.getString("menu_kids");
			String menu_image = menuJson.getString("menu_image");
			int menu_totalTime = Integer.parseInt(menuJson.getString("menu_totalTime"));
			
			String menu_writer = "admin";
			int menu_category = 0;
			
			//System.out.println("menu_name는 "+menu_name);
			//System.out.println("menu_writer는 "+menu_writer);
			//System.out.println("menu_tag는 "+menu_tag);
			//System.out.println("menu_reqMaterial는 "+menu_reqMaterial);
			//System.out.println("menu_needlessMaterial는 "+menu_needlessMaterial);
			//System.out.println("menu_description는 "+menu_description);
			//System.out.println("menu_kids는 "+menu_kids);
			//System.out.println("menu_image는 "+menu_image);
			//System.out.println("menu_totalTime는 "+menu_totalTime);
			
			//1-2.MENU 객체에 정보 등록
			Menu menu = new Menu();
			menu.setMenu_name(menu_name);
			menu.setMenu_writer(menu_writer);
			menu.setMenu_category(menu_category);
			menu.setMenu_tag(menu_tag);
			menu.setMenu_reqMaterial(menu_reqMaterial);
			menu.setMenu_needlessMaterial(menu_needlessMaterial);
			menu.setMenu_description(menu_description);
			menu.setMenu_kids(menu_kids);
			menu.setMenu_image(menu_image);
			menu.setMenu_totalTime(menu_totalTime);
			
			//1-3.메뉴 등록
			int result1 = menuDao.menuInsert(menu);
			
			//성공하면
			if(result1 > 0) {
				//2.MENU_Stat 테이블에 메뉴 번호 등록
				
				//2-1.menu_no 찾아오기
				Integer menu_no = menuDao.findNobyName(menu_name);
				
				//menu_no이 없으면
				if(menu_no == null) {
					result = false;
					tm.rollback(status); 
					return result;
				}
				//menu_no이 있으면
				else {
					//2-2.찾아온 menu_no를 MENU_Stat에 등록
					int result2 = menuDao.menuStatInsert(menu_no);
					
					//성공하면
					if(result2 > 0) {
						//3.recipe 등록
						
						//3-1.Recipe 객체 생성
						Recipe recipe = new Recipe();
						
						//3-2.recipeArr의 개수만큼 recipe 정보를 Recipe 객체에 정보를 담고 dao도 호출
						for(int i=0;i<recipeArr.length();i++) {
							try {
								//순서대로 하나씩 JSONObject로 추출
								JSONObject recipeJson = recipeArr.getJSONObject(i);
								
								//레시피 정보 추출
								int recipe_processNo = Integer.parseInt(recipeJson.getString("recipe_processNo"));
								String recipe_action = recipeJson.getString("recipe_action");
								Integer recipe_fire = Integer.parseInt(recipeJson.getString("recipe_fire"));
								Integer recipe_reqTime = Integer.parseInt(recipeJson.getString("recipe_reqTime"));
								String recipe_image = recipeJson.getString("recipe_image");
								
								//System.out.println("menu_no는 "+menu_no);
								//System.out.println("recipe_processNo는 "+recipe_processNo);
								//System.out.println("recipe_action는 "+recipe_action);
								//System.out.println("recipe_fire는 "+recipe_fire);
								//System.out.println("recipe_reqTime는 "+recipe_reqTime);
								//System.out.println("recipe_image는 "+recipe_image);
								
								//Recipe 객체에 담기
								recipe.setMenu_no(menu_no);
								recipe.setRecipe_processNo(recipe_processNo);
								recipe.setRecipe_action(recipe_action);
								recipe.setRecipe_fire(recipe_fire);
								recipe.setRecipe_reqTime(recipe_reqTime);
								recipe.setRecipe_image(recipe_image);
								
								//dao호출
								menuDao.recipeInsert(recipe);
								
							}catch(Exception e) {
								e.printStackTrace();
								result = false;
								tm.rollback(status); 
								return result;
							}
				
						}
						
						//System.out.println("모든 작업 성공!");
						
					}
					//result2가 실패하면
					else {
						result = false;
						tm.rollback(status); 
						return result;
					}
					
				}//menu_no != null 부분
				
			}
			//result1이 실패하면
			else {
				result = false;
				tm.rollback(status); 
				return result;
			}
			
		}catch(Exception e) {
			e.printStackTrace(); 
			tm.rollback(status); 
			throw e; 
		}
		
		result = true;
		//System.out.println("commit 하고 돌아갑시다");
		tm.commit(status);
		
		return result;
	}

	//전체 메뉴 조회
	@Override
	public List<MenuDisplay> menuList() {
		List<MenuDisplay> list = new ArrayList<>();
		
		list = menuDao.menuList();
		
		return list;
	}

	//문자열로 메뉴 검색
	@Override
	public List<MenuDisplay> searchMenuByString(String keyword) {
		List<MenuDisplay> list = new ArrayList<>();
		
		//받아온 keyword를 like 구문에 맞춰서 바꾸고 dao에 넣기
		//keyword에 있는 공백 무시, 특수문자 무시
		
		//일단 공백 무시만
		keyword = keyword.replace(" ", "");
		System.out.println("가공된 1차 keyword는 "+keyword);
		
		//like 구문에 맞게 모양 내기
		String menu_name = "%"+keyword+"%";
		System.out.println("최종 db에 들어갈 값은 "+menu_name);
		
		//dao에 넣기
		list = menuDao.searchMenuByString(menu_name);
		
		return list;
	}

	//태그로 메뉴 검색
	@Override
	public List<MenuDisplay> searchMenuByTag(int tagNo) {
		List<MenuDisplay> list = new ArrayList<>();
		
		//System.out.println("처음 온 tagNo는 "+tagNo);
		
		//1.태그 번호를 like에 맞는 형식으로 변환해서 결과를 받아온다
		//2.menu_tag를 int 배열에 , 를 구분자로 구분해서 저장
		//3.int 배열을 돌면서 tagNo와 같은 숫자가 있는 menu_tag의 메뉴만 list에 저장해서 리턴
		
		//1.tagNo를 형식에 맞게 바꿔서 dao에 넣기
		
		//형식 맞추기
		String tag = "%"+tagNo+"%"; 
		//System.out.println("dao에 들어갈 모양은 "+tag);
		
		//dao 넣기
		List<MenuDisplay> tempList = menuDao.searchMenuByTag(tag);
		
		//만약 찾아온 데이터가 없으면 그냥 리턴
		if(tempList == null) {
			return list;
		}
		//찾아온 데이터가 있으면
		else if(tempList != null) {
			//2.tempList를 돌면서 menu_tag를 빼서 int 배열에 넣은 후 tagNo과 비교
			
			//tempList를 돌면서
			for(MenuDisplay tempMD:tempList) {
				//menu_tag 가져오기
				String menu_tag = tempMD.getMenu_tag();
				
				//menu_tag를 , 로 구분해서 String 배열에 담기
				String[] tempTags = menu_tag.split(",");
				
				//tempTags 배열을 int 배열로 변경
				int[] tags = Arrays.stream(tempTags).mapToInt(Integer::parseInt).toArray();
				
				//다시 tags 배열을 돌면서 tagNo와 일치하는 수가 있는지 확인
				for(int i=0;i<tags.length;i++) {
					
					//일치하는게 있으면 해당 MenuDisplay 변수(tempMD)를 list에 넣기
					if(tagNo == tags[i]) {
						list.add(tempMD);
					}
				}
			}
		}
		
		return list;
	}

	//테스트
	@Override
	public boolean test(String menu_name) {
		boolean result = false;
		
		Integer menu_no = menuDao.findNobyName(menu_name);
		System.out.println(menu_no);
		if(menu_no == null) {
			System.err.println("해당하는 메뉴 번호가 없어영 ㅠㅠ");
			menu_no = 0;
		}
		System.out.println(menu_no);
		
		
		return result;
	}
}
