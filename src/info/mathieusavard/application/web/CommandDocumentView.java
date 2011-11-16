package info.mathieusavard.application.web;

import info.mathieusavard.domain.index.DiskReaderThread;
import info.mathieusavard.domain.reuters.ReutersDocument;
import info.mathieusavard.technicalservices.BenchmarkRow;

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
		BenchmarkRow timer = new BenchmarkRow(null);
		timer.start();
		ReutersDocument a = DiskReaderThread.getArticleById(documentId);
		timer.stop();
		request.setAttribute("article", a);
		request.setAttribute("pull-time", timer.getDuration());
		
		RequestDispatcher rd = request.getRequestDispatcher(super.getJSPPAth("document_view.jsp"));
		rd.forward(request, response);

	}

}
