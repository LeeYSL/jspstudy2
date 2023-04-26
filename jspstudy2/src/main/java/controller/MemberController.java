package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;

import gdu.mskim.MSLogin;
import gdu.mskim.MskimRequestMapping;
import gdu.mskim.RequestMapping;
import model.Member;
import model.MemberMybatisDao;

///member/* => http://localhost:8080/jspstudy2/member/이후의 어떤 요청이 들어와도
//MemberController 서블릿이 호출 됨
///view/ => /jspstudy/src/main/webapp/view 폴더

@WebServlet(urlPatterns = { "/member/*" }, 
   initParams = { @WebInitParam(name = "view", value = "/view/") })

public class MemberController extends MskimRequestMapping { // MskimRequestMapping :서블릿
private MemberMybatisDao dao = new MemberMybatisDao();

//=====================================================================	
public String loginIdCheck(HttpServletRequest request, HttpServletResponse response) {
try {
request.setCharacterEncoding("UTF-8");
} catch (UnsupportedEncodingException e) {
e.printStackTrace();
}
String id = request.getParameter("id");
String login = (String) request.getSession().getAttribute("login");
if (login == null) {
request.setAttribute("msg", "로그인하세요");
request.setAttribute("url", "loginForm");
return "alert";
} else if (!login.equals("admin") && !id.equals(login)) {
request.setAttribute("msg", "본인만 거래 가능합니다.");
request.setAttribute("url", "main");
return "alert";
}
return null;

}

public String loginAdminCheck(HttpServletRequest request, HttpServletResponse response) {
String login = (String) request.getSession().getAttribute("login");
if (login == null) {
request.setAttribute("msg", "로그인하세요");
request.setAttribute("url", "loginForm");
return "alert";
} else if (!login.equals("admin")) {
request.setAttribute("msg", "본인만 거래 가능합니다.");
request.setAttribute("url", "main");
return "alert";
}
return null;

}
//=====================================================================================================		

// http://localhost:8080/jspstudy2/member/loginForm
@RequestMapping("loginForm")
public String loginForm(HttpServletRequest request, HttpServletResponse response) {
return "member/loginForm"; // view 선택
// view/member/loginForm.jsp => view 이름
}

@RequestMapping("login")
public String login(HttpServletRequest request, HttpServletResponse response) {
// 1.파라미터 변수 저장하기
String id = request.getParameter("id");
String pass = request.getParameter("pass");
// 2.비밀번호 검증
Member mem = dao.selectOne(id);
String msg = null;
String url = null;
if (mem == null) {
msg = "아이디를 확인하세요";
url = "loginForm";
} else if (!pass.equals(mem.getPass())) { // 아이디 존재. 비밀번호 검증
msg = "비밀번호가 틀립니다.";
url = "loginForm";
} else { // 정상 로그인
request.getSession().setAttribute("login", id); // 로그인 정보 등록
msg = "반갑습니다." + mem.getName() + "님";
url = "main";
}
request.setAttribute("msg", msg);
request.setAttribute("url", url);
return "alert"; // view 이름 :/view/alert.jsp
} // request.getSession() : session 객체

@RequestMapping("main")
public String main(HttpServletRequest request, HttpServletResponse response) {
//
String login = (String) request.getSession().getAttribute("login");
if (login == null) { // 로그아웃 상태
request.setAttribute("msg", "로그인하세요");
request.setAttribute("url", "loginForm");
return "alert"; // /view/alert.jsp 페이지 forward 됨(호출)
}
return "member/main"; // 사용되는 메서드 위치 view/member/main.jsp 페이지로 forward됨
}

/*
* logout이 할 일
* 
* 1.session에 등록 된 로그인 정보 제거 2.loginForm으로 페이지 이동
* 
*/
@RequestMapping("logout")
public String logout(HttpServletRequest request, HttpServletResponse response) {
request.getSession().invalidate();
return "redirect:loginForm"; // redirect는 url 부분이 바뀐다. forward는 url 안 바뀜 브라우저가 요청한게 아니기 때문에(그냥 서버단에서 함)
						// redirect request 객체가 다름

}

/*
* 1.id 파라미터를값을 조회 2.로그인 상태 검증 --로그아웃 상태 : '로그인 하세요' 메세지 출력 후 loginForm.jsp 페이지로
* 호출 --로그인 상태 - 다른 id 조회(관리자제외) 시 :'내 정보만 가능합니다.' 메세지 출력 후 main.jsp 호출 3.db에서
* id에 해당하는 데이터 조회하기 4.view로 데이터 전송 =>request 객체의 속성등록
* 
*/
@RequestMapping("info")
@MSLogin("loginIdCheck")
public String info(HttpServletRequest request, HttpServletResponse response) {
String id = request.getParameter("id");
Member mem = dao.selectOne(id);
request.setAttribute("mem", mem);
return "member/info";

}

/*
* 1.파라밈터 정보를 Member 객체에 저장 인코딩 필요 2. Member 객체를 이용하여 db에 insert(member 테이블)저장
* 3. 가입성공 :성공 메세지 출력 후 loginForm 페이지 이동 가입실패 : 실패 메세지 출력 후 joinForm 페이지 이동
*/
@RequestMapping("join")
public String join(HttpServletRequest request, HttpServletResponse response) {
try {
request.setCharacterEncoding("UTF-8");
} catch (UnsupportedEncodingException e) {

e.printStackTrace();
}

Member mem = new Member();
mem.setId(request.getParameter("id"));
mem.setPass(request.getParameter("pass"));
mem.setName(request.getParameter("name"));
mem.setGender(Integer.parseInt(request.getParameter("gender")));
mem.setTel(request.getParameter("tel"));
mem.setEmail(request.getParameter("email"));
mem.setPicture(request.getParameter("picture"));
if (dao.insert(mem)) {
request.setAttribute("msg", mem.getName() + "님 회원 가입 되었습니다.");
request.setAttribute("url", "loginForm");
} else {
request.setAttribute("msg", mem.getName() + "님 회원가입 시 오류 발생되었습니다.");
request.setAttribute("url", "joinForm");

}

return "alert";
}

/*
* 1.파라미터 값을 조회 2.로그인 상태 검증 -로그아웃 상태 : '로그인하세요' 메세지 출력 후 loginFrom.jsp 페이지 호츌
* -로그인 상태 -다른 id 수정(관리자 제외) : '내 정보만 '
* 
* 
*/
@RequestMapping("updateForm")
@MSLogin("loginIdCheck")
public String updayeForm(HttpServletRequest request, HttpServletResponse response) {
String id = request.getParameter("id");
Member mem = dao.selectOne(id);
request.setAttribute("mem", mem);
return "member/updateForm";
}
/*
* 1.모든 파라미터를 Member 객체에 저장하기 2.입력 된 비밀번호와 db에 저장된 비밀번호 비교. -관리자인 경우 관리자 비밀번호로
* 비교 불일치 : '비밀번호 오류' 메세지 출력 후 updateForm 페이지로 이동 3.Member 객체의 내용으로 db를 수정하기 -성공
* : 회원정보 수정 완료 메세지 출력 후 info 페이지로 이동 -실패 : 회원정보 수정 실패 메시지 출력 후 updateForm 페이지로
* 이동
* 
* 
*/

@RequestMapping("update")
@MSLogin("loginIdCheck")
public String update(HttpServletRequest request, HttpServletResponse response) {

Member mem = new Member();
mem.setId(request.getParameter("id"));
mem.setPass(request.getParameter("pass"));
mem.setName(request.getParameter("name"));
mem.setGender(Integer.parseInt(request.getParameter("gender")));
mem.setTel(request.getParameter("tel"));
mem.setEmail(request.getParameter("email"));
mem.setPicture(request.getParameter("picture"));

// 로그인 정보 가져오기
String login = (String) request.getSession().getAttribute("login");
Member dbMem = dao.selectOne(login);

String msg = "비밀번호가 틀렸습니다.";
String url = "updateForm?id=" + mem.getId();

if (mem.getPass().equals(dbMem.getPass())) {
if (dao.update(mem)) {
msg = "회원정보 수정 완료";
url = "info?id=" + mem.getId();

} else {
msg = "회원정보 수정 실패";
}
}
request.setAttribute("msg", msg);
request.setAttribute("url", url);
return "alert";

}

/*
* 1.id 파라미터 저장 2.로그인 정보 검즌 -로그아웃 상태: 로그인 하세요 메세지 출력. loginForm페이지로 이동 -다른 사용자
* 탈퇴(관리자 제외) : 본인만 탈퇴 가능합니다 출력 후 main 출력 3.deleteForm.jsp 페이지 호출
* 
*/
@RequestMapping("deleteForm")
@MSLogin("loginIdCheck")
public String deleteForm(HttpServletRequest request, HttpServletResponse response) {
return "member/deleteForm";
}
/*
* 1.파라미터 정보 저장 2.로그인 정보 검증 -로그아웃 상태: 로그인 하세요 메세지 출력 후 loginForm 페이지 이동 -다른사람
* 탈퇴(관리자 제외) : 본인만 탈퇴 가능 메세지 출력 후 main 페이지로 이동 3.관리자 탈퇴는 검증 -관리자 정보 탈퇴: 관리자는 탈퇴
* 불가 list 페이지로 이동 4.비밀번호 검증 -로그인 정보로 비밀번호 검증 비밀번호 불일치 : 비밀번호 오류 출력 후 delectForm
* 페이지로 이동 5.db에서 delete 실행 boolean MemberDao.delete(id) 탈퇴성공 - 일반 사용자 : 로그아웃
* 실행. 탈퇴 완료 메세지 출력 후 loginForm 페이지로 이동 - 관리자가 다른 사람 탈퇴 : 탈퇴 성공 메세지 출력 후 list
* 페이지로 이동 탈퇴실패 - 일반 사용자 : 탈퇴 실패 메세지 출력 후 info 페이지로 이동 - 관리자가 다른 사람 탈퇴 : 탈퇴 실패
* 메세지 출력 후 list 페이지로 이동
* 
*/

@RequestMapping("delete")
@MSLogin("loginIdCheck")
public String delete(HttpServletRequest request, HttpServletResponse response) {
String id = request.getParameter("id");
String pass = request.getParameter("pass");
String login = (String) request.getSession().getAttribute("login");
String msg = null;
String url = null;
if (id.equals("admin")) {
request.setAttribute("msg", "관리자는 탈퇴 불가능");
request.setAttribute("url", "list");
return "alert";
}

Member dbMem = dao.selectOne(login); // 로그인 된 사용자의 비밀번호로 검증
if (!pass.equals(dbMem.getPass())) {
request.setAttribute("msg", "비밀번호 오류");
request.setAttribute("url", "deleteForm?id=" + id);
return "alert";
}
// 비밀번호 일치 => 고객정보 삭제
if (dao.delete(id)) { // 삭제 성공
msg = id + "고객님 탈퇴성공";
if (login.equals("admin")) {
url = "list";
} else { // 일반 사용자
request.getSession().invalidate();
url = "loginForm";
}
} else { // 삭제 실패
msg = id + "고객님 탈퇴시 오류 발생. 탈퇴 실패";
if (login.equals("admin")) {
url = "list";
} else {
url = "info?id=" + id;
}
}
request.setAttribute("msg", msg);
request.setAttribute("url", url);
return "alert";
}

/*
* 1.관리자만 사용 가능한 페이지=>검증 필요 -로그아웃 상태 : 로그인이 필요합니다. 메세지 출력.loginForm.jsp 페이지 이동
* -로그인 상태 : 일반 사용자 로그인시 "관리자만 가능합니다." 메세지 출력 main.jsp 페이지 이동 2.db에서 모든 회원정보를
* 조회. List<Member> 객체로 리턴 List<Member> MemberDao.list() 3.List 객체를 화면에 출력하기
* 
*/
@RequestMapping("list")
@MSLogin("loginAdminCheck")
public String list(HttpServletRequest request, HttpServletResponse response) {
List<Member> list = dao.list();
request.setAttribute("list", list);
return "member/list";
}

/*
* 1.이미지 업로드 request 객체 사용 불가 이미지 파일 업로드 위치 :현재 url/picture opener 화면에 결과 전달=?
*/
@RequestMapping("picture")
public String picture(HttpServletRequest request, HttpServletResponse response) {
// request.getServletContext() : application 객체
// request.getServletContext().getRealPath("/")
// : 실제 웹 어플리케이션 경로
String path = request.getServletContext().getRealPath("/") + "/picture/";
String fname = null;
File f = new File(path); // 파일이 없으면 생성해라
if (!f.exists()) {
f.mkdirs();
} // 업로드 폴더가 없는 경우 폴더 생성
MultipartRequest multi = null;
try {
// request : 요청객체 파라미터,파일의 내용,파일이름,
// path: 업로드 된 파일이 저장된 폴더
// 10*1024*1024 : 업로드 파일의 최대 크기 바이트 수 10MB 최대 크기
// UTF-8 :인코딩 코드
multi = new MultipartRequest(request, path, 10 * 1024 * 1024, "UTF-8");
} catch (IOException e) {
e.printStackTrace();
}
// fname : 업로드 된 파일 이름
fname = multi.getFilesystemName("picture");
request.setAttribute("fname", fname);
return "member/picture";
}

@RequestMapping("idchk")
public String idchk(HttpServletRequest request, HttpServletResponse response) {
String id = request.getParameter("id");
Member mem = dao.selectOne(id);
String msg = null;
boolean able = true;

if (mem == null) {
msg = "사용 가능한 아이디 입니다.";
} else {
msg = "이미 사용 가능중인 아이디 입니다.";
able = false;
}
request.setAttribute("able", able);
request.setAttribute("msg", msg);
return "member/idchk";
}

@RequestMapping("id")
public String id(HttpServletRequest request, HttpServletResponse response) {
String email = request.getParameter("email");
String tel = request.getParameter("tel");

String id = dao.idSearch(email, tel);
if (id != null) {
String showId = id.substring(0, id.length() - 2) + "**";
request.setAttribute("id", showId);
return "member/id";

} else {
request.setAttribute("msg", "아이디를 찾을 수 없습니다.");
request.setAttribute("url", "idForm");
return "alert";
}
}

@RequestMapping("pw")
public String pw(HttpServletRequest request, HttpServletResponse response) {


String id = request.getParameter("id");
String email = request.getParameter("email");
String tel = request.getParameter("tel");

String pass = dao.pwSearch(id, email, tel);
if (pass != null) {
String showPass = pass.substring(2, pass.length());
request.setAttribute("pass", "showPass");
return "member/pw";

} else {
request.setAttribute("msg", "비밀번호를 찾을 수 없습니다.");
request.setAttribute("url", "pwForm");
return "alert";
}

}

	@RequestMapping("password")
	public String pssword(HttpServletRequest request, HttpServletResponse response) {
		//파라미터가져오기
		String pass = request.getParameter("pass");
		String chgpass = request.getParameter("chgpass");
		
		//로그인 정보 가져오기
		String login = (String) request.getSession().getAttribute("login");
		
		
		boolean opener = true;
		if (login == null) {
			request.setAttribute("msg", "로그인하세요");
			request.setAttribute("url", "loginForm");
			opener = true;
			return "alert";
		} else { // 로그인 상태
			
			Member dbMem = dao.selectOne(login); //로그인 정보 읽기
			if (pass.equals(dbMem.getPass())) { // db랑 비밀번호 같으면
				if (dao.updatePass(login, chgpass)) { // db에 비밀번호 변경
					request.setAttribute("msg", "비밀번호가 변경되었습니다 \\n 다시로그인 하세요");
					request.getSession().invalidate(); //세션 죽이기
					request.setAttribute("url", "loginForm");
					opener = true;
				} else {  //비밀번호 수정시 오류발생
					request.setAttribute("msg", "비밀번호 수정 실패");
					request.setAttribute("url", "updateForm?id=" + login);
					opener = true;
				}
			} else { //비밀번호 오류
				opener = false;
				request.setAttribute("msg", "비밀번호 오류입니다.");
				request.setAttribute("url", "passwordForm");

			}
		}
		
		request.setAttribute("opener", opener);
		return "member/password";
	}
	/*
	 * 네이버 smtp(메일보내는서버) 서버 이용하기
	 * 1. 네이버 2단계 로그인 해제
	 * 2. SMTP 서버 사용하도록 설정 => 네이버 메일에서 설정
	 * 3. LMS 에 mail.properties 다운 받기
	 * 4. https://mvnrepository.com/ 접속
	 * 5. ﻿web-inf/lib 2개의 jar 파일 붙여넣기
	 */
	@RequestMapping("mailForm")
//	@MSLogin("loginAdminCheck")
	public String mailForm(HttpServletRequest request, HttpServletResponse response) {
		String[] ids = request.getParameterValues("idchks");
		List<Member> list = dao.selectEmail(ids);
		request.setAttribute("list", list);
		return "member/mailForm";
	}
	@RequestMapping("mailSend")
//	@MSLogin("loginAdminCheck")
	public String mailSend(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		request.setCharacterEncoding("UTF-8"); //파라미터값 한글 인코딩
		String sender = request.getParameter("naverid");
		String passwd = request.getParameter("naverpw");
		String recipient = request.getParameter("recipient");
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String mtype = request.getParameter("mtype");
		/*
		 * Properties 클래스 : Hashtable(Map 구현 클래스)의 하위 클래스
		 * 					(key(String),value(String))인 클래스
		 * 					 => 제네릭 표현안함
		 * prop : 메일 전송을 위한 환경설정 객체  
		 */
		Properties prop = new Properties();
		//환경설정
		try {
			FileInputStream fis = new FileInputStream
					("D:/20230125/html/worksapce/jspstudy2/mail.properties");
			prop.load(fis); //key=value 으로 읽어서 Map 객체로 저장
			//prop.load(fis);
			prop.put("mail.smtp.user", sender);
			System.out.println(prop);
		} catch (IOException e) {
			e.printStackTrace();
		}
//========================================================================================================	
		/*
		 * 
		 */
		//메일 전송 전에 인증 객체 생성 =>  네이버 인증
		MyAuthenticator auth = new MyAuthenticator(sender, passwd); //MyAuthenticator : 인증객체
		//session 객체 : 메일 전송을 위한 연결 객체(properties 객체, 인증객체)
		Session session = Session.getInstance(prop, auth); //session 연결하면 자기가 받아서 알려줌
		//Message 객체 준비 : 메일의 내용을 저장하는 객체
		MimeMessage msg = new MimeMessage(session);//메세지 객체에 세션 넣어서 누가 보내는 건지 알려줌
		List<InternetAddress> addrs = new ArrayList<InternetAddress>();
		try {
			String[] emails = recipient.split(",");//수신자 , 를 이용해 분리
			for(String email : emails) {
				try {
					//수신인 이름 한글을 깨지지 않도록 주소값 변경
					//프로토콜에서 내부의 서버로만 이뤄져있으면..?
					//안하고 보내면 거기서 인코딩할 때 깨짐
					//8859_1 웹에서 사용되는 기본 인코딩 방법 (ISO 8859_1)
					addrs.add(new InternetAddress(new String(email.getBytes("utf-8"),"8859_1")));
				} catch(UnsupportedEncodingException ue) {
					ue.printStackTrace();
				}
			}
			//수신인 배열 객체로 들어가야함 하나씩 address객체로 넣어줌
			InternetAddress[] address = new InternetAddress[emails.length];
			for(int i=0; i<addrs.size();i++) {
				address[i] = addrs.get(i);
			}
			//보내는 사람 이메일 주소 설정
			InternetAddress from = new InternetAddress(sender+"@naver.com");
			msg.setFrom(from); //보내는 사람의 이메일 주소를 메일 객체에 설정
			//받는 사람 이메일 주소 설정
			//참조인은 cc로 보내면됨
			msg.setRecipients(Message.RecipientType.TO, address); //받는 사람들 설정
			msg.setSubject(title); //메일 제목
			msg.setSentDate(new Date()); //메일 전송시간
			msg.setText(content); //메일 내용
			MimeMultipart multipart = new MimeMultipart(); 
			MimeBodyPart body = new MimeBodyPart();
			body.setContent(content,mtype);
			multipart.addBodyPart(body);
			msg.setContent(multipart); 
			
			//메일전송
			Transport.send(msg); //메세지 객체 전송 (타입: mimemsg)

		} catch(MessagingException me) {
			System.out.println(me.getMessage());
			me.printStackTrace();
		}
		return "redirect:list";
	}
	public final class MyAuthenticator extends Authenticator {
		private String id;
		private String pw;
		
		public MyAuthenticator(String id, String pw) {
			this.id = id;
			this.pw = pw;
		}
		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(id,pw); //PasswordAuthentication 객체 만들어서 id, pw 넣어줌
		}
	}
	 
}

