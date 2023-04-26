<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>elForm.jsp 결과 화면</title>
</head><body>
<fmt:requestEncoding value="UTF-8"/>
<%   
//  request.setCharacterEncoding("UTF-8");
    String tel = "010-1111-2222";
    String te11 ="010-1111-3333";
    pageContext.setAttribute("tel", tel);
    pageContext.setAttribute("test", "pageContext 객체의 test 속성");
    request.setAttribute("test", "request 객체의 test 속성");
    application.setAttribute("test", "application 객체의 속성");

%>
<h3>JSP의 스크립트를 이용하여 파라미터와 속성값 출력</h3>
pageContext test 속성 값 :<%=pageContext.getAttribute("test") %><br>
session test 속성 값 : <%=session.getAttribute("test") %><br>
today 속성 값 : <%=session.getAttribute("today") %><br>
name 파라미터 값: <%=request.getParameter("name") %><br>
tel 변수 값 : <%=tel %><br>
tel 속성 값 : <%=pageContext.getAttribute("tel") %><br>
noAttr 속성 값 :<%=pageContext.getAttribute("noAttr") %><br><br>
noparam 파라미터 값 : <%=request.getParameter("noparam")%>


<h3>JSP의 EL(표현언어)를 이용하여 파라미터와 속성 값 출력</h3>
pageContext test 속성 값 : ${pageScope.test}<br>
request test 속성 값 : ${requestScope.test}<br>
session test 속성값  : ${sessionScope.test}<br>
application test 속성 값: ${applicationScope.test}<br>
session test 속성 값 : ${sessionScope.test}<br>
today 속성 값 :  ${today }<br> <%--한개만 있을 경우 앞에 영역(sessionScpoe) 생략 가능 --%>
name 파라미터 값: ${param.name}<br>
tel 변수 값 : EL로 표현 못함, ${tel1}<br>
tel 속성 값 : ${pageScope.tel}<br>
noAttr 속성 값 :${pageScope.noAttr }<br><br>
noparam 파라미터 값 :${param.noparm}<br>
<h3>영역을 표시하여 test 속성 값 출력하기</h3> <%--영역에 다 있으면 우선 순위가 높은 순서로 조회 --%>
\${test} = ${test}<br>
\${pageScope.test}=${pageScope.test}<br>
\${requestScope.test}=${requestScope.test}<br>
\${sessionScope.test}=${sessionScope.test}<br>
\${applicationScope.test} =${applicationScope.test}<br>
\${today}=${today}<br>
\${pageScope.today}=${pageScope.today }<br>
\${requestScope.today}=${requestScope.today }<br>
\${sessionScope.today}=${sessionScope.today }<br>
\${applicationScope.today}=${appliactionScope.today }<br>

<%--
   ${test} : 영역 담당 객체에 저장 된 속성 중 이름이 test인 속성 값 출력
     
      1.page 영영에 등력 된 test 속성 값 리턴 : pageScope.test
      -1번에 해당되는 내용없으면
      2. requestScope.test 속성 값 리턴
       -2번에 해당되는 내용없으면
      3.ssessionScope.test 속성 값 리턴
       -3번에 해당되는 내용없으면
      4.applicationScope.test 속성 값 리턴
        -1,2,3,4의 내용 없으면 공란을 리턴
        
      == 직접 영역 담당 객체를 표현하면 영역에 해당하는 속성을 리턴
      1.해당 객체가 없다면 공란 리턴
         
 --%>



</body>
</html>