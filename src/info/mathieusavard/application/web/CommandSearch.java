package info.mathieusavard.application.web;

import info.mathieusavard.domain.queryprocessor.QueryProcessor;
import info.mathieusavard.domain.queryprocessor.ResultSet;
import info.mathieusavard.domain.queryprocessor.booleantree.InvalidQueryException;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CommandSearch extends Command {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		String query = request.getParameter("q");
		request.setAttribute("query", query);
		
		System.out.println("searching for " + query);
		ResultSet resultset = null;
		try {
			resultset = QueryProcessor.performQuery(query);
			
		} catch (InvalidQueryException e) {
			throw new ServletException(e);
		}
		request.setAttribute("timetomatch", QueryProcessor.getMatchingTime());
		request.setAttribute("resultcount", resultset.size());
		request.setAttribute("resultset", resultset);
		
		RequestDispatcher rd = request.getRequestDispatcher(super.getJSPPAth("search.jsp"));
		rd.forward(request, response);

	}

}
