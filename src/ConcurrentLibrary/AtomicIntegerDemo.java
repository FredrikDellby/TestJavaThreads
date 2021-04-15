package ConcurrentLibrary;

import java.util.concurrent.atomic.*;

public class AtomicIntegerDemo {
	private static AtomicInteger counter = new AtomicInteger(0);

	public static void main(String[] args) {
		Thread t1 = new Thread(AtomicIntegerDemo::increment, "thread1");
		Thread t2 = new Thread(AtomicIntegerDemo::increment, "thread2");
		
		t1.start(); t2.start();
		
		try {
			t1.join(); t2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("Counter: " + counter);
	}
	
	public static synchronized void increment() {
		for (int i=0; i<10000; i++)
			counter.getAndIncrement();
	}

}
