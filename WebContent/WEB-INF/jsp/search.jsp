<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="info.mathieusavard.domain.reuters.ReutersDocument,java.util.List,info.mathieusavard.domain.queryprocessor.QueryProcessor,info.mathieusavard.technicalservices.BenchmarkRow"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Search results for <%=request.getAttribute("query")%></title>
</head>
<body>
<jsp:include page="searchForm.jsp" />

<%
	BenchmarkRow timer = new BenchmarkRow(null);
	timer.start();
	if (((Integer) request.getAttribute("result-count")) == 0) {
%>
	<h2>Sorry, no results have been found for <%=request.getAttribute("query")%></h2>
	
<%
		} else {
	%>

I found <%=request.getAttribute("result-count")%> results in <%=request.getAttribute("time-to-match")%> ms.

	<%
		while(QueryProcessor.hasNext()) { 
		ReutersDocument a = QueryProcessor.next().getResult(); if (a==null) continue;
	%>
		<h3><a href="document/view/?d=<%=a.getId()%>&q=<%= request.getAttribute("query") %>"><%= a.getTitle() %></a></h3>
	<% 
	 	response.flushBuffer();
		}
}%>

<% timer.stop();  %>
<hr>Page generated in <%= timer.getDuration()/1000.0 %> seconds.
</body>
</html>