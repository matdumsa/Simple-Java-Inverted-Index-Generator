<form method="get" action="/InformationRetrieval/search">
	<input name="q" type="text" value="<%= (request.getParameter("q") != null) ? request.getParameter("q") : ""%>" /><input type="submit" value="search" />
</form>

<hr/>