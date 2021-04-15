package dellby.fredrik.threads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

	/**
	 * 
	 *  1.) ExecutorService es = Executors.newCachedThreadPool();
	 *  	- going to return an executorService that can dynamically
	 *  		reuse threads
	 *		- before starting a job -> it going to check whether there are any threads that
	 *			finished the job...reuse them
	 *		- if there are no waiting threads -> it is going to create another one 
	 *		- good for the processor ... effective solution !!!
	 *
	 *	2.) ExecutorService es = Executors.newFixedThreadPool(N);
	 *		- maximize the number of threads
	 *		- if we want to start a job -> if all the threads are busy, we have to wait for one
	 *			to terminate
	 *
	 *	3.) ExecutorService es = Executors.newSingleThreadExecutor();
	 *		It uses a single thread for the job
	 *
	 *		execute() -> runnable + callable
	 *		submit() -> runnable
	
	 */

class Counter implements Runnable{

		@Override
		public void run() {
			for(int i=0;i<50;i++){
				System.out.println(i);
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
}

class Task implements Runnable {
	private int id;
	
	public Task(int id) {
		this.id = id;
	}

	@Override
	public void run() {
		System.out.println("Task with id " + id + " is in work - thread id: " + Thread.currentThread().getId());
		long duration = (long) (Math.random() * 5 + 1);
		
		try {
			TimeUnit.SECONDS.sleep(duration);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}

class StockMarketUpdater implements Runnable {
	@Override
	public void run() {
		System.out.println("Updating and downloading stock market data from the web...");
	}
}

public class ExecutorServiceDemo {
	public static void main(String[] args) {
		
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		//ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		//executor.scheduleAtFixedRate(new StockMarketUpdater(), 1, 2, TimeUnit.SECONDS);
		
		for(int i=0; i<100; i++) {
			executorService.execute(new Task(i + 1));
		}
		// Prevent the executor to execute any FURTHER tasks
		executorService.shutdown();
		
		try {
			if (!executorService.awaitTermination(1000, TimeUnit.MILLISECONDS)) {
				//executorService.shutdownNow();
			}
		} catch (InterruptedException e) {
			executorService.shutdownNow();
		}
	}
}