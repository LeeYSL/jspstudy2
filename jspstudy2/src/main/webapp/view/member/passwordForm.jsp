<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>비밀번호 변경</title>
<script type="text/javascript">
     function input_check(f) {
    	 if(f.pass.value.trim()=='') {
    		 alert("현재 비밀번호를 입력하세요")
    		 f.pass.focus()
    		 return false
    	 }
          if(f.chgpass.value.trim()=='') {
        	  alert("변경 비밀번호를 입력하세요")
        	  f.chgpass.focus()
        	  return false
          }
          if(f.chgpass2.value.trim()=='') {
        	  alert("변경 비밀번호를 한번 더 입력하세요")
        	  f.chgpass2.focus()
        	  return false
          }
          else if(f.chgpass.value !=f.chgpass2.value)
        	  alert("값이 동일하지 않습니다.")
        	  f.chgpass2.value="";
              f.chgpass2.focus()
        	  return false
     }



</script>
</head>
<body>
<form action="password" method="post" name="f"
      onsubmit="return inchk(this)">
      <%--1.모든 값 입력 2.변경 비밀번호==재입력 값이 동일 --%>
      <table><caption>비밀번호 변경</caption>
      <tr><th>현재비밀번호</th>
          <td><input type="password" name="pass"></td></tr>
      <tr><th>변경비밀번호</th>
          <td><input type="password" name="chgpass"></td></tr>
      <tr><th>변경비밀번호 재입력</th>
          <td><input type="password" name="chgpass2"></td></tr>
      <tr><td colspan="2">
      <input type="submit" value="비밀번호변경">      
      <input type="reset" value="초기화"></td></tr>        
      </table></form></body></html>



