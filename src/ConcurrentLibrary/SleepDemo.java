package ConcurrentLibrary;

class SleepWorker implements Runnable {

    private boolean isTerminated = false;

    @Override
    public void run() {

        while(!isTerminated) {

            System.out.println("Hello from worker class...");

            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isTerminated() {
        return isTerminated;
    }

    public void setTerminated(boolean isTerminated) {
        this.isTerminated = isTerminated;
    }
}

public class SleepDemo {

    public static void main(String[] args) {

        SleepWorker worker = new SleepWorker();
        Thread t1 = new Thread(worker);
        t1.start();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        worker.setTerminated(true);
        System.out.println("Finished...");
    }
}
