<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="true" %>
<html>
<head>
	<title>Home</title>
</head>
<body>
<h1>
	Hello DBSG!  
</h1>

<button onClick="kakao()">카카오 로그인</button>
<button onClick="location.href='/controller'">컨트롤러 정보 보기</button>

<script>
function kakao(){
	var appKey = "51c7c8f63345a28a25a4b28fff7048ef";
	var redirect_uri = "http://15.165.215.38:8080/user/login/kakao";
	//var redirect_uri = "http://localhost:8080/user/login/kakao";
	
	var uri = "https://kauth.kakao.com/oauth/authorize?"
	uri += "client_id="+appKey+"&redirect_uri="+redirect_uri+"&scope=profile,account_email,age_range,birthday,gender";
	uri += "&response_type=code";
	//uri += "&scope=profile,email,age_range,birthday,birthyear,gender,phone_number";
	
	//alert(uri);
	
	location.href=uri;
}
</script>

</body>
</html>
