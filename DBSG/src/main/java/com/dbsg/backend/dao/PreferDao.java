package com.dbsg.backend.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dbsg.backend.domain.MenuDisplay;
import com.dbsg.backend.domain.Prefer;

@Repository
public class PreferDao {

	@Autowired
	private SqlSession sqlSession;
	
	//즐겨찾기 수가 많은 3개 메뉴 추천
	public List<MenuDisplay> preferRecommend(){
		return sqlSession.selectList("prefer.preferRecommend");
	}
	
	//즐겨찾기 테이블에 해당 유저가 해당 메뉴를 추가 한적이 있는지 체크
	public Prefer preferCheck(Prefer prefer) {
		return sqlSession.selectOne("prefer.preferCheck", prefer);
	}
	
	//즐겨찾기 테이블에 즐겨찾기 로그 기록
	public int addPrefer(Prefer prefer) {
		return sqlSession.insert("prefer.addPrefer", prefer);
	}
	
	//menu_stat 테이블의 preferCnt +1
	public int addpreferCnt(int menu_no){
		return sqlSession.update("prefer.addPreferCnt", menu_no);
	}
	
	//즐겨찾기 테이블에서 로그 삭제
	public int deletePrefer(Prefer prefer) {
		return sqlSession.delete("prefer.deletePrefer", prefer);
	}
	
	//메뉴 스탯 테이블에서 preferCnt -1
	public int deletePreferCnt(int menu_no) {
		return sqlSession.update("prefer.deletePreferCnt", menu_no);
	}
}
