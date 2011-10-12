//Mathieu Dumais-Savard
//TP4 18 Mars 2009
//ArithmeticTreeNode.java
package info.mathieusavard.arithmetictree;

public class ArithmeticTreeNode
{
    private String element;
    private ArithmeticTreeNode left;
    private ArithmeticTreeNode right;
    
    public ArithmeticTreeNode(String elem)
    {
    	this.element = elem;
    }

    public ArithmeticTreeNode(String elem, ArithmeticTreeNode left, ArithmeticTreeNode right)
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

    public ArithmeticTreeNode getLeftNode()
    { return left;  }

    public ArithmeticTreeNode getRightNode()
    { return right;  }

    public double getResult()
    {	//C'est ici que les calculs sont effectués pour un noeud.
    	if (isLeaf())
    		return Double.parseDouble(element);
	else {
			String opcode = element;
			Double operandL = left.getResult();
			Double operandR = right.getResult();
			if (opcode.equals("+"))
				return operandL + operandR;
			else if (opcode.equals("-"))
				return operandL - operandR;
			else if (opcode.equals("*"))
				return operandL * operandR;
			else if (opcode.equals("/"))
			{	if (operandR==0)
					System.out.println("Division par zéro");
				return operandL / operandR;
			}
			else
			{	System.out.println("Hm... symbole inconun \"" + opcode + "\"");
				return 0;
			}
		}

    }
	    
    public String getElement()
    {	return element;
    }
    
    public boolean equals(ArithmeticTreeNode otherNode)
        {	
    	if (isLeaf() != otherNode.isLeaf())
    		return false;	//one is a leaf the other is not... 
    	if(otherNode.element == null && element == null && left==null && otherNode.left==null && right==null && otherNode.right ==null)
    		return false;	//par convention null != null
    	if (!this.element.equals(otherNode.element))
    		return false;
    	//à ce stade on sait que les deux sont initialisés, retournent le même .isLeaf() et ont la même valeur
    	if (isLeaf())	//je suis feuille, l'autre est feuille, nos valeurs sont =
    		return true;	
   		else	//creusons plus loin.
    			return (left.equals(otherNode.left) && right.equals(otherNode.right));
    	
    }
}