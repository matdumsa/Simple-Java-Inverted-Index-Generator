<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="info.mathieusavard.indexgen.Article, java.util.List, info.mathieusavard.queryprocessor.QueryProcessor"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Search results for <%= request.getAttribute("query") %></title>
</head>
<body>

<% 
	if (((Integer) request.getAttribute("result-count")) == 0) {
%>
	<h2>Sorry, no results have been found for <%= request.getAttribute("query") %></h2>
	
<% } else { 
	while(QueryProcessor.hasNext()) { Article a = QueryProcessor.next(); %>
		<h3><a href="document/view/?d=<%=a.getId()%>"><%= a.getTitle() %></a></h3>
	<% }
}%>
</body>
</html>