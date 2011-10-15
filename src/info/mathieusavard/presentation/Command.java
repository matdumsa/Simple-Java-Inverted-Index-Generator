package info.mathieusavard.presentation;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public abstract class Command {

	public Command() {
	
	}

	public abstract void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException;

	protected static final String getJSPPAth(String string) {
		return "/WEB-INF/jsp/" + string;
	}
	


}
