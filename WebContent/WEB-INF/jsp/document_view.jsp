<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="info.mathieusavard.domain.reuters.ReutersDocument,info.mathieusavard.technicalservices.BenchmarkRow"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%
	ReutersDocument a = (ReutersDocument) request.getAttribute("article");
%>
<title><%=a.getTitle() %></title>
<style>
	div.document { width: 80%; margin:2em auto; }
	.found { background-color: yellow; }
	span.label { margin-right:1em; }
</style>
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.6/jquery.min.js"></script>
<script>

function boldify(m){
    return '<span class="found">'+m+'<\/span>';
}

function stringToRegExp(pattern, flags){
    return new RegExp(
        pattern.replace(/[\[\]\\{}()+*?.$^|]/g, function(m){return '\\'+m;}),
        flags);
}


	$(function() {
		$.each($("#q").val().split(" "), function(i,q) {
			var label = $("<span class='found label'>" + q+ "</span>").appendTo("h2");
			
			pattern=stringToRegExp(q, 'gi');
			$("div.document").html($("div.document").html().replace(pattern, boldify));
			
		});
		
	});
</script>
</head>
<body>
<jsp:include page="searchForm.jsp" />

	<h1><%=a.getTitle() %></h1>	
	<h2></h2>
	<div class="document">
		<%=a.getText().replace("\n","<br>").replace("    ","</p><p>")%>
	</div>

<hr>Page generated in <%= request.getAttribute("pull-time") %> ms.
</body>
</html>