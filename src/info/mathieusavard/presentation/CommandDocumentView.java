package info.mathieusavard.presentation;

import info.mathieusavard.indexgen.Article;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CommandDocumentView extends Command {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		String sDocumentId = request.getParameter("d");
		int documentId = Integer.parseInt(sDocumentId);
		Article a = new Article(documentId);
		request.setAttribute("article", a);
		
		RequestDispatcher rd = request.getRequestDispatcher(super.getJSPPAth("document_view.jsp"));
		rd.forward(request, response);

	}

}
