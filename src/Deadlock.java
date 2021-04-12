package com.balazsholczer.udemy;

import java.util.concurrent.locks.*;

public class Deadlock {
	
	private Lock lock1 = new ReentrantLock(true);
	private Lock lock2 = new ReentrantLock(true);
	
	public static void main(String[] args) {
		
		Deadlock deadlock = new Deadlock();
		new Thread(deadlock::worker1, "worker1").start();
		new Thread(deadlock::worker2, "worker2").start();
	}
	
	public void worker1() {
		lock1.lock();
		System.out.println("Worker1 acquires lock1...");
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		lock2.lock();
		System.out.println("Worker1 acquires lock2...");
		lock1.unlock();
		lock2.unlock();
	}
	
	public void worker2() {
		lock1.lock();
		System.out.println("Worker2 acquires lock2...");
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		lock2.lock();
		System.out.println("Worker2 acquires lock1...");
		lock1.unlock();
		lock2.unlock();
	}

}
