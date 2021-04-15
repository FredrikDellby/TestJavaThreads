package ConcurrentLibrary;

class VolatileWorker implements Runnable {
	private volatile boolean terminated;
	
	@Override
	public void run() {
		while (!terminated) {
			System.out.println("Working class is running...");
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean isTerminated() {
		return terminated;
	}
	
	public void setTerminated(boolean terminated) {
		this.terminated = terminated;
	}
}

public class Volatile {
	
	public static void main(String[] args) {
		VolatileWorker VolatileWorker = new VolatileWorker();
		Thread t1 = new Thread(VolatileWorker);
		t1.start();
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		VolatileWorker.setTerminated(true);
		System.out.println("Algorithm is terminated...");
	}
	
	
	
}
