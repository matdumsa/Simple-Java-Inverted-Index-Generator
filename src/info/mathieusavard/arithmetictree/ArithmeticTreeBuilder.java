//Mathieu Dumais-Savard
//TP4 18 Mars 2009
//ArithmeticTreeBuilder.java
package info.mathieusavard.arithmetictree;

import java.util.Stack;
import java.util.StringTokenizer;

public class ArithmeticTreeBuilder
{
    private Stack<ArithmeticTree> treeStack = new Stack<ArithmeticTree>(); //La pile d'arbres en construction tel que dÃ©crite dans le manuel de cours
    private ArithmeticTree finalTree;
   
    public ArithmeticTreeBuilder(String postfix)
    {	
	String delim = " +-*/"; 
	String token;
	treeStack.clear();
	
	StringTokenizer st = new StringTokenizer(postfix, delim, true);
	while (st.hasMoreTokens()) {
	    token = st.nextToken();
	    
	    if(delim.contains(token)){
		if(!(token.equals(" "))){ //Opérateur trouvé
			ArithmeticTree opcodeR = treeStack.pop();	//on pop le right (selon p. 110 du livre)
			ArithmeticTree opcodeL = treeStack.pop();	//on pop le droit ensuite.
			ArithmeticTree treeToInsert = new ArithmeticTree(token, opcodeL, opcodeR);	//on créé un nouvel arbre
			treeStack.push(treeToInsert);	//pousse l'Arbe sur la pile;
			
		}
	    }
	    else{
		try {
		    Double.parseDouble(token); //Ce n'est pas un opérateur, il sagit donc d'un opérand. (feuille)
		    treeStack.push(new ArithmeticTree(token));
		}
		catch( NumberFormatException e ) { 
		    System.out.println("ERROR: Expected double got : "+token);
		    System.out.println(e);
		    System.exit(1);
		}

	    }
	}
	finalTree = treeStack.pop();	//le résultat sur la pile est notre arbre final.
    }

    public ArithmeticTree getTree()
    {
    	return finalTree;
    }

}