package assignment4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ForkJoinPool;

public class Main {
	private static ForkJoinPool pool;
	public static void shutdownPool(){
		pool.shutdownNow();

	}
	public static void main(String[] args) throws InterruptedException {
		Scanner s = new Scanner(System.in);
		System.out.println("Enter the number of nodes in the tree: ");
		Tree tree = new Tree(s.nextInt());
		long startTime = System.nanoTime();
		tree.generateTree();
		long endTime = System.nanoTime();
		//divide by 1000000 to get milliseconds.
		System.out.println("Time taken: " + (endTime - startTime));
		System.out.println("Height: " + tree.getHeight());
		System.out.print("Input number of nodes to check: ");
		int numOfNodes = s.nextInt();
		HashMap<Integer, Integer> nodesToCheck = new HashMap<>(numOfNodes);
		for(int i = 0; i < numOfNodes; i++){
			//hashmap of node number and height
			nodesToCheck.put(s.nextInt(), -1);
		}
		System.out.println("Input the technique to check the program:\n1) Explicit Multithreading\n2) ForkJoinPool");
		int technique = s.nextInt();
		System.out.print("Enter number of threads to be used: ");
		int numThreads = s.nextInt();
		switch(technique){
			case 1:
//				explicit multithreading code elided
				System.out.println("Explicit");
				break;
			case 2:
				System.out.println("ForkJoinPool");
				pool = new ForkJoinPool(numThreads);
				//root task created
				TreeForkJoinPool task = new TreeForkJoinPool(tree.getRoot(), 0, nodesToCheck);
				//speculative parallelism thread shutdown
				try {
					pool.invoke(task);
				}
				catch (CancellationException e){
					//All threads have found the values abort

				}
				task.printResult();
				break;
			default:
				System.out.println("Wrong input");
		}
	}
}
