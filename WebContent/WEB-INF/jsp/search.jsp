<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"
	import="info.mathieusavard.domain.WebDocument,info.mathieusavard.domain.GenericDocument,java.util.List,info.mathieusavard.domain.queryprocessor.QueryProcessor,info.mathieusavard.technicalservices.BenchmarkRow"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Search results for ${query}></title>
</head>
<body>
	<jsp:include page="searchForm.jsp" />
	<c:choose>
		<c:when test="${resultcount == 0}">
			<h2>
				Sorry, no results have been found for ${query}
			</h2>
		</c:when>
		<c:otherwise>
	
	For Query : ${resultset.userInputQuery}
	<c:choose>
			<c:when test="${resultset.suggestedQuery != null}">
			<h2>
				<font size="3" color="red">Did you mean ?</font> <font size="3">${resultset.suggestedQuery }</font>
			</h2>
			</c:when>
	 </c:choose>
	I found ${resultcount} results in ${timetomatch} ms.

	<c:forEach var="r" items="${resultset.results}">
				<h3>
				<!--	<a href="document/view/?d=${r.document.id }&q=${query}">
						${r.document.title} </a>-->

					<a href="${r.document.url}"> ${r.document.title} </a>	
				</h3>
			</c:forEach>
		</c:otherwise>
	</c:choose>
</body>
</html>