<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
       <script>
    if($dao.insert(board)}) {
    	response.sendRedirect("$(list)");
    }else
      alert("게시물 등록 실패")
      location.href="writeForm.jsp" 
   </script>
  
   