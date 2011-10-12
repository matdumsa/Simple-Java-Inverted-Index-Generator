//Mathieu Dumais-Savard
//TP4 18 Mars 2009
//ArithmeticTree.java
package info.mathieusavard.arithmetictree;

import java.util.Stack;

public class ArithmeticTree
{
    private ArithmeticTreeNode root;
    private String expr; //L'expression construite en parcourant l'arbre
    Stack<ArithmeticTreeNode> nodeStack;	//nodeStack requis pour le parcours post-ordre. Sera initialis� new() sur demande.
    private double result; //Le r�sultat de l'expression representee par l'arbre
    private boolean resultatValide = false;	//quand faux, la valeur devra �tre calcul�e par getResult() avant d'�tre retourn�.
    
    
    public ArithmeticTree(String elem)
    { //Nous recevons qu'une string alors cr�ons un arbre avec un seul noeud repr�sent� par cette string
    	root = new ArithmeticTreeNode(elem);
    }

    public ArithmeticTree(String newRoot, ArithmeticTree left, ArithmeticTree right)
    {	//cr�ation d'un arbre avec enfants
    	root = new ArithmeticTreeNode(newRoot, left.root, right.root);
    }

    public ArithmeticTreeNode getRoot()
    {
    	return root;
    }

    public double getResult()
    {	if (resultatValide == false)	//le r�sultat est � calculer?
    		setResult();
    	return result;
    }

    public void setResult()
    {
    	result = root.getResult();
    	resultatValide=true;
    	
    }

    public String recInOrder()
    {	System.out.print("Notation inorder (Recursive): ");
    	expr = "";
    	return recInOrder(root);	
    }

    public String recInOrder(ArithmeticTreeNode node)
    {
    	if (node.isLeaf())
    		return node.getElement();
    	else	//le node de gauche, le node pr�sent, le node de droite. (parenth�se pour maintenir la priorit�)
    		return "(" + recInOrder(node.getLeftNode()) + node.getElement() + recInOrder(node.getRightNode()) + ")";    	
    }

    public String stackPostOrder()
    {
		expr = "";
		if (nodeStack != null)
			nodeStack.clear();
		else
			nodeStack = new Stack<ArithmeticTreeNode>(); //A utiliser pour les parcours pre et post ordre 
		//La cr�ation de nodeStack a �t� mise ici afin d'�viter de surcharger chaque instance de ArithmeticTree d'un nouveau nodeStack.
		
		System.out.print("Notation postorder (Stack): ");
	
		stackPostOrder( root);
		//� ce point, la pile nodeStack contient exactement ce que nous avons besoin, dans l'ordre d�sir�. Il ne reste qu'� imprimer � l'�cran
		while (!nodeStack.isEmpty())
		{	ArithmeticTreeNode currentNode = nodeStack.pop();
			expr = expr.concat(currentNode.getElement() + ((!nodeStack.isEmpty()) ? " ":""));
		}
		return expr;
    }

    public void stackPostOrder(ArithmeticTreeNode node)
    {	if (node.isLeaf())
    		nodeStack.add(node);
    	else	//afin de produire l'effet d�sir� (enfant de gauche, enfant de droit, noeud courrant, nous devrons les "pousser" dans le d�sordre.
    	{	nodeStack.add(node);
    		stackPostOrder( node.getRightNode());	//appel r�cursif
    		stackPostOrder( node.getLeftNode());		//appel r�cursif
    	}
    }

    public boolean equals(ArithmeticTree otherTree)
    {	return root.equals(otherTree.root);
    }

    public boolean benchmarkedEquals(ArithmeticTree otherTree)	//version de equals qui donne le temps pris pour le calcul.
    {	Benchmark bench = new Benchmark();
    	bench.start();
    	boolean comparaisonResult = equals(otherTree);
    	bench.stop();
    	System.out.println("La comparaison des arbres a dur� " + bench.getResult() + " sec.");
    	return comparaisonResult;
    }
}