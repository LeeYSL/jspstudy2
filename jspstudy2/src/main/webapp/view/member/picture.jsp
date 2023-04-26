<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 
 <script type="text/javascript">
    //opener : 현재 window를 open한 window 값 =>joinForm.jsp
    img = opener.document.getElementById("pic");//id 속성값이 pic라는 태그 선택
    //img 태그의 src 속성 이 순간 joinForm.jsp 페이지에 이미지 보여짐 
    img.src = "/jspstudy2/picture/${fname}" 
    opener.document.f.picture.value="${fname}";
    self.close(); //현재 창 닫기
 </script>
    
 