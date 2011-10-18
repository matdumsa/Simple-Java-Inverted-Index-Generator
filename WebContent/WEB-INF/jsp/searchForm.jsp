<form method="get" action="/InformationRetrieval/search">
	<input id="q" name="q" type="text" value="<%= (request.getParameter("q") != null) ? request.getParameter("q") : ""%>" /><input type="submit" value="search" />
</form>

<hr/>