package ConcurrentLibrary;

// From lecture 11
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ReentrantWorker {

    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    //private List<Integer> list = new ArrayList<>();

    public void produce() throws InterruptedException {
        lock.lock();
        System.out.println("Producer method...");
        condition.await();
        System.out.println("Producer method again...");
        lock.unlock();
    }

    public void consume() throws InterruptedException {
        lock.lock();
        Thread.sleep(2000);
        System.out.println("Consumer method...");
        Thread.sleep(3000);
        condition.signal();
        lock.unlock();
    }
}

public class AwaitAndSignalDemo {
    public static void main(String[] args) {

        final ReentrantWorker worker = new ReentrantWorker();

        Thread t1 = new Thread(new Runnable() {
            public void run() {
                try {
                    worker.produce();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            public void run() {
                try {
                    worker.consume();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        t1.start();
        t2.start();

    }
}
