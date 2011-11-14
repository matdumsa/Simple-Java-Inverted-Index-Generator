//Mathieu Dumais-Savard
//TP4 18 Mars 2009
//ArithmeticTree.java
package info.mathieusavard.domain.queryprocessor.booleantree;

import info.mathieusavard.domain.Posting;
import info.mathieusavard.domain.index.spimi.IInvertedIndex;

import java.util.Set;

public class QueryTree
{
    private QueryTreeNode root;
        
    QueryTree(String elem)
    { //Nous recevons qu'une string alors cr�ons un arbre avec un seul noeud repr�sent� par cette string
    	root = new QueryTreeNode(elem);
    }

    /**
     * Creates a new query tree with a root, a left and a right sub-tree
     * @param newRoot this root
     * @param left left root
     * @param right right root (can be null for a unary operation)
     */
    QueryTree(String newRoot, QueryTree left, QueryTree right)
    {	//cr�ation d'un arbre avec enfants
    		root = new QueryTreeNode(newRoot, left.root, right.root);
    }

    QueryTreeNode getRoot()
    {
    	return root;
    }

    public Set<Posting> getResult(IInvertedIndex index)
    {	
    	QueryTreeNode.queryTerms="";
    	return root.getResult(index);
    }
    
    public String getAllMatchedTerms() {
    	return QueryTreeNode.queryTerms;
    }

}