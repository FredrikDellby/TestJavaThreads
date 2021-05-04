package ForkJoinFramework;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ForkJoinMax {

	public static int THREASHOLD = 0;
	private static int ARRAY_SIZE = 80000000;
	
	public static void main(String[] args) {
		
		long[] nums = initializeNums();
		THREASHOLD =  nums.length / Runtime.getRuntime().availableProcessors();
		
		SequentialMaxFind normalMaxFind = new SequentialMaxFind();
		
		long start = System.currentTimeMillis();
		System.out.println("Max: " + normalMaxFind.sequentialMaxFinding(nums, nums.length));
		System.out.println("Time taken: " + (System.currentTimeMillis() - start) + "ms");

		System.out.println();
		
		ForkJoinPool forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
		ParallelMaxFindTask findTask = new ParallelMaxFindTask(nums, 0, nums.length);
		
		start = System.currentTimeMillis();
		System.out.println("Max: " + forkJoinPool.invoke(findTask));
		System.out.println("Time taken: " + (System.currentTimeMillis() - start) + "ms");
	}

	private static long[] initializeNums() {
		
		Random random = new Random();
		long[] nums = new long[ARRAY_SIZE];
		
		for(int i=0; i<ARRAY_SIZE; i++)
			nums[i] = random.nextInt(1000);
		
		return nums;
	}
}

class ParallelMaxFindTask extends RecursiveTask<Long> {

	private long[] nums;
	private int lowIndex;
	private int highIndex;

	public ParallelMaxFindTask(long[] nums, int lowIndex, int highIndex) {
		this.highIndex = highIndex;
		this.lowIndex = lowIndex;
		this.nums = nums;
	}

	@Override
	protected Long compute() {

		if (highIndex - lowIndex < ForkJoinMax.THREASHOLD) {
			return sequentialMaxFinding();
		} else {

			int middleIndex = (lowIndex+highIndex) / 2;

			ParallelMaxFindTask leftSubtask = new ParallelMaxFindTask(nums, lowIndex, middleIndex);
			ParallelMaxFindTask rightSubtask = new ParallelMaxFindTask(nums, middleIndex, highIndex);

			invokeAll(leftSubtask, rightSubtask);

			return Math.max(leftSubtask.join(), rightSubtask.join());
		}
	}

	private long sequentialMaxFinding() {

		long max = nums[0];

		for(int i=lowIndex; i<highIndex;++i)
			if( nums[i] > max)
				max = nums[i];

		return max;
	}
}

class SequentialMaxFind {

	// O(N) time complexity
	public long sequentialMaxFinding(long[] nums, int highIndex) {
		long max = nums[0];

		for (int i = 0; i < highIndex; ++i)
			if (nums[i] > max)
				max = nums[i];

		return max;
	}
}
