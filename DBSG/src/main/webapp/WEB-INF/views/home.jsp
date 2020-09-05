<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="true" %>
<html>
<head>
	<title>Home</title>
</head>
<script
  src="https://code.jquery.com/jquery-1.12.4.min.js"
  integrity="sha256-ZosEbRLbNQzLpnKIkEdrPv7lOy9C27hHQ+Xp8a4MxAQ="
  crossorigin="anonymous"></script>
<body>
<h1>
	Hello DBSG!  
</h1>

<button onClick="kakao()">카카오 로그인</button>
<button onClick="naver()">네이버 로그인</button><br>
<!-- <button onClick="location.href='/DBSG/controller'">컨트롤러 정보 보기</button> -->

<script>
function kakao(){
	var appKey = "51c7c8f63345a28a25a4b28fff7048ef";
	//var redirect_uri = "http://15.165.215.38:8080/DBSG/user/login/kakao";
	var redirect_uri = "http://localhost:8080/DBSG/user/login/kakao";		//로컬 테스트용
	
	var uri = "https://kauth.kakao.com/oauth/authorize?"
	uri += "client_id="+appKey+"&redirect_uri="+redirect_uri+"&scope=profile,account_email,age_range,birthday,gender";
	uri += "&response_type=code";
	
	//alert(uri);
	
	location.href=uri;
}
</script>

<script>
function naver(){
	$.ajax({
		url:"/DBSG/user/naverState",
		type:"get",
		success:function(result){
			
			var client_id = "m8QQGZXfACv5KdlFw8oI";
			var redirect_uri = "http://localhost:8080/DBSG/user/login/naver";
			//var redirect_uri = "http://15.165.215.38:8080/DBSG/user/login/naver";
			var state = result.state;
			
			//https://nid.naver.com/oauth2.0/authorize?client_id={클라이언트 아이디}&response_type=code&redirect_uri={개발자 센터에 등록한 콜백 URL(URL 인코딩)}&state={상태 토큰}
			var uri = "https://nid.naver.com/oauth2.0/authorize?";
			uri += "client_id="+client_id+"&response_type=code&redirect_uri="+redirect_uri;
			uri += "&state="+state;

			location.href=uri;
			
		}
	});
	
}
</script>

</body>
</html>
