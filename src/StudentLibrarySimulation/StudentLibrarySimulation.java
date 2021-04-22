package StudentLibrarySimulation;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import java.util.Random;

public class StudentLibrarySimulation {
	
public static void main(String[] args) {
		
		Student[] students = null;
		Book[] books = null;
		ExecutorService executor = Executors.newFixedThreadPool(Constants.NUMBER_OF_STUDENTS);
		
		try {
			
			books = new Book[Constants.NUMBER_OF_BOOKS];
			students = new Student[Constants.NUMBER_OF_STUDENTS];
			
			
			for(int i=0;i<Constants.NUMBER_OF_BOOKS; i++) {
				books[i] = new Book(i);
			}
			
			for (int i=0; i<Constants.NUMBER_OF_STUDENTS; i++) {
				students[i] = new Student(i,books);
				executor.execute(students[i]);
			}						
		} catch (Exception e) {
			e.printStackTrace();
			//executor.shutdown();
		} finally {
			executor.shutdown();
		}
	}

}

class Book {

	private int id;
	private Lock lock;

	public Book(int id) {
		this.lock = new ReentrantLock();
		this.id = id;
	}

	public void read(Student student) throws InterruptedException {
		//lock.tryLock(10, TimeUnit.MINUTES);
		lock.lock();
		System.out.println(student + " starts reading " + this);
		Thread.sleep(2000);
		lock.unlock();
		System.out.println(student + " has finished reading " + this);
	}

	public String toString() {
		return "Book " + id;
	}

}

class Student implements Runnable {

	private int id;
	private Book[] books;

	public Student(int id, Book[] books) {
		this.books = books;
		this.id = id;
	}

	@Override
	public void run() {

		Random random = new Random();

		while (true) {

			int bookId = random.nextInt(Constants.NUMBER_OF_BOOKS);

			try {
				books[bookId].read(this);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public String toString() {
		return "Student" + id;
	}
}

class Constants {

	private Constants(){
		
	}
	
	public static final int NUMBER_OF_STUDENTS = 5;
	public static final int NUMBER_OF_BOOKS = 7;
}