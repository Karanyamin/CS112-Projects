package poly;

import java.io.IOException;
import java.util.Scanner;

/**
 * This class implements evaluate, add and multiply for polynomials.
 * 
 * @author runb-cs112
 *
 */
public class Polynomial {
	
	/**
	 * Reads a polynomial from an input stream (file or keyboard). The storage format
	 * of the polynomial is:
	 * <pre>
	 *     <coeff> <degree>
	 *     <coeff> <degree>
	 *     ...
	 *     <coeff> <degree>
	 * </pre>
	 * with the guarantee that degrees will be in descending order. For example:
	 * <pre>
	 *      4 5
	 *     -2 3
	 *      2 1
	 *      3 0
	 * </pre>
	 * which represents the polynomial:
	 * <pre>
	 *      4*x^5 - 2*x^3 + 2*x + 3 
	 * </pre>
	 * 
	 * @param sc Scanner from which a polynomial is to be read
	 * @throws IOException If there is any input error in reading the polynomial
	 * @return The polynomial linked list (front node) constructed from coefficients and
	 *         degrees read from scanner
	 */
	public static Node read(Scanner sc) 
	throws IOException {
		Node poly = null;
		while (sc.hasNextLine()) {
			Scanner scLine = new Scanner(sc.nextLine());
			poly = new Node(scLine.nextFloat(), scLine.nextInt(), poly);
			scLine.close();
		}
		return poly;
	}
	
	/**
	 * Returns the sum of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list
	 * @return A new polynomial which is the sum of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node add(Node poly1, Node poly2) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		Node ptr1 = poly1;
		Node ptr2 = poly2;
		Node newListFront = null;
		while (ptr1 != null && ptr2 != null){
			if(ptr1.term.degree == ptr2.term.degree){
				Node tempPtr = newListFront;
				if (tempPtr == null) {
					if (((ptr1.term.coeff + ptr2.term.coeff) != 0)){
						newListFront = new Node(ptr1.term.coeff + ptr2.term.coeff, ptr1.term.degree, null);
					}
				} else {
					while (tempPtr.next != null){
						tempPtr = tempPtr.next; 
					}
					if (((ptr1.term.coeff + ptr2.term.coeff) != 0)){
						tempPtr.next = new Node(ptr1.term.coeff + ptr2.term.coeff, ptr1.term.degree, null);
					}
				}
				ptr1 = ptr1.next;
				ptr2 = ptr2.next;
			} else if (ptr1.term.degree < ptr2.term.degree){
				Node tempPtr = newListFront;
				if (tempPtr == null) {
					newListFront = new Node(ptr1.term.coeff, ptr1.term.degree, null);

				} else {
					while (tempPtr.next != null){
						tempPtr = tempPtr.next; 
					}
					tempPtr.next = new Node(ptr1.term.coeff, ptr1.term.degree, null);

				}
				ptr1 = ptr1.next;
			} else if (ptr2.term.degree < ptr1.term.degree){
				Node tempPtr = newListFront;
				if (tempPtr == null) {
					newListFront = new Node(ptr2.term.coeff, ptr2.term.degree, null);

				} else {
					while (tempPtr.next != null){
						tempPtr = tempPtr.next; 
					}
					tempPtr.next = new Node(ptr2.term.coeff, ptr2.term.degree, null);
				}
				ptr2 = ptr2.next;
			}
		}

		if (ptr1 == null && ptr2 != null){
			Node tempPtr = newListFront;
			if (tempPtr == null){
				newListFront = new Node(ptr2.term.coeff, ptr2.term.degree, null);
				tempPtr = newListFront;
				ptr2 = ptr2.next;
				while (ptr2 != null){
					tempPtr.next = new Node(ptr2.term.coeff, ptr2.term.degree, null);
					ptr2 = ptr2.next;
					tempPtr = tempPtr.next;
				}
			} else {
				while (tempPtr.next != null){
					tempPtr = tempPtr.next;
				}
				while (ptr2 != null){
					tempPtr.next = new Node(ptr2.term.coeff, ptr2.term.degree, null);
					ptr2 = ptr2.next;
					tempPtr = tempPtr.next;
				}
			}
		} else if (ptr1 != null && ptr2 == null){
			Node tempPtr = newListFront;
			if (tempPtr == null){
				newListFront = new Node(ptr1.term.coeff, ptr1.term.degree, null);
				tempPtr = newListFront;
				ptr1 = ptr1.next;
				while (ptr1 != null){
					tempPtr.next = new Node(ptr1.term.coeff, ptr1.term.degree, null);
					ptr1 = ptr1.next;
					tempPtr = tempPtr.next;
				}
			} else {
				while (tempPtr.next != null){
					tempPtr = tempPtr.next;
				}
				while (ptr1 != null){
					tempPtr.next = new Node(ptr1.term.coeff, ptr1.term.degree, null);
					ptr1 = ptr1.next;
					tempPtr = tempPtr.next;
				}
			}
		}
		return newListFront;
		
	}
	
	/**
	 * Returns the product of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list)
	 * @return A new polynomial which is the product of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node multiply(Node poly1, Node poly2) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		if (poly1 == null || poly2 == null) return null;
		Node ptr1 = poly1;
		Node ptr2 = poly2;
		Node newFront = null;
		int currentDegree = 0;
		while (ptr1.next != null){
			ptr1 = ptr1.next;
		}
		while (ptr2.next != null){
			ptr2 = ptr2.next;
		}
		currentDegree = ptr1.term.degree + ptr2.term.degree;
		while (currentDegree >= 0){
			float add = 0;
			for (ptr1 = poly1; ptr1 != null; ptr1 = ptr1.next){
				for (ptr2 = poly2; ptr2 != null; ptr2 = ptr2.next){
					if ((ptr1.term.degree + ptr2.term.degree) == currentDegree){
						add += ptr1.term.coeff * ptr2.term.coeff;
					}
				}
			}
			newFront = new Node(add, currentDegree, newFront);
			currentDegree--;
		}
		Node ptr3 = newFront;
		int pos = 0;
		while (ptr3 != null){
			if (ptr3.term.coeff == 0){
				newFront = deleteNode(newFront, pos);
				pos--;
			}
			pos++;
			ptr3 = ptr3.next;
		}
		return newFront;
	}
	
	private static Node deleteNode(Node head, int pos){
		Node temp = head;
		if (pos == 0){
			return temp.next;
		}

		for (int i=0; temp!=null && i<pos-1; i++)
            temp = temp.next;

	 	temp.next = temp.next.next;

		return head;
	}
		
	/**
	 * Evaluates a polynomial at a given value.
	 * 
	 * @param poly Polynomial (front of linked list) to be evaluated
	 * @param x Value at which evaluation is to be done
	 * @return Value of polynomial p at x
	 */
	public static float evaluate(Node poly, float x) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		float temp = 0;
		for (Node ptr = poly; ptr != null; ptr = ptr.next){
			temp += ptr.term.coeff * (float)Math.pow(x, ptr.term.degree);
		}
		return temp;
	}
	
	/**
	 * Returns string representation of a polynomial
	 * 
	 * @param poly Polynomial (front of linked list)
	 * @return String representation, in descending order of degrees
	 */
	public static String toString(Node poly) {
		if (poly == null) {
			return "0";
		} 
		
		String retval = poly.term.toString();
		for (Node current = poly.next ; current != null ;
		current = current.next) {
			retval = current.term.toString() + " + " + retval;
		}
		return retval;
	}	
}
