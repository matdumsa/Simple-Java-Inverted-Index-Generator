//Mathieu Dumais-Savard
//TP4 18 Mars 2009
//ArithmeticTreeMain.java
//Ce fichier contient la class main et sera exécuter en premier.
package info.mathieusavard.arithmetictree;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ArithmeticTreeMain
{

    public static void main(String[] args){

	String input;
	String postfix;
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

	ArithmeticTreeBuilder tb;
	InToPost theTrans;
	ArithmeticTree tree;
	
        try
	{
            System.out.println( "Entrez une expression, une par ligne:" );
            while(!(input = in.readLine()).equals(""))
            {
		theTrans = new InToPost(input);
		postfix = theTrans.doTrans();
		System.out.println("Voici votre expression convertie en postfix: " + postfix);
		tb = new ArithmeticTreeBuilder(postfix);
		tree = tb.getTree();
		System.out.println();
		System.out.println(tree.stackPostOrder());
		System.out.println();
		System.out.println("Le résultat de l'expression fournie est : " + tree.getResult());
		System.out.println("Merci d'entrer une expression pour comparaison des arbres, faire enter pour poursuivre");
		String newLine = in.readLine();
		if (!newLine.equals(""))	//portion pour réponse à la question #2
		{	ArithmeticTreeBuilder comparableTree = new ArithmeticTreeBuilder(theTrans.doTrans(newLine));
			System.out.println("==" + comparableTree.getTree().benchmarkedEquals(tree));
		}
		System.out.println( "Entrez la prochaine expression:" );
            }
        }
        catch( IOException e ) { e.printStackTrace(); }
    }

}