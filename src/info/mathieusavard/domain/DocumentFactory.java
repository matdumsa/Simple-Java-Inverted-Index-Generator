package info.mathieusavard.domain;

import info.mathieusavard.domain.index.ParsableArticleCollection;
import info.mathieusavard.technicalservices.Constants;


public class DocumentFactory {

	public static Document findArticle(int id) {		
		//Determine article collection
		int collection = id/1000;
		
		//open this collection
		ParsableArticleCollection ac = new ParsableArticleCollection(Constants.basepath + "/reut/" + collection + "/" + id + ".xml");
		return ac.getArticleById(String.valueOf(id));

	}
}
