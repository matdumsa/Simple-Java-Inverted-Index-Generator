<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="info.mathieusavard.indexgen.Article, info.mathieusavard.indexgen.BenchmarkRow"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<% Article a = (Article) request.getAttribute("article"); %>
<title><%=a.getTitle() %></title>
<style>
	div.document { width: 80%; margin:2em auto; }
</style>
</head>
<body>
<jsp:include page="searchForm.jsp" />

	<h1><%=a.getTitle() %></h1>	
	<div class="document">
		<%=a.getText().replace("\n","<br>").replace("    ","</p><p>")%>
	</div>

<hr>Page generated in <%= request.getAttribute("pull-time") %> ms.
</body>
</html>