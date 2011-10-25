package info.mathieusavard.indexgen;

import info.mathieusavard.utils.Constants;


public class ArticleFactory {

	public static Article findArticle(int id) {		
		//Determine article collection
		int collection = id/1000;
		
		//open this collection
		Collection ac = new Collection(Constants.basepath + "/reut/" + collection + "/" + id + ".xml");
		return ac.getArticleById(String.valueOf(id));

	}
}
