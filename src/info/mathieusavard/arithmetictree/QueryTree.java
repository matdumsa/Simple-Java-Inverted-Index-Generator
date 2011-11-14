//Mathieu Dumais-Savard
//TP4 18 Mars 2009
//ArithmeticTree.java
package info.mathieusavard.arithmetictree;

import info.mathieusavard.indexgen.IInvertedIndex;
import info.mathieusavard.indexgen.Posting;

import java.util.Set;

public class QueryTree
{
    private QueryTreeNode root;
        
    public QueryTree(String elem)
    { //Nous recevons qu'une string alors créons un arbre avec un seul noeud représenté par cette string
    	root = new QueryTreeNode(elem);
    }

    /**
     * Creates a new query tree with a root, a left and a right sub-tree
     * @param newRoot this root
     * @param left left root
     * @param right right root (can be null for a unary operation)
     */
    public QueryTree(String newRoot, QueryTree left, QueryTree right)
    {	//création d'un arbre avec enfants
    		root = new QueryTreeNode(newRoot, left.root, right.root);
    }

    public QueryTreeNode getRoot()
    {
    	return root;
    }

    public Set<Posting> getResult(IInvertedIndex index)
    {	
    	return root.getResult(index);
    }

}