<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

<title>Insert title here</title>
</head>

<body>



	<form action="test.jsp" method="post">
			이름:<input type="text" name="name"><br> 
			나이:<input type="text" name="age"><br> 
			성별:<input type="radio"name="gender" value="1">남 
			   <input type="radio" name="gender" value="2">여<br>
			출생연도 : <select name="year">
		       <option>출생연도</option>
		       <c:forEach var="i" begin="1980" end="1999"> 
		       <option>${i}</option>
		        </c:forEach>
		</select><br> <input type="submit" value="전송">
	</form>
</body>
</html>

<%--
1. jsp 페이지내에서 스크립트(스크립트릿, 표현식)은 사용하지 않는다 => jstl과 EL로 작성하기

2. testForm.jsp 페이지의 출생년도는 1980년에서 1999년까지 나오도록 수정한다. => select 태그내의 option 태그를 jstl 로 작성할것

3.나이는 입력받은대로 출력하고, 2023년 기준 나이는 2023-출생연도 를 의미한다.

4. 수정된 testForm.jsp 와 구현한 test.jsp를 압축하여 첨부한다
--%>