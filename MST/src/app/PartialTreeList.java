package app;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import structures.Arc;
import structures.Graph;
import structures.MinHeap;
import structures.PartialTree;
import structures.Vertex;

/**
 * Stores partial trees in a circular linked list
 * 
 */
public class PartialTreeList implements Iterable<PartialTree> {
    
	/**
	 * Inner class - to build the partial tree circular linked list 
	 * 
	 */
	public static class Node {
		/**
		 * Partial tree
		 */
		public PartialTree tree;
		
		/**
		 * Next node in linked list
		 */
		public Node next;
		
		/**
		 * Initializes this node by setting the tree part to the given tree,
		 * and setting next part to null
		 * 
		 * @param tree Partial tree
		 */
		public Node(PartialTree tree) {
			this.tree = tree;
			next = null;
		}
	}

	/**
	 * Pointer to last node of the circular linked list
	 */
	private Node rear;
	
	/**
	 * Number of nodes in the CLL
	 */
	private int size;
	
	/**
	 * Initializes this list to empty
	 */
    public PartialTreeList() {
    	rear = null;
    	size = 0;
    }

    /**
     * Adds a new tree to the end of theist
     * 
     * @param tree Tree to be added to the end of the list
     */
    public void append(PartialTree tree) {
    	Node ptr = new Node(tree);
    	if (rear == null) {
    		ptr.next = ptr;
    	} else {
    		ptr.next = rear.next;
    		rear.next = ptr;
    	}
    	rear = ptr;
    	size++;
    }

    /**
	 * Initializes the algorithm by building single-vertex partial trees
	 * 
	 * @param graph Graph for which the MST is to be found
	 * @return The initial partial tree list
	 */
	public static PartialTreeList initialize(Graph graph) {
	
		/* COMPLETE THIS METHOD */
		
		PartialTreeList result = new PartialTreeList(); //Create empty list of partial trees
		
		for (Vertex temp : graph.vertices) {
			PartialTree pt = new PartialTree(temp);
			temp.parent = pt.getRoot();
			MinHeap<Arc> pq = pt.getArcs();
			Vertex.Neighbor neighbor = temp.neighbors;
			while (neighbor != null) {
				Arc arc = new Arc(temp, neighbor.vertex, neighbor.weight);
				pq.insert(arc);
				neighbor = neighbor.next;
			}
			result.append(pt);
		}		
		return result;
		
	}
	

	
	/**
	 * Executes the algorithm on a graph, starting with the initial partial tree list
	 * for that graph
	 * 
	 * @param ptlist Initial partial tree list
	 * @return Array list of all arcs that are in the MST - sequence of arcs is irrelevant
	 */
	public static ArrayList<Arc> execute(PartialTreeList ptlist) {
		
		/* COMPLETE THIS METHOD */
		
		ArrayList<Arc> result = new ArrayList<Arc>();
		while (ptlist.size > 1) {
			PartialTree ptx = ptlist.remove();
			MinHeap<Arc> pqx = ptx.getArcs();
			Arc highest = pqx.deleteMin();
			Vertex v2 = highest.getv2();
			while (ptlist.isInTree(v2, ptx)) {
				highest = pqx.deleteMin();
				v2 = highest.getv2();
			}
			System.out.println(highest + " " + "is a component of the MST");
			result.add(highest);
			//Find pty to which v2 belongs to
			PartialTree pty = ptlist.removeTreeContaining(v2);
			ptx.merge(pty);
			ptlist.append(ptx);
		}
		return result;
		
	}
	
	  
    /**
     * Removes the tree that is at the front of the list.
     * 
     * @return The tree that is removed from the front
     * @throws NoSuchElementException If the list is empty
     */
    public PartialTree remove() 
    throws NoSuchElementException {
    			
    	if (rear == null) {
    		throw new NoSuchElementException("list is empty");
    	}
    	PartialTree ret = rear.next.tree;
    	if (rear.next == rear) {
    		rear = null;
    	} else {
    		rear.next = rear.next.next;
    	}
    	size--;
    	return ret;
    		
    }

    /**
     * Removes the tree in this list that contains a given vertex.
     * 
     * @param vertex Vertex whose tree is to be removed
     * @return The tree that is removed
     * @throws NoSuchElementException If there is no matching tree
     */
    public PartialTree removeTreeContaining(Vertex vertex) 
    throws NoSuchElementException {
    		/* COMPLETE THIS METHOD */
    	if (size == 0)
    		throw new NoSuchElementException("No tree in this list");
    	
    	Node ptr = rear.next;
    	Node prev = rear;
    	do {
    		PartialTree pt = ptr.tree;
    		if (isInTree(vertex, pt)) {
    			if (size == 1) {
    				rear = null;
    				size--;
    				return pt;
    			}
    			if (rear == ptr) {
    				prev.next = ptr.next;
    				rear = prev;
    				size--;
    				return pt;
    			}
   				prev.next = ptr.next;
   				size--;
    			return pt;
    	
    		}
    		prev = ptr;
    		ptr = ptr.next;
    	} while (ptr != rear.next);
    	
    	throw new NoSuchElementException("No tree in list with this vertex");
  	
     }
   
     private boolean isInTree(Vertex vertex, PartialTree pt) {
    	 
    	 while (vertex != null) {
    		 if (pt.getRoot() == vertex) {
    			 return true;
    		 } else if (vertex.parent == vertex) {
    			 return false;
    		 }
    		 vertex = vertex.parent;
    	 }
    	 return false;
    	 
     }
    
    
    /**
     * Gives the number of trees in this list
     * 
     * @return Number of trees
     */
    public int size() {
    	return size;
    }
    
    /**
     * Returns an Iterator that can be used to step through the trees in this list.
     * The iterator does NOT support remove.
     * 
     * @return Iterator for this list
     */
    public Iterator<PartialTree> iterator() {
    	return new PartialTreeListIterator(this);
    }
    
    private class PartialTreeListIterator implements Iterator<PartialTree> {
    	
    	private PartialTreeList.Node ptr;
    	private int rest;
    	
    	public PartialTreeListIterator(PartialTreeList target) {
    		rest = target.size;
    		ptr = rest > 0 ? target.rear.next : null;
    	}
    	
    	public PartialTree next() 
    	throws NoSuchElementException {
    		if (rest <= 0) {
    			throw new NoSuchElementException();
    		}
    		PartialTree ret = ptr.tree;
    		ptr = ptr.next;
    		rest--;
    		return ret;
    	}
    	
    	public boolean hasNext() {
    		return rest != 0;
    	}
    	
    	public void remove() 
    	throws UnsupportedOperationException {
    		throw new UnsupportedOperationException();
    	}
    	
    }
}


