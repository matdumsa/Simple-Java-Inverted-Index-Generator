package info.mathieusavard.application.web;

import info.mathieusavard.domain.queryprocessor.QueryProcessor;
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
		try {
			QueryProcessor.performBufferedQuery(query);
		} catch (InvalidQueryException e) {
			throw new ServletException(e);
		}
		request.setAttribute("time-to-match", QueryProcessor.getMatchingTime());
		request.setAttribute("result-count", QueryProcessor.size());
		
		RequestDispatcher rd = request.getRequestDispatcher(super.getJSPPAth("search.jsp"));
		rd.forward(request, response);

	}

}
