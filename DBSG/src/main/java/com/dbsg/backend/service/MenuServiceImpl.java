package com.dbsg.backend.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
import com.dbsg.backend.dao.RecipeDao;
import com.dbsg.backend.domain.Menu;
import com.dbsg.backend.domain.MenuDisplay;
import com.dbsg.backend.domain.Recipe;

@Service
public class MenuServiceImpl implements MenuService {

	@Autowired
	private MenuDao menuDao;
	
	@Autowired
	private RecipeDao recipeDao;
	
	@Autowired
	private DataSourceTransactionManager tm;
	
	//메뉴,레시피 등록
	@Override
	public Map<String,Object> menuInsert(Map<String, Object> param) {
		Map<String,Object> map = new HashMap<>();		//리턴할 map
		
		DefaultTransactionDefinition def = new DefaultTransactionDefinition(); 
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = tm.getTransaction(def); 
		
		try {
			
			//param 파싱
			JSONObject jsonParam = new JSONObject(param);
			
			//menuJson과 recipeJson 뺴오기
			//해당 키가 있는지 확인하고 추출
			
			JSONObject menuJson;
			JSONArray recipeArr;
			
			if(jsonParam.has("menu")) {
				menuJson = jsonParam.getJSONObject("menu");
			}else {		//없으면 리턴
				map.put("menuInsert", "fail");
				map.put("error", "menu json이 없음");
				return map;
			}
			
			if(jsonParam.has("recipe")) {
				recipeArr = jsonParam.getJSONArray("recipe");
			}else {
				map.put("menuInsert", "fail");
				map.put("error", "recipe jsonArr이 없음");
				return map;
			}
			
			//트랜잭션 순서
			//1.MENU 테이블에 메뉴 정보 (메뉴 이름, 메뉴 태그 등) 등록
			//2.성공하면 MENU_Stat 테이블에 메뉴 번호 등록
			//3.2번도 성공하면 Recipe 테이블에 레시피 등록
			//4.모두 성공하면 true 리턴, 중간에 하나라도 실패하면 false 리턴
			
			//1.MENU 테이블에 메뉴 정보 등록
			//1-1.menuJson에서 메뉴에 대한 정보 추출
			String menu_name = "";								//메뉴 이름
			String menu_tag = "";									//메뉴 태그
			String menu_reqMaterial = "";						//필수 재료
			String menu_needlessMaterial = "";				//선택 재료
			String menu_description = "";						//매뉴 설명
			String menu_kids = "N";								//키즈 여부
			String menu_image = "";								//메뉴 사진
			int menu_totalTime = 0;								//전체 소요 시간
			
			if(menuJson.has("menu_name")) {
				menu_name = menuJson.getString("menu_name");
			}else {
				map.put("menuInsert", "fail");
				map.put("error", "there is no menu_name");
				return map;
			}
			
			if(menuJson.has("menu_tag")) {
				menu_tag = menuJson.getString("menu_tag");
			}else {
				map.put("menuInsert", "fail");
				map.put("error", "there is no menu_tag");
				return map;
			}
			
			if(menuJson.has("menu_reqMaterial")) {
				menu_reqMaterial = menuJson.getString("menu_reqMaterial");
			}else {
				map.put("menuInsert", "fail");
				map.put("error", "there is no menu_reqMaterial");
				return map;
			}
			
			if(menuJson.has("menu_needlessMaterial")) {
				menu_needlessMaterial = menuJson.getString("menu_needlessMaterial");
			}
			
			if(menuJson.has("menu_description")) {
				menu_description = menuJson.getString("menu_description");
			}
			
			if(menuJson.has("menu_kids")) {
				menu_kids = menuJson.getString("menu_kids");
			}
			
			if(menuJson.has("menu_image")) {
				menu_image = menuJson.getString("menu_image");
			}
			
			if(menuJson.has("menu_totalTime")) {
				menu_totalTime = menuJson.getInt("menu_totalTime");
			}else {
				map.put("menuInsert", "fail");
				map.put("error", "there is no menu_totalTime");
				return map;
			}
			
			//고정값
			String user_nickname = "admin";
			
			//1-2.MENU 객체에 정보 등록
			Menu menu = new Menu();
			menu.setMenu_name(menu_name);
			menu.setUser_nickname(user_nickname);
			menu.setMenu_tag(menu_tag);
			menu.setMenu_reqMaterial(menu_reqMaterial);
			menu.setMenu_needlessMaterial(menu_needlessMaterial);
			menu.setMenu_description(menu_description);
			menu.setMenu_kids(menu_kids);
			menu.setMenu_image(menu_image);
			menu.setMenu_totalTime(menu_totalTime);
			
			//System.out.println("menu "+menu.toString());
			
			//1-3.메뉴 등록
			int result1 = menuDao.menuInsert(menu);
			
			//메뉴 정보를 menu 테이블 삽입에 성공하면
			if(result1 > 0) {
				//2.MENU_Stat 테이블에 메뉴 번호 등록
				
				//2-1.menu_no 찾아오기
				Integer menu_no = menuDao.findNobyName(menu_name);
				
				//menu_no이 없으면
				if(menu_no == null) {
					map.put("menuInsert", "fail");
					map.put("error", "menu_no 찾아오는데 실패");
					tm.rollback(status); 
					return map;
				}
				//menu_no이 있으면
				else {
					//2-2.찾아온 menu_no를 MENU_Stat에 등록
					int result2 = menuDao.menuStatInsert(menu_no);
					
					//menu_no를 menu_stat 테이블 삽입에 성공하면
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
								int recipe_processNo;						//레시피 순서
								String recipe_action;							//레시피 내용
								int recipe_fire = 0;							//불세기
								int recipe_reqTime = 0;						//해당 작업을 하는데 필요한 시간
								String recipe_image = "";					//레시피 사진
								
								
								if(recipeJson.has("recipe_processNo")) {
									recipe_processNo = recipeJson.getInt("recipe_processNo");
								}else {
									map.put("menuInsert", "fail");
									map.put("error", "recipe_processNo 없음");
									return map;
								}
								
								if(recipeJson.has("recipe_action")) {
									recipe_action = recipeJson.getString("recipe_action");
								}else {
									map.put("menuInsert", "fail");
									map.put("error", "recipe_action 없음");
									return map;
								}
								
								if(recipeJson.has("recipe_fire")) {
									recipe_fire = recipeJson.getInt("recipe_fire");
								}
								
								if(recipeJson.has("recipe_reqTime")) {
									recipe_reqTime = recipeJson.getInt("recipe_reqTime");
								}
								
								if(recipeJson.has("recipe_image")) {
									recipe_image = recipeJson.getString("recipe_image");
								}
								
								//Recipe 객체에 담기
								recipe.setMenu_no(menu_no);
								recipe.setRecipe_processNo(recipe_processNo);
								recipe.setRecipe_action(recipe_action);
								recipe.setRecipe_fire(recipe_fire);
								recipe.setRecipe_reqTime(recipe_reqTime);
								recipe.setRecipe_image(recipe_image);
								
								//System.out.println("recipe "+recipe.toString());
								
								//dao호출
								menuDao.recipeInsert(recipe);
								
							}catch(Exception e) {
								e.printStackTrace();
								map.put("menuInsert", "fail");
								map.put("error", e.getMessage());
								tm.rollback(status); 
								return map;
							}
				
						}
						
						//System.out.println("모든 작업 성공!");
						
					}
					//menu_no를 menu_stat 테이블 삽입에 실패하면
					else {
						map.put("menuInsert", "fail");
						map.put("error", "menu_stat 테이블에 insert 실패");
						map.put("result", result2);
						tm.rollback(status); 
						return map;
					}
					
				}//menu_no != null 부분
				
			}
			//메뉴 정보를 menu 테이블 삽입에 실패하면
			else {
				map.put("menuInsert", "fail");
				map.put("error", "메뉴 정보 menu 테이블에 insert 실패");
				map.put("result", result1);
				tm.rollback(status); 
				return map;
			}
			
		}catch(Exception e) {
			e.printStackTrace(); 
			map.put("menuInsert", "fail");
			map.put("error", e.getMessage());
			tm.rollback(status); 
			throw e; 
		}
		
		//System.out.println("commit 하고 돌아갑시다");
		tm.commit(status);
		
		map.put("menuInsert", "success");
		
		return map;
	}

	//전체 메뉴 조회
	@Override
	public Map<String,Object> menuList() {
		Map<String,Object> map = new HashMap<>();
		List<MenuDisplay> list = new ArrayList<>();
		
		try {
			list = menuDao.menuList();
			
			map.put("menuList", "success");
			map.put("size", list.size());
			map.put("data", list);
		}catch(Exception e) {
			e.printStackTrace();
			map.put("menuList", "fail");
			map.put("error", e.getMessage());
			return map;
		}
		
		return map;
	}

	//문자열로 메뉴 검색
	@Override
	public Map<String,Object> searchMenuByString(String word) {
		Map<String,Object> map = new HashMap<>();
		
		List<MenuDisplay> list = new ArrayList<>();
		List<Integer> noList = new ArrayList<>();
		
		//받아온 keyword를 like 구문에 맞춰서 바꾸고 dao에 넣기
		//keyword에 있는 공백 무시, 특수문자 무시
		
		DefaultTransactionDefinition def = new DefaultTransactionDefinition(); 
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = tm.getTransaction(def); 
		
		try {			
			//받아온 keyword를 like 구문에 맞춰서 바꾸고 dao에 넣기
			//keyword에 있는 공백 무시, 특수문자 무시
			
			//일단 공백 무시만
			word = word.replace(" ", "");
			//System.out.println("가공된 1차 keyword는 "+keyword);
			
			//like 구문에 맞게 모양 내기
			String keyword = "%"+word+"%";
			//System.out.println("최종 db에 들어갈 값은 "+keyword);
			
			//해당 키워드가 이름, 설명, 필수재료에 있는 메뉴의 번호만 찾아오기
			noList = menuDao.searchMenuNoByString(keyword);
			
			//noList의 결과가 있을 때
			if(noList != null) {
				//noList를 돌면서 메뉴 번호로 해당 메뉴의 정보 찾아오기
				for(int i=0;i<noList.size();i++) {
					int menuNo = noList.get(i);
					
					//번호로 메뉴 정보 찾아오기
					MenuDisplay temp = menuDao.searchMenuInfoByNo(menuNo);
				
					//찾아외서 list에 추가
					list.add(temp);
				}
				
			}
		
		}catch(Exception e) {
			e.printStackTrace(); 
			System.out.println(e.getMessage());
			map.put("menuList", "fail");
			map.put("error", e.getMessage());
			tm.rollback(status); 
			throw e; 
		}
		
		map.put("menuList", "success");
		map.put("data", list);
		map.put("size", list.size());
		
		tm.commit(status);
		
		return map;
	}

	//태그로 메뉴 검색
	@Override
	public Map<String,Object> searchMenuByTag(int tagNo) {
		Map<String,Object> map = new HashMap<>();
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
		try {
			List<MenuDisplay> tempList = menuDao.searchMenuByTag(tag);
			
			//만약 찾아온 데이터가 있으면 작업 수행
			if(tempList.size() != 0) {
				//2.tempList를 돌면서 menu_tag를 빼서 int 배열에 넣은 후 tagNo과 비교
				
				//tempList를 돌면서
				for(MenuDisplay tempMD:tempList) {
					//menu_tag 가져오기
					String menu_tag = tempMD.getMenu_tag();
					
					//menu_tag를 , 로 구분해서 String 배열에 담기
					String[] tempTags = menu_tag.split(",");
					
					//tempTags 배열을 int 배열로 변경
					int[] tags = Arrays.stream(tempTags).mapToInt(Integer::parseInt).toArray();
					
					//tags 배열을 돌면서 tagNo와 일치하는 수가 있는지 확인
					for(int i=0;i<tags.length;i++) {
						
						//일치하는게 있으면 해당 MenuDisplay 변수(tempMD)를 list에 넣기
						if(tags[i] == tagNo) {
							list.add(tempMD);
						}
					}
				}

			}
			
		}catch(Exception e) {
			e.printStackTrace();
			map.put("tagSearch", "fail");
			map.put("error", e.getMessage());
		}
		
		map.put("tagSearch", "success");
		map.put("size", list.size());
		map.put("data", list);
		
		return map;
	}

	//조회수로 메뉴 추천
	@Override
	public Map<String, Object> menuRecommendByreadCnt() {
		Map<String,Object> map = new HashMap<>();
		List<MenuDisplay> list = new ArrayList<>();
		
		try {
			list = menuDao.menuRecommendByreadCnt();
			
			map.put("data", list);
			map.put("readCntRecommend", "success");
			map.put("size", list.size());
			
		}catch(Exception e) {
			e.printStackTrace();
			map.put("readCntRecommend", "fail");
			map.put("error", e.getMessage());
		}
		
		return map;
	}

	//메뉴 삭제
	@Override
	public Map<String, Object> deleteMenu(int menu_no) {
		Map<String,Object> map = new HashMap<>();
		
		//외래키 설정으로 menu 테이블에서 지우면 다 지워짐
		int result = menuDao.deleteMenu(menu_no);
		
		//성공하면
		if(result>0) {
			map.put("deleteMenu", "success");
		}else {		//실패하면
			map.put("deleteMenu", "fail");
		}
		
		return map;
	}

	//메뉴 별점
	@Override
	public Map<String, Object> menuStar(Map<String, Object> param) {
		Map<String, Object> map = new HashMap<>();
		
		//1.데이터 파싱
		//1-1.필요 데이터 : 메뉴 번호, 해당 메뉴의 현재 별점, 추가될 별점
		
		
		return map;
	}

}
