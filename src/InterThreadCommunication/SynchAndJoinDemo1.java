package InterThreadCommunication;

// BasicMultithreading/Lecture6
// Section 4: Lecture 14-15
public class SynchAndJoinDemo1 {
    private static int counter = 0;

    public static synchronized void increment() {
        ++counter;
    }

    public static void process() {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; ++i) {
                    increment();
                    System.out.println("Thread 1");
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; ++i) {
                    increment();
                    System.out.println("Thread 2");
                }
            }
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        process();
        System.out.println(counter);
    }
}
