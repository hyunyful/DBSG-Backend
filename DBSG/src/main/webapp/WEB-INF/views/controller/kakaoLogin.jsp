<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>카카오 로그인 컨트롤러 정보</title>
</head>
<body>

<h1>요청 주소 : <button onClick="show()"> 보고싶으면 클릭 (기니까 복붙 추천)</button> (get)</h1>

<p>
<b>리턴 값 정보</b><br>
로그인에 성공한 후 등록되지 않은 회원일 때<br><img src="/resources/controllerImage/kakaoLogin/info1.PNG"><br><br>
로그인에 성공한 후 등록된 회원일 때<br><img src="/resources/controllerImage/kakaoLogin/info2.PNG"><br><br>
작업도중 에러가 발생한 경우<br><img src="/resources/controllerImage/kakaoLogin/info3.PNG"><br><br>
<b>성공</b><br>
회원 데이터가 존재하는 경우<br><img src="/resources/controllerImage/kakaoLogin/exist.PNG"><br>
회원 데이터가 존재하지 않는 경우<br><img src="/resources/controllerImage/kakaoLogin/needNickname.PNG"><br><br>

<b>카카오 로그인 작업 과정</b><br>
<img src="/resources/controllerImage/kakaoLogin/ppt.png">
</p>

<script>
function show(){
	var appKey = "51c7c8f63345a28a25a4b28fff7048ef";
	var redirect_uri = "http://15.165.215.38:8080/user/login/kakao";
	
	var uri = "https://kauth.kakao.com/oauth/authorize?"
	uri += "client_id="+appKey+"&redirect_uri="+redirect_uri+"&scope=profile,account_email,age_range,birthday,gender";
	uri += "&response_type=code";
	
	alert(uri);
}
</script>

</body>
</html>