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
		ResultSet result = null;
		try {
			result = QueryProcessor.performBufferedQuery(query);
			
		} catch (InvalidQueryException e) {
			throw new ServletException(e);
		}
		request.setAttribute("timetomatch", QueryProcessor.getMatchingTime());
		request.setAttribute("resultcount", result.size());
		request.setAttribute("result", result);
		
		RequestDispatcher rd = request.getRequestDispatcher(super.getJSPPAth("search.jsp"));
		rd.forward(request, response);

	}

}
