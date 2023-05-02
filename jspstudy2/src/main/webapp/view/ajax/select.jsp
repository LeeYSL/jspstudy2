<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%--html 주석 쓰면 안됨 --%>
<%--list의 데이터를 JSON(배열) 형태의 문자열로 출력 --%>
[
<c:forEach var="s" items="${list}" varStatus="stat">
  "${s}"<c:if test="${stat.count < len}">,</c:if>
</c:forEach>
]

