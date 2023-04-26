<%@page import="model.BoardDao"%>
<%@page import="com.oreilly.servlet.MultipartRequest"%>
<%@page import="java.io.File"%>
<%@page import="model.Board"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%--
  1.파라미터 정보들을 board 객체 저장
  2.비밀번호 검증
    -비밀번호 오류 메시지 출력 후 updateForm.jsp로 이동
  3.비밀번호 일치 시 수정
       첨부파일의 변경이 없는 경우 file2 파라미터의 내용을 다시 저장하기
       파라미터의 냐용으로 해당 게시물의 내용을 수정하기.
       boolean BoardDao.update(Board)
          수정 성공 info.jsp 페이지 이동
          수정 실패 : 수정실패 메세지 출력 후 updateForm.jsp 페이지 이동
  --%>   
<%
// String board =(String)session.setAttribute(board, doardid);

Board board = new Board();
String path = application.getRealPath("/") + "model1/board/file";
File f = new File(path);
if(!f.exists()) f.mkdirs();
MultipartRequest multi = new MultipartRequest 
         (request,path,10*1024*1024,"UTF-8");
board.setNum(Integer.parseInt(multi.getParameter("num")));
board.setWriter(multi.getParameter("writer"));
board.setPass(multi.getParameter("pass"));
board.setTitle(multi.getParameter("title"));
board.setContent(multi.getParameter("content"));
board.setFile1(multi.getFilesystemName("file1"));
if(board.getFile1()==null || board.getFile1().equals("")) {
	 board.setFile1(multi.getParameter("file2"));
}

//2비밀번호 검증
BoardDao dao =  new BoardDao();
Board dbBoard = dao.selectOne(board.getNum());
String msg = "비밀번호가 틀렸습니다";
String url = "updateForm.jsp?num="+board.getNum();
if(board.getPass().equals(dbBoard.getPass())) {
	//3번
	 if(dao.update(board)) {
		 url ="info.jsp?num="+board.getNum();
		 response.sendRedirect(url);
		 
	 }else {
		 msg="게시물 수정 실패";
	 }
}
%>
<script>
 alert("<%=msg%>");
 location.href="<%=url%>";
</script> 
