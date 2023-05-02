<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<h2 class="w3-ceneter">수출입은행<br>${date}</h2>
<table class="w3-table-all">
	<tr>
		<th>통화</th>
		<th>기준을</th>
		<th>받을 때</th>
		<th>파실 때</th>
	</tr>
	<c:forEach items="${list}" var="td">
		<tr>
			<td>${td[0]}
		    <br>${td[1]}</td>
			<td>${td[4]}</td>
			<td>${td[3]}</td>
			<td>${td[2]}</td>
		</tr>
	</c:forEach>
</table>