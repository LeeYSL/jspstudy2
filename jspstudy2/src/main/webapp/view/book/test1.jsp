<!--  /jspstudy2/src/main/webapp/view/test/test1.jsp
    1. testForm.jsp 페이지에서 전달한 파라미터를 Book Bean 클래스를 이용하여 DB에 저장하기
       BookDao.java클래스를 생성하여 db에 등록하기
    2. 등록된 내용을 db에서 읽어 화면에 출력하기     
-->    

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!--  /jspstudy2/src/main/webapp/view/test/test1.jsp-->    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>방명록</title>
</head>
<body>
  <h2>방명록 등록 결과 </h2>
<table class="w3-table-all">
     <tr><td>이름</td><td>${book.writer}</td></tr>
     <tr><td>제목</td><td>${book.title}</td></tr>
     <tr><td>내용</td><td>${book.content}</td></tr></table>
</body></html>