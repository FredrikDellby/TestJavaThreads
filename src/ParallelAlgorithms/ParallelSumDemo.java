package ParallelAlgorithms;

import java.util.Random;

public class ParallelSumDemo {

	public static void main(String[] args) {
		
		Random random = new Random();
		SequentialSum sequentialSum = new SequentialSum();
		
		int numOfProcessors = Runtime.getRuntime().availableProcessors();

	    int[] nums = new int[100000000];

	    for (int i = 0; i < nums.length; i++) {
	        nums[i] = random.nextInt(101) + 1; // 1..100
	    }

	    long start = System.currentTimeMillis();

	    System.out.println("Sum is: " + sequentialSum.sum(nums));

	    System.out.println("Single: " + (System.currentTimeMillis() - start)+"ms"); // Single: 44

	    start = System.currentTimeMillis();

	    ParallelSum parallelSum = new ParallelSum(numOfProcessors);
	    System.out.println("Sum is: " +parallelSum.parallelSum(nums));

	    System.out.println("Parallel: " + (System.currentTimeMillis() - start)+ "ms"); // Parallel: 25
		
	}
}

class SequentialSum {

	public int sum(int[] nums) {

		int total = 0;

		for (int i = 0; i < nums.length; ++i) {
			total += nums[i];
		}

		return total;
	}
}

class ParallelSum {

	private ParallelWorker[] sums;
	private int numOfThreads;

	public ParallelSum(int numOfThreads) {
		this.sums = new ParallelWorker[numOfThreads];
		this.numOfThreads = numOfThreads;
	}

	public int parallelSum(int[] nums) {

		int size = (int) Math.ceil(nums.length * 1.0 / numOfThreads);


		for (int i = 0; i < numOfThreads; i++) {
			sums[i] = new ParallelWorker(nums, i * size, (i + 1) * size);
			sums[i].start();
		}

		try {
			for (ParallelWorker sum : sums) {
				sum.join();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		int total = 0;

		for (ParallelWorker sum : sums) {
			total += sum.getPartialSum();
		}

		return total;
	}

}

class ParallelWorker extends Thread {

	private int[] nums;
	private int low;
	private int high;
	private int partialSum;

	public ParallelWorker(int[] nums, int low, int high) {
		this.nums = nums;
		this.low = low;
		this.high = Math.min(high, nums.length);
	}

	public int getPartialSum() {
		return partialSum;
	}

	@Override
	public void run() {

		partialSum = 0;

		for (int i = low; i < high; i++) {
			partialSum += nums[i];
		}
	}
}
