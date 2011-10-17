package info.mathieusavard.indexgen;

public class ArticleFactory {

	public static Article findArticle(int id) {		
		//Determine article collection
		int collection = id/1000;
		
		//open this collection
		ArticleCollection ac = new ArticleCollection(Constants.basepath + "reut/reut2-" + Utils.padWithZero(collection, 3) + ".xml");
		return ac.getArticleById(String.valueOf(id));

	}
}
