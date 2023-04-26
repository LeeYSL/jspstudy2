<%@page import="model.Member"%>
<%@page import="model.MemberDao"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
     <%--
    1.파라미터 저장(pass,chgpass)
    2.로그인한 사용자의 비밀번호 변경만 가능 =>로그인 부분 검증
      로그아웃 상태 : 로그인 하세요. 메세지 출력 후 
                   opener 창을 loginForm.jsp 페이지로 이동 후 현재 페이지 닫기
    3.로그인 상태 : 비밀번호 검증(현재 비밀번호로 비교)
        비밀번호 오류- 비밀번호 오류 메세지 출력 후 현재 페이지가 passwordForm.jsp 페이지로 이동
    4.db에 비밀번호 수정
       boolean MemberDao.updatepass(id,변경 비밀번호)    
          -수정 성공 : 성공 메세지 출력 후
                    (로그 아웃 되었습니다. 변경 된 비밀번호로 다시 로그인 하세요) 
                   // opener 페이지 info.jsp로 이동. 현재 페이지 닫기  
                    로그아웃 후 opener 페이지를 loginForm.jsp로 이동 페이지 닫기        
          -수정 실패 : 실패 메세지 출력 후 opener 페이지 updateForm.jsp로 이동. 현재 페이지 닫기 --%>  
 <%
   
     String pass = request.getParameter("pass");
     String chgpass = request.getParameter("chgpass");
     String login =(String)session.getAttribute("login");
     String msg = null;
     String url = null;
     boolean opener = true;
     
     if(login==null){ //로그아웃 상태
    	
            
            msg="로그인 하세요";  
            url="loginForm.jsp";
            opener = true;
     } else { //로그인 상태
    	 MemberDao dao = new MemberDao();
    	 Member dbmem = dao.selectOne(login);
    	 //pass : 현재 비밀번호로 입력된 값
    	 //dbmem.getPass() : db에 등록된 비밀번호
    	 if(pass.equals(dbmem.getPass())) { //비밀번호 일치
    		  if(dao.updatePass(login,chgpass)) {
    			  msg="비밀번호 변경 후 로그아웃 되었습니다.\\n다시 로그인 하세요";
    			 // msg = "비밀번호가 변경되었습니다.";
    			  session.invalidate(); // 세션 죽이는 거
    			  url = "loginForm.jsp";
    			 // url = "info.jsp?id=" + login;
    			  opener = true;
    		  }else {
    			  msg = "비밀번호 변경 시 오류 발생";
    			  url = "updateForm.jsp?id=" + login;
    		  }
    		 
    	 }else { //비밀번호 오류
    		 opener = false;
    	     msg ="비밀번호 오류입니다.";
    	     url = "passwordForm.jsp";		 
    	 
    	 }
     }
      %>
 <script type="text/javascript">
     alert("${msg}")
     if(${opener}) {
    	 opener.location.href="${url}"
    	 self.close()
     }else {
    	 location.href="${url}"
     }
  
</script>     


    
    
   