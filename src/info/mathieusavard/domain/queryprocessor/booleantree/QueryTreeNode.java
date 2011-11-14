//Mathieu Dumais-Savard
//TP4 18 Mars 2009
//ArithmeticTreeNode.java
package info.mathieusavard.domain.queryprocessor.booleantree;

import info.mathieusavard.domain.Posting;
import info.mathieusavard.domain.index.spimi.IInvertedIndex;
import info.mathieusavard.technicalservices.SetOperation;

import java.util.HashSet;
import java.util.Set;

class QueryTreeNode
{
    private String element;
    private QueryTreeNode left;
    private QueryTreeNode right;
	protected static String queryTerms;
    
    public QueryTreeNode(String elem)
    {
    	this.element = elem;
    }

    public QueryTreeNode(String elem, QueryTreeNode left, QueryTreeNode right)
    {
    	this.element = elem;
    	this.left = left;
    	this.right = right;
    }

    public Boolean isLeaf()
    {
    	if (left == null && right == null)
    		return true;
    	else
    		return false;
    }

    public QueryTreeNode getLeftNode()
    { return left;  }

    public QueryTreeNode getRightNode()
    { return right;  }

    public Set<Posting> getResult(IInvertedIndex index)
    {	//C'est ici que les calculs sont effectu�s pour un noeud.
    	if (isLeaf()) {
    		Set<Posting> possibleAnswer = null;
    		if (element.charAt(0) == '-') {
    			element = element.substring(1);
        		possibleAnswer = (Set<Posting>) index.getSet(element);
        		if (possibleAnswer == null)
        			return index.getAll();
        		possibleAnswer = SetOperation.difference(index.getAll(), possibleAnswer);
    		}
    		else {
    			queryTerms += ((queryTerms.length()==0) ?"":" ") + element;
        		possibleAnswer = (Set<Posting>) index.getSet(element);    			
        		System.out.println("  Found" + possibleAnswer.size() + " for term '" + element + "'");
    		}
    		if (possibleAnswer == null)
    			return new HashSet<Posting>(); //empty set
    		return possibleAnswer;
    	}
	else {
			String opcode = element;
			Set<Posting> operandL = left.getResult(index);
			Set<Posting> operandR = right.getResult(index);
			if (opcode.equals("+"))
				return SetOperation.union(operandL, operandR);
			else if (opcode.equals("-"))
				return SetOperation.difference(operandL, operandR);
			else if (opcode.equals("^"))
				return SetOperation.intersection(operandL, operandR);
			else
			{	System.out.println("Hm... symbole inconun \"" + opcode + "\"");
				return null;
			}
		}

    }
	    
    public String getElement()
    {	return element;
    }
    
    public boolean equals(QueryTreeNode otherNode)
        {	
    	if (isLeaf() != otherNode.isLeaf())
    		return false;	//one is a leaf the other is not... 
    	if(otherNode.element == null && element == null && left==null && otherNode.left==null && right==null && otherNode.right ==null)
    		return false;	//par convention null != null
    	if (!this.element.equals(otherNode.element))
    		return false;
    	//� ce stade on sait que les deux sont initialis�s, retournent le m�me .isLeaf() et ont la m�me valeur
    	if (isLeaf())	//je suis feuille, l'autre est feuille, nos valeurs sont =
    		return true;	
   		else	//creusons plus loin.
    			return (left.equals(otherNode.left) && right.equals(otherNode.right));
    	
    }

}