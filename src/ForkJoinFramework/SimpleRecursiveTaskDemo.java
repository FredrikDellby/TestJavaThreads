package ForkJoinFramework;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class SimpleRecursiveTaskDemo {

	public static void main(String[] args) {
		
		ForkJoinPool forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
		SimpleRecursiveTask simpleRecursiveTask = new SimpleRecursiveTask(200);
		System.out.println( forkJoinPool.invoke(simpleRecursiveTask) );
	}
}

class SimpleRecursiveTask extends RecursiveTask<Integer> {

	private int simulatedWork;

	public SimpleRecursiveTask(int simulatedWork) {
		this.simulatedWork = simulatedWork;
	}

	@Override
	protected Integer compute() {

		if (simulatedWork > 100) {

			System.out.println("Parallel execution and split the tasks (" + simulatedWork + ")");

			SimpleRecursiveTask simpleRecursiveTask1 = new SimpleRecursiveTask(simulatedWork/2);
			SimpleRecursiveTask simpleRecursiveTask2 = new SimpleRecursiveTask(simulatedWork/2);

			simpleRecursiveTask1.fork();
			simpleRecursiveTask2.fork();

			int solution = 0;
			solution = solution + simpleRecursiveTask1.join();
			solution = solution + simpleRecursiveTask2.join();

			return solution;

		} else {
			System.out.println("No need for parallel execution, sequential is OK for this task (" + simulatedWork + ")" );
			return 2 * simulatedWork;
		}
	}
}
