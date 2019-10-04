
package app;

import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;

import structures.Arc;
import structures.Graph;

public class Driver {

	public static void main(String[] args)  throws IOException {
		// TODO Auto-generated method stub
			Scanner in = new Scanner(System.in);
		
			System.out.print("Type in the name of the graph: ");
			Graph graph = new Graph(in.nextLine());
			PartialTreeList list = PartialTreeList.initialize(graph);
	        ArrayList<Arc> arcArrayList = PartialTreeList.execute(list);
	        System.out.println("Results ");
	        System.out.print("[");
	        for (int i = 0; i < arcArrayList.size(); i++) {
	            Arc anArcArrayList = arcArrayList.get(i);
	            System.out.print(anArcArrayList + ", ");
	        }
	        System.out.println("]");
	        
	        in.close();
	}

}
