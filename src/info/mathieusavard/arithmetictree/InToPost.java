package info.mathieusavard.arithmetictree;
import java.util.Stack;
import java.util.StringTokenizer;

public class InToPost {
    
    private Stack<String> opStack;
    private String input;
    private String output = "";
    
    public InToPost(String in) {
   	input = in;
	opStack = new Stack<String>();
    }
    
    public String doTrans()
    {   return doTrans(input);
    }

    public String doTrans(String in) {
   	input=in;
   	opStack.clear();
	String delim = " +-*/()";
	String token;
	
	StringTokenizer st = new StringTokenizer(input, delim, true);
	while (st.hasMoreTokens()) {
	    token = st.nextToken();

	    if(delim.contains(token)){ 
		if(!(token.equals(" "))){ //On travaille assurément avec un opérateur et non avec un espace blanc
		    if(token.equals("+") || token.equals("-"))
			gotOper(token, 1); //   (precedence 1)
		    if(token.equals("*") || token.equals("/"))
			gotOper(token, 2); //   (precedence 2)
		    if(token.equals("("))
			opStack.push(token); // on push toujours les parenthèses gauche
		    if(token.equals(")"))
			gotParen(); // aller popper jusqu'à une parenthèse gauche
		}
	    }
	    else
		output = output + " " + token; // ce doit être une opérande, l'écrire dans le output
	}
	
	while (!opStack.isEmpty()) {
	    output = output + " " +opStack.pop();
	    
	}
	return output.substring(1); // retourne l'expression en postfix et enlève l'espace du début
    }
    
    public void gotOper(String opThis, int prec1) {

	while (!opStack.isEmpty()) {

	    String opTop = opStack.peek();

	    if (opTop.equals("(")) {//on pop une parenthèse gauche si et seulement si on recoit une parenthèse droite en entrée.
		break;
	    }// le dessus de la pile est un opérateur

	    else {//précédence du nouvel opérateur
		
		int prec2;
		
		if (opTop.equals("+") || opTop.equals("-"))
		    prec2 = 1;
		else
		    prec2 = 2;
		
		if (prec2 < prec1) // si la précédence du nouvel opérateur est plus basse que celle du précédent
		    break; // laisser l'ancien opérateur sur la pile
		
		else    // la précédence du nouvel opérateur est >= à celle du précédent
		    output = output + " " +opStack.pop(); //popper l'opérateur qui était sur le dessus de la pile et l'écrire
	    }
	}
	opStack.push(opThis);
    }
    
    public void gotParen(){ //On reçoit une parenthèse droite en entrée
	
	while (!opStack.isEmpty()) {
	
	    String opTop = opStack.pop();
	    
	    if (opTop.equals("(")) 
		break; 
	    else
		output = output + " " + opTop;
	}
    }
    
}
