package ForkJoinFramework;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class SimpleRecursiveActionDemo {

	public static void main(String[] args) {
		
		ForkJoinPool forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
		SimpleRecursiveAction simpleRecursiveAction = new SimpleRecursiveAction(400);
		forkJoinPool.invoke(simpleRecursiveAction);
	}
}

class SimpleRecursiveAction extends RecursiveAction {

	private int simulatedWork;

	public SimpleRecursiveAction(int simulatedWork) {
		this.simulatedWork = simulatedWork;
	}

	@Override
	protected void compute() {

		if( simulatedWork > 100 ) {

			System.out.println("Parallel execution and split the tasks..." + simulatedWork);

			SimpleRecursiveAction simpleRecursiveAction1 = new SimpleRecursiveAction(simulatedWork/2);
			SimpleRecursiveAction simpleRecursiveAction2 = new SimpleRecursiveAction(simulatedWork/2);

			simpleRecursiveAction1.fork();
			simpleRecursiveAction2.fork();

			//simpleRecursiveAction1.join();
			//simpleRecursiveAction2.join();
		} else {
			System.out.println("No need for parallel execution, sequential is OK for this task..." + simulatedWork);
		}
	}
}
