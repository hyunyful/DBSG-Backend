package com.dbsg.backend.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dbsg.backend.domain.User;
import com.dbsg.backend.domain.UserDisplay;

@Repository
public class UserDao {

	@Autowired
	private SqlSession sqlSession;
	
	//해당 정보로 이전에 가입한 적이 있는지 확인
	public UserDisplay userCheck(String email) {
		return sqlSession.selectOne("user.userCheck", email);
	}
	
	//회원가입 진행
	public int userJoin(User user) {
		return sqlSession.insert("user.userJoin", user);
	}
	
	//닉네임 중복검사
	public String nicknameCheck(String nickname) {
		return sqlSession.selectOne("user.nicknameCheck",nickname);
	}
	
	//닉네임 설정
	public int setNickname(User user) {
		return sqlSession.update("user.setNickname", user);
	}
	
	//회원 번호로 회원 정보 얻어오기
	public UserDisplay findInfoByNo(int user_no) {
		return sqlSession.selectOne("user.findInfoByNo", user_no);
	}
	
	//회원 정보로 회원 번호 얻어오기
	public int findNoByInfo(String email) {
		return sqlSession.selectOne("user.findNoByInfo",email);
	}
}
