package DiningPhilosophersDemo;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class App {
	
	/*
	 * The aim of this simulation is that it's possible to avoid thread starvation:
	 * - All threads are going to be executed by the executorService
	 * - We're able to avoid deadlocks because we're using trylock() method
	 */

	public static void main(String[] args) throws InterruptedException {
		
		ExecutorService executorService = null;
		Philosopher[] philosophers = null;
		
		try {
			
			philosophers = new Philosopher[Constants.NUMBER_OF_PHILOSOPHERS];
			ChopStick[] chopSticks = new ChopStick[Constants.NUMBER_OF_PHILOSOPHERS];
			
			for (int i=0; i<Constants.NUMBER_OF_CHOPSTICKS; i++) {
				chopSticks[i] = new ChopStick(i);
			}
			
			executorService = Executors.newFixedThreadPool(Constants.NUMBER_OF_PHILOSOPHERS);
			
			for (int i=0; i<Constants.NUMBER_OF_PHILOSOPHERS; i++) {
				philosophers[i] = new Philosopher(i, chopSticks[i], chopSticks[(i+1) % Constants.NUMBER_OF_PHILOSOPHERS]);
				executorService.execute(philosophers[i]);
			}
			
			Thread.sleep(Constants.SIMULATION_RUNNING_TIME);
			
			for (Philosopher philosopher : philosophers) {
				philosopher.setFull(true);
			}
		} finally {

			executorService.shutdown();

			while (!executorService.isTerminated()) {
				Thread.sleep(1000);
			}

			for (Philosopher philosopher : philosophers) {
				System.out.println(philosopher + " eat #" + philosopher.getEatingCounter());
			}
		}
	}
}

class ChopStick {

	private Lock lock;
	private int id;
	
	public ChopStick(int id) {
		this.id = id;
		this.lock = new ReentrantLock();
	}
	
	public boolean pickUp(Philosopher philosopher, State state) throws InterruptedException {
	
		if (this.lock.tryLock(10, TimeUnit.MILLISECONDS)) {
			System.out.println(philosopher + " picked up " + state.toString() + " " + this);
			return true;
		}
		
		return false;
	}
	
	public void putDown(Philosopher philosopher, State state) {
		this.lock.unlock();
		System.out.println(philosopher + " put down " + state.toString() + this);
	}
	
	@Override
	public String toString() {
		return "Chopstick " + this.id;
	}
}

class Philosopher implements Runnable {

	private int id;
	private ChopStick leftChopStick;
	private ChopStick rightChopStick;
	private volatile boolean isFull = false;
	private Random random;
	private int eatingCounter;
	
	public Philosopher(int id, ChopStick leftChopStick, ChopStick rightChopStick) {
		this.id = id;
		this.leftChopStick = leftChopStick;
		this.rightChopStick = rightChopStick;
		this.random = new Random();
	}
	
	@Override
	public void run() {
		try {
			while(!isFull ) {
				think();
				if (leftChopStick.pickUp(this, State.LEFT) ) {
					if (rightChopStick.pickUp(this, State.RIGHT)) {
						eat();
						rightChopStick.putDown(this, State.RIGHT);
					}
					leftChopStick.putDown(this, State.LEFT);
				}	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void think() throws InterruptedException {
		System.out.println(this+" is thinking...");
		Thread.sleep(this.random.nextInt(1000));
	}
	
	private void eat() throws InterruptedException {
		System.out.println(this+" is eating...");
		this.eatingCounter++;
		Thread.sleep(this.random.nextInt(1000));
	}
	
	public int getEatingCounter(){
		return this.eatingCounter;
	}
	
	public void setFull(boolean isFull){
		this.isFull = isFull;
	}
	
	@Override
	public String toString() {
		return "Philosopher-"+this.id;
	}
}

class Constants {

	private Constants() {
		
	}
	
	public static final int NUMBER_OF_PHILOSOPHERS = 5;
	public static final int NUMBER_OF_CHOPSTICKS = NUMBER_OF_PHILOSOPHERS;
	public static final int SIMULATION_RUNNING_TIME = 5*1000;
}

enum State {
	LEFT, RIGHT;
}