package info.mathieusavard.arithmetictree;
import java.util.Stack;
import java.util.StringTokenizer;

public class InToPost {

	private static String output;


	public static String doTrans(String input) {
		Stack<String> opStack = new Stack<String>();
		output = "";
		
		input = input.replace(" AND ", "^");
		input = input.replace(" OR ", "+");
		input = input.replace(" NOT ", "-");
		input = input.replace(" AND(", "^(");
		input = input.replace(" OR(", "+(");
		input = input.replace(" NOT(", "-(");
		input = input.replace(" (NOT", "(-");
		input = input.replace("  ", " ");
		input = input.replace(" ^ ", "");
		input = input.replace(" - ", "-");
		input = input.replace(" + ", "+");
		input = input.replace(" ", "^");

		//Distribute -(a or b) to (-a and -b)
		System.out.println("Normalizing: " + input);
		input = normalizeString(input, false);
		System.out.println("=== " + input);
		
		String delim = "^+()";
		String token;

		StringTokenizer st = new StringTokenizer(input, delim, true);
		while (st.hasMoreTokens()) {
			token = st.nextToken();

			if(delim.contains(token)){ 
				if(!(token.equals(" "))){ //On travaille assurément avec un opérateur et non avec un espace blanc
					if(token.equals("+") || token.equals("^") || token.equals("-"))
						processOpcode(token, 1, opStack); //   (precedence 1)
					if(token.equals("("))
						opStack.push(token); // on push toujours les parenthèses gauche
					if(token.equals(")"))
						processClosingParenthesis(opStack); // aller popper jusqu'à une parenthèse gauche

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

	public static void processOpcode(String opcode, int prec1, Stack<String> opStack) {

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
		opStack.push(opcode);
	}

	public static void processClosingParenthesis(Stack<String> opStack){ //On reçoit une parenthèse droite en entrée
		String resultToAppend = "";
		while (!opStack.isEmpty()) {
			String opTop = opStack.pop();
			if (opTop.equals("(")) 
				break;
			else
				resultToAppend = resultToAppend + " " + opTop;
		}
		
		output = output + resultToAppend;
	}
	
	/*
	 * Distribute the - sign in a set of parenthesis,
	 */
	private static String normalizeString(String input, boolean negate) {
		int minusOpen = input.indexOf("-(");
		if (minusOpen == -1)
			if (negate)
				return negateString(input);
			else
				return input;
		
		//Get the beginning of the string from 0 to that -(
		String firstPart = input.substring(0, minusOpen);
		
		//How many ( are there in the firstPart?
		int closingParentCount = countOccurrences(firstPart, '(') - countOccurrences(firstPart, ')');
		int closingParentOfInterest =  findBackPositionIgnoring(input, ')', closingParentCount);
		
		String substring = input.substring(minusOpen+2, closingParentOfInterest);
		
		String lastPart = input.substring(closingParentOfInterest+1);
		return firstPart + "(" + normalizeString(substring, !negate) +")" + lastPart;
	}
	
	private static String negateString(String input) {
		
		StringTokenizer st = new StringTokenizer(input, " ^+", true);
		String ans = "";
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (token.equals("^"))
				ans+="+";
			else if (token.equals("+"))
				ans+="^";
			else
				ans += "-" + token;
		}
		return ans.replace("--", "");
	} 
	
	public static void main(String[] args) {
		String s = "(((((sdkf or sjd sh a -(a+b+c-d))))) or d)";
		System.out.println(normalizeString(s, false));
	}
	public static int countOccurrences(String haystack, char needle)
	{
	    int count = 0;
	    for (int i=0; i < haystack.length(); i++)
	    {
	        if (haystack.charAt(i) == needle)
	        {
	             count++;
	        }
	    }
	    return count;
	}
	
	public static int findBackPositionIgnoring(String haystack, char needle, int ignoreCount)
	{
	    for (int i=haystack.length()-1; i >= 0; i--)
	    {
	        if (haystack.charAt(i) == needle)
	        {
	             ignoreCount--;
	             if (ignoreCount == -1)
	            	 return i;
	        }
	    }
	    return -1;
	}
	
}
