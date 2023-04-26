<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html><html><head>
<meta charset="UTF-8">
<title>게시글 삭제</title>
</head>
<body>
<h2>게시글 삭제</h2>
<form action="delete" method="post">
   <input type="hidden" name="num" 
       value="${param.num}">
   <label>Password:</label>
   <input type="password" class="form-control" name="pass">
   <div id="w-3center" style="padding:3px;">
     <button type="submit">게시물삭제</button>
   </div>
</form>
</body>
</html>