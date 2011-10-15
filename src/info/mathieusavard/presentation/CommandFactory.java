package info.mathieusavard.presentation;

import java.util.HashMap;

public class CommandFactory {

	private static HashMap<String, Class<? extends Command>> commandStore = new HashMap<String, Class<? extends Command>>();

	static {
		//Each registered controller class should be named here.
		commandStore.put("/search", CommandSearch.class);
		commandStore.put("/document/view", CommandDocumentView.class);
	}

	
	public static Command createCommand(String requestedURI) throws IllegalURIException {
		if (requestedURI.equals("/"))
			return new CommandHomePage();

		for (String prefix : commandStore.keySet()) {
			if (requestedURI.startsWith(prefix))
				try {
					return commandStore.get(prefix).newInstance();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
		throw new IllegalURIException("The command '" + requestedURI + "' cannot be dispatched properly.");
	}
	
	public static void registerCommand(String urlPrefix, Class<? extends Command> c) {
		commandStore.put(urlPrefix,c);
	}
	
	public static class IllegalURIException extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = -3204639399252733913L;

		public IllegalURIException(String reason) {
			super(reason);
		}
	}

}
