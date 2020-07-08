package com.dbsg.backend.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.dbsg.backend.dao.PreferDao;
import com.dbsg.backend.domain.MenuDisplay;
import com.dbsg.backend.domain.Prefer;

@Service
public class PreferServiceImpl implements PreferService {

	@Autowired
	private PreferDao preferDao;
	
	@Autowired
	private DataSourceTransactionManager tm;
	
	//즐겨찾기 수가 높은 3개 메뉴 가져오기
	@Override
	public Map<String, Object> preferRecommend() {
		Map<String,Object> map = new HashMap<>();
		List<MenuDisplay> list = new ArrayList<>();
		
		try {
			
			list = preferDao.preferRecommend();
			
			map.put("preferRecommend", "success");
			map.put("data", list);
			map.put("size", list.size());
			
		}catch(Exception e) {
			e.printStackTrace();
			map.put("preferRecommend", "fail");
			map.put("error", e.getMessage());
		}
		
		return map;
	}

	//즐겨찾기 추가
	@Override
	public Map<String, Object> addPrefer(Prefer prefer) {
		Map<String,Object> map = new HashMap<>();
		
		DefaultTransactionDefinition def = new DefaultTransactionDefinition(); 
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = tm.getTransaction(def); 
		
		//1.해당 유저가 해당 메뉴를 즐겨찾기 한 적이 있는지 먼저 확인
		//2.없다면 즐겨찾기 테이블에 로그 등록 및 메뉴의 preferCnt 증가 작업 수행
		//3.있다면 있다고 리턴
		
		try {
			//prefer 테이블에 해당 데이터가 있는지 확인
			Prefer p = preferDao.preferCheck(prefer);
			
			//있으면
			if(p != null) {
				//있다고 보내주기
				map.put("addPrefer", "fail");
				map.put("error", "exist");
				tm.rollback(status);
				return map;
			}else {		//해당 유저가 해당 메뉴를 즐겨찾기 한 적이 없으면
				//즐겨찾기 작업 수행
				
				//즐겨찾기 테이블에 로그 등록
				int r1 = preferDao.addPrefer(prefer);
				
				//즐겨찾기 테이블에 추가 성공하면
				if(r1>0) {
					//메뉴 스탯 테이블의 preferCnt 증가
					int r2 = preferDao.addpreferCnt(prefer.getMenu_no());
					
					//preferCnt 증가까지 성공하면
					if(r2>0) {
						map.put("addPrefer", "success");
						tm.commit(status);
						return map;
					}else {			//preferCnt 증가에 실패한 경우
						map.put("addPrefer", "fail");
						map.put("error", "menu_stat의 preferCnt 증가에 실패");
						tm.rollback(status);
						return map;
					}
					
					
				}else {		//즐겨찾기 테이블에 추가 실패한 경우
					map.put("addPrefer", "fail");
					map.put("error", "즐겨찾기 테이블 추가에 실패");
					tm.rollback(status);
					return map;
				}
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			map.put("addPrefer", "fail");
			map.put("error", e.getMessage());
			tm.rollback(status);
			throw e;
		}

	}

	//즐겨찾기 삭제
	@Override
	public Map<String, Object> deletePrefer(Prefer prefer) {
		Map<String,Object> map = new HashMap<>();
		
		//1.prefer 테이블에서 해당 유저가 해당 메뉴를 즐겨찾기 한 적 있는지 확인
		//2.있으면 삭제 작업 수행
		//3.없으면 없다고 리턴
		
		DefaultTransactionDefinition def = new DefaultTransactionDefinition(); 
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = tm.getTransaction(def); 
		
		try {
			
			//로그 확인
			Prefer p = preferDao.preferCheck(prefer);
			
			//만약 로그가 없으면
			if(p == null) {
				map.put("deletePrefer", "fail");
				map.put("error", "즐겨찾기를 한적도 없네요");
				tm.rollback(status);
				return map;
			}else {		//로그가 있으면
				//즐겨찾기 테이블에서 로그 삭제
				int r1 = preferDao.deletePrefer(prefer);
				
				//즐겨찾기 테이블에서 로그 삭제에 성공하면
				if(r1>0) {
					//메뉴 스탯 테이블에서 preferCnt -1
					int r2 = preferDao.deletePreferCnt(prefer.getMenu_no());
					
					//메뉴 스탯 테이블 작업에 성공하면
					if(r2>0) {
						map.put("deletePrefer", "success");
						tm.commit(status);
						return map;
					}else {		//메뉴 스탯 테이블 작업에 실패하면
						map.put("deletePrefer", "fail");
						map.put("error", "preferCnt -1 실패");
						tm.rollback(status);
						return map;
					}
					
				}else {		//실패하면
					map.put("deletePrefer", "fail");
					map.put("error", "로그 삭제 실패");
					tm.rollback(status);
					return map;
				}
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			map.put("deletePrefer", "fail");
			map.put("error", e.getMessage());
			tm.rollback(status);
			throw e;
		}

	}

}
