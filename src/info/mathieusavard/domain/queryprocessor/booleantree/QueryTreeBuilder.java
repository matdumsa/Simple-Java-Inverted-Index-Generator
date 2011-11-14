//Mathieu Dumais-Savard
//TP4 18 Mars 2009
//ArithmeticTreeBuilder.java
package info.mathieusavard.domain.queryprocessor.booleantree;

import java.util.Stack;
import java.util.StringTokenizer;

public class QueryTreeBuilder
{

	public static QueryTree getTree(String postfix)
	{	
		Stack<QueryTree> treeStack = new Stack<QueryTree>(); //La pile d'arbres en construction tel que décrite dans le manuel de cours
		QueryTree finalTree;
		System.out.println(postfix);
		String delim = " +^"; 
		String token;
		treeStack.clear();

		StringTokenizer st = new StringTokenizer(postfix, delim, true);
		while (st.hasMoreTokens()) {
			token = st.nextToken();

			if(delim.contains(token)){
				if(!(token.equals(" "))){ //Op�rateur trouv�
					QueryTree opcodeR = null, opcodeL = null;
					opcodeR = treeStack.pop();	//on pop le right (selon p. 110 du livre)
					opcodeL = treeStack.pop();	//on pop le droit ensuite.
					QueryTree treeToInsert = new QueryTree(token, opcodeL, opcodeR);	//on cr�� un nouvel arbre
					treeStack.push(treeToInsert);	//pousse l'Arbe sur la pile;
				}
			}
			else{
				treeStack.push(new QueryTree(token));
			}
		}
		finalTree = treeStack.pop();	//le r�sultat sur la pile est notre arbre final.
		return finalTree;
	}


}