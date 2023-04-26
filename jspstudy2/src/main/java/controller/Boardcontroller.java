package controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import javax.security.auth.message.callback.PrivateKeyCallback.Request;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;

import org.apache.taglibs.standard.lang.jstl.test.beans.PublicBean1;
import org.eclipse.jdt.internal.compiler.ast.ReturnStatement;

import com.oreilly.servlet.MultipartRequest;

import gdu.mskim.MskimRequestMapping;
import gdu.mskim.RequestMapping;
import model.Board;
import model.BoardMybatsDao;
import model.CommentDao;
import model.Comment;


//http://localhost:8080/jspstudy2/board/writeForm
@WebServlet(urlPatterns = { "/board/*" }, initParams = { @WebInitParam(name = "view", value = "/view/") })
public class Boardcontroller extends MskimRequestMapping {
	private BoardMybatsDao dao = new  BoardMybatsDao();
    private CommentDao cdao = new CommentDao();
	
	@RequestMapping("writeForm")
	public String writeForm(HttpServletRequest request, HttpServletResponse response) {
		String boardid = (String) request.getSession().getAttribute("boardid");
		if (boardid == null)
			boardid = "1";
		String login = (String) request.getSession().getAttribute("login");
		if (boardid.equals("1")) {
			if (login == null || !login.equals("admin")) {
				request.setAttribute("msg", "관리자만 공지사항 글쓰기가 가능합니다.");
				request.setAttribute("url", request.getContextPath() + "/board/list?boardid=" + boardid);
				return "alert";
			}
		}
		return "board/writeForm";
	}

	@RequestMapping("write")
	public String write(HttpServletRequest request, HttpServletResponse response) {
		String path = request.getServletContext().getRealPath("/") + "/upload/";
		File f = new File(path);
		if (!f.exists())
			f.mkdirs();
		int size = 10 * 1024 * 1024;
		MultipartRequest multi = null;
		try {
			multi = new MultipartRequest(request, path, size, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		Board board = new Board();
		board.setWriter(multi.getParameter("writer"));
		board.setPass(multi.getParameter("pass"));
		board.setTitle(multi.getParameter("title"));
		board.setContent(multi.getParameter("content"));
		board.setFile1(multi.getFilesystemName("file1"));
		String boardid = (String) request.getSession().getAttribute("boardid");
		if (boardid == null)
			boardid = "1";
		board.setBoardid(boardid);
		if (board.getFile1() == null)
			board.setFile1("");
		int num = dao.maxnum();
		board.setNum(++num);
		board.setGrp(num);
		String msg = "게시물 등록 실패";
		String url = "writeForm";
		if (dao.insert(board)) {
			return "redirect:list?boardid=" + boardid;
		}
		request.setAttribute("msg", msg);
		request.setAttribute("url", url);
		return "alert";
	}

//http://localhost:8080/jspstudy2/board/list?boardid=1
	/*
	 * 1. 한페이지당 10건의 게시물을 출력하기. pageNum 파라미터값을 저장 => 없는 경우는 1로 설정하기. 2. 최근 등록된 게시물이
	 * 가장 위에 배치함. 3. db에서 해당 페이지에 출력될 내용을 조회하여 화면에 출력. 게시물을 출력부분. 페이지 구분 출력 부분 4.
	 * 페이지별 게시물번호 출력하기(boardnum) 5. 첨부파일 등록하면 글제목 앞에 @ 표시하기 없으면 공백3개
	 */
	@RequestMapping("list")
	public String list(HttpServletRequest request, HttpServletResponse response) {
		
		     try {
				request.setCharacterEncoding("UTF-8");
			} catch (UnsupportedEncodingException e1) {
			
				e1.printStackTrace();
			}
		if (request.getParameter("boardid") != null) {
			// list에 boardid 라는 파라미터가 존재하니?
			// session에 게시판 종류 정보 등록
			request.getSession().setAttribute("boardid", request.getParameter("boardid"));
			request.getSession().setAttribute("pageNum", "1"); // 현재페이지 번호
		}
		String boardid = (String) request.getSession().getAttribute("boardid");
		if (boardid == null)
			boardid = "1"; // 파라미터에도 없고 세션에도 없으면 1로 하자
		int pageNum = 1; // 페이지 넘버 1로 설정
		try {
			pageNum = Integer.parseInt(request.getParameter("pageNum"));
		} catch (NumberFormatException e) {} // 페이지 넘버 1 로 할거다 -> 오류 무시할거다
           String column = request.getParameter("column");
           String find = request.getParameter("find");
           /*
            * column,find 파라미터 중 한개만 존재하는 경우 두개의 파라미터 값은 
            * 없는 상태로 설정 
            * 
            */
           if(column == null || column.trim().equals("")) {
               column = null;
               find = null;
	}
           if(find == null || find.trim().equals("")) {
               column = null;
               find = null;
	}
		int limit = 10; // 한페이지에 보여질 게시물 건수 > 한페이지에 10 개만 보여줄거다
		// boardcount : 게시물종류별 게시물 등록 건수
		int boardcount = dao.boardCount(boardid,column,find); // 게시판 종류별 전체 게시물등록 건수 리턴
		// list : 현재 페이지에 보여질 게시물 목록.
		List<Board> list = 
				dao.list(boardid, pageNum, limit ,column, find);

		int maxpage = (int) ((double) boardcount / limit + 0.95);

		int startpage = ((int) (pageNum / 10.0 + 0.9) - 1) * 10 + 1;
		// endpage : 화면에 출력할 마지막 페이지 번호. 한 화면에 10개의 페이지를 보여줌
		int endpage = startpage + 9;
		// endpage 는 maxpage를 넘어가면 안됨
		if (endpage > maxpage)
			endpage = maxpage;
		// boardName : 게시판 이름 화면에 출력
		String boardName = "공지사항";
		switch (boardid) {
		case "2":
			boardName = "자유게시판";
			break;
		case "3":
			boardName = "QNA";
			break;
		}

		int boardnum = boardcount - (pageNum - 1) * limit;
		request.setAttribute("boardName", boardName);
		request.setAttribute("boardcount", boardcount);
		request.setAttribute("boardid", boardid);
		request.setAttribute("pageNum", pageNum);
		request.setAttribute("boardnum", boardnum);
		request.setAttribute("list", list);
		request.setAttribute("startpage", startpage);
		request.setAttribute("endpage", endpage);
		request.setAttribute("maxpage", maxpage);
		request.setAttribute("today", new Date());
		return "board/list";

	}

	@RequestMapping("info")
	public String info(HttpServletRequest request, HttpServletResponse response) {
		int num = Integer.parseInt(request.getParameter("num"));
		String readcnt = request.getParameter("readcnt");
		String boardid = (String) request.getSession().getAttribute("boardid");
		if (boardid == null)
			boardid = "1";
		Board b = dao.selectOne(num);
		if(readcnt == null ||!readcnt.equals("f"))
		dao.readcntAdd(num);
		
		request.setAttribute("b", b);
		request.setAttribute(boardid, boardid);
		String boardName = "공지사항";
		switch (boardid) {
		case "2":
			boardName = "자유게시판";
			break;
		case "3":
			boardName = "QNA";
			break;
		}
		request.setAttribute("boardName", boardName);
		//댓글 목록 읽어서 화면에 전달
		List<Comment> commlist = cdao.list(num);//num 게시글 번호
		request.setAttribute("commlist", commlist);
		return "board/info";

	}

	/*
	 * 1.원글의 num을 파라미터 저장 : num 원글의 게시물 번호 2.db에서 num의 게시물을 조회하여 원글의
	 * num,grp,grplevel,gepstep 정보를 저장 3.입력 화면 표시
	 * 
	 */
	@RequestMapping("replyForm")
	public String replyForm(HttpServletRequest request, HttpServletResponse response) {
		int num = Integer.parseInt(request.getParameter("num"));// 파라미터값 읽기
		Board board = dao.selectOne(num);
		request.setAttribute("board", board);
		return "board/replyForm";

	}

	/*
	 * 1.파라미터 값을 Board 객체에 저장하기 원글 정보 : num,grp,grplevel,gepstep,boardid 답글 정보 :
	 * writer, pass, title, content = > 등록정보 2.같은 grp 값을 사용하는 게시물의 grpstep 값을 1 증가
	 * 하기. void BoardDao.grpStrpAdd(grp,grpstep) 3.Board 객체를 db에 insert 하기 num :
	 * maxnum +1 grp : 원글과 동일 grplevel : 원글 +1 grpstep : 원글 +1 boardid : 원글과 동일 4.등록
	 * 성공 시 :list.jsp 페이지 이동 등롤 실패 시 :"답변 등록시 오류 발생" 메세지지 출력 후 replyForm.jsp 페이지로 이동
	 */
	@RequestMapping("reply")
	public String reply(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Board b = new Board(); // 답글에 저장될
		b.setWriter(request.getParameter("writer"));
		b.setPass(request.getParameter("pass"));
		b.setTitle(request.getParameter("title"));
		b.setContent(request.getParameter("content"));
		b.setBoardid(request.getParameter("boardid"));
		b.setGrp(Integer.parseInt(request.getParameter("grp")));
		b.setGrplevel(Integer.parseInt(request.getParameter("grplevel")));
		b.setGrpstep(Integer.parseInt(request.getParameter("grpstep")));
//2
		dao.grpStepAdd(b.getGrp(), b.getGrpstep());// grpstep 변경
//3 답글 등록
		int grplevel = b.getGrplevel();
		int grpstep = b.getGrpstep();
		int num = dao.maxnum();
		String msg = "답변 등록시 오류 발생";
		String url = "replyForm.jsp?num=" + b.getNum();
		b.setNum(++num);// 답글에 대한 정보
		b.setGrplevel(grplevel + 1);// 원글 grplevel 보다 하나 더 커야 함 => 답글 데이터
		b.setGrpstep(grpstep + 1);// 원글 grpstep 보다 하나 더 커야 됨 => 답글 데이터
		if (dao.insert(b)) { // b라는 객체 만들어서 insert
			return "redirect:list?boardid=" + b.getBoardid();
		}
		request.setAttribute("msg", msg);
		request.setAttribute("url", url);
		return "alert";
	}

	/*
	 * 1.공지사항인 경우 관리자만 수정 가능 2.num값에 해당하는 게시물을 조회 3.조회된 게시물을 화면에 출력
	 * 
	 */
	@RequestMapping("updateForm")
	public String updateForm(HttpServletRequest request, HttpServletResponse response) {
		String boardid = (String) request.getSession().getAttribute("boardid");
		if (boardid == null) boardid = "1";
		String login = (String) request.getSession().getAttribute("login");
		String msg = "관리자만 수정 가능합니다.";
		String url = "list?boardid=1";
		
		if (boardid.equals("1")) 
			if (login == null || !login.equals("admin")) {
				request.setAttribute("msg", msg);
				request.setAttribute("url", url);
				return "alert";
			}
			int num = Integer.parseInt(request.getParameter("num"));
			Board b = dao.selectOne(num);
			String boardName ="공지사항";
			switch (boardid) {
			case "2": 
				    boardName = "자유게시판"; break;
			case "3": 
					boardName=  "QNA"; break;
				
			}
				request.setAttribute("boardName", boardName);
				request.setAttribute("b", b);
				
				return "board/updateForm";
			}

	@RequestMapping("update")
	public String update(HttpServletRequest request, HttpServletResponse response) {
		Board board = new Board();
		String path = request.getServletContext().getRealPath("/") + "view/board/file";
		File f = new File(path);
		if (!f.exists())
			f.mkdirs();
		MultipartRequest multi=null;
		try {
			multi = new MultipartRequest(request, path, 10 * 1024 * 1024, "UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		board.setNum(Integer.parseInt(multi.getParameter("num")));
		board.setWriter(multi.getParameter("writer"));
		board.setPass(multi.getParameter("pass"));
		board.setTitle(multi.getParameter("title"));
		board.setContent(multi.getParameter("content"));
		board.setFile1(multi.getFilesystemName("file1"));
		if (board.getFile1() == null || board.getFile1().equals("")) {
			board.setFile1(multi.getParameter("file2"));
		}
		
		Board dbBoard = dao.selectOne(board.getNum());
		String msg = "비밀번호가 틀렸습니다";
		String url = "updateForm?num=" + board.getNum();
		if (board.getPass().equals(dbBoard.getPass())) {
			// 3번
			if (dao.update(board)) {
				url = "info?num=" + board.getNum();
				return "redirect:" +url;

			} else {
				msg = "게시물 수정 실패";
			}
		}
		request.setAttribute("msg", msg);
		request.setAttribute("url", url);
		return "alert";
	}

	@RequestMapping("deleteForm")
	public String deleteForm(HttpServletRequest request, HttpServletResponse response) {
		String boardid = (String) request.getSession().getAttribute("boardid");
		if (boardid == null)
			boardid = "1";
		String login = (String) request.getSession().getAttribute("login");
		if (boardid.equals("1")) {
			if (login == null || !login.equals("admin")) {
				request.setAttribute("msg", "관리자만 삭제 가능합니다.");
				request.setAttribute("url", "list?boardid=1.");
				return "alert";
			}
		}
		return "board/deleteForm";
	}

	@RequestMapping("delete")
	public String delete(HttpServletRequest request, HttpServletResponse response) {
		int num = Integer.parseInt(request.getParameter("num"));
		String pass = request.getParameter("pass"); // 입력된 비밀번호
		Board board = dao.selectOne(num); // db의 비밀번호
		String login = (String) request.getSession().getAttribute("login");
		String msg = "비밀번호가 틀렸습니다";
		String url = "daleteForm?num=" + num;
		if (pass.equals(board.getPass())) {
			if (board.getBoardid().equals("1") && (login == null || !login.equals("admin"))) {
				msg = "공지사항은 관리자만 삭제 가능합니다.";
				url = "list?boardid=" + board.getBoardid();
			} else { // 정상적인 삭제 권한
				if (dao.delete(num)) {
					msg = "삭제되었습니다.";
					url = "list?boardid=" + board.getBoardid();
				} else { // 삭제실패
					msg = "게시글 삭제 실패";
					url = "info.?num=" + num;
				}
			}

		}
		request.setAttribute("msg", msg);
		request.setAttribute("url", url);
		return "alert";
		
	}
	@RequestMapping("imgupload")
	public String imgupload(HttpServletRequest request, HttpServletResponse response) {
	String path = request.getServletContext().getRealPath("/") 
			+ "/upload/imgfile/";
	File f = new File(path);
	if (!f.exists())f.mkdirs();
	int size = 10 * 1024 * 1024;
	MultipartRequest multi = null;
	try {
		multi = new MultipartRequest(request, path, size, "UTF-8");
	} catch (IOException e) {
		e.printStackTrace();
	}
	//ckeditord에서 file 이름이 upload임
	String fileName = multi.getFilesystemName("upload"); //ckeditor가 파일 이름을 upload로 전달해준다는 뜻 우리가 설정하는 게 아님(고정)
	request.setAttribute("fileName",fileName);
	return "ckeditor"; 
	   
	  }
		@RequestMapping("comment")
		public String comment(HttpServletRequest request, 
				HttpServletResponse response) {
			try {
				request.setCharacterEncoding("UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			//num : 게시글 번호
			int num = Integer.parseInt(request.getParameter("num"));// 파라미터값 읽기
			String url = "info?num="+num+"&readcnt=f";
			//파라미터 값 Comment 객체에 저장
			Comment comm = new Comment();
			comm.setNum(num);
			comm.setWriter(request.getParameter("writer"));
			comm.setContent(request.getParameter("content"));
			int seq = cdao.maxseq(num); //num에 해당하는 최대 seq 컬럼의 값
			comm.setSeq(++seq); //현재 최대 값보다 하나 더 커야 중복이 안 됨
			if (cdao.insert(comm)) { //comment 테이블에 insert
				return "redirect:"+url;
			}
			request.setAttribute("msg", "답글 등록 시 오류발생");
			request.setAttribute("url", url);
			return "alert";
		}
		
		@RequestMapping("commdel")
		public String commdel(HttpServletRequest request, 
				HttpServletResponse response) {
			int num = Integer.parseInt(request.getParameter("num"));
			int seq = Integer.parseInt(request.getParameter("seq"));
			String url = "info?num="+num+"&readcnt=f";
			if (cdao.delete(num,seq)) {
				return "redirect:" + url;
			}
			request.setAttribute("msg","답글 삭제 시 오류 발생");
			request.setAttribute("url", url);
			return "alert";
			}
	
			
				
		}
		
	
	