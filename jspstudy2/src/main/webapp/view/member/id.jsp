<%@page import="model.Member"%>
<%@page import="model.MemberDao"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>아이디 찾기</title>
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js">
	<script type="text/javascript">
		function idsend(id) {
			  opener.document.f.id.value=id; 
			   self.close();
		}
 	</script>
</head>
<body>
	<div class="container">
		<table class="table">
			<tr>
		  		<th>아이디</th>
		    	<td>${id}</td>
		    </tr>
		  	<tr>
		  		<td colspan="2">
		     	<input type="button" value="아이디 전송" onclick="idsend('${id}');" class="btn btn-dark">
		  		</td>
		  	</tr>
		</table>
	</div>
</body>
</html>

