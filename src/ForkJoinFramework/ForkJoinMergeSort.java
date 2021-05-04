package ForkJoinFramework;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class ForkJoinMergeSort {

	public static void main(String[] args) {

		int[] nums = initializeNums();
		SequentialMergesort mergesort = new SequentialMergesort(nums);

		long start = System.currentTimeMillis();
		mergesort.mergeSort(nums);
		System.out.println("Time taken with sequential sort: " + (System.currentTimeMillis() - start) + "ms");
		
		MergeSortTask rootTask = new MergeSortTask(nums);
		ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
		start = System.currentTimeMillis();
		pool.invoke(rootTask);
		System.out.println("Time taken with parallel sort: " + (System.currentTimeMillis() - start) + "ms");
		
		
	}

	private static int[] initializeNums() {

		Random random = new Random();

		int[] nums = new int[30000000];

		for (int i = 0; i < 30000000; ++i)
			nums[i] = random.nextInt(100);

		return nums;
	}
}

class SequentialMergesort {

	private int[] nums;

	public SequentialMergesort(int[] nums) {
		this.nums = nums;
	}

	public void mergeSort(int[] nums) {

		if (nums.length <= 1)
			return;

		int middleIndex = nums.length / 2;

		int[] left = Arrays.copyOfRange(nums, 0, middleIndex);
		int[] right = Arrays.copyOfRange(nums, middleIndex, nums.length);

		mergeSort(left);
		mergeSort(right);

		merge(left, right, nums);
	}

	private void merge(int[] leftSubarray, int[] rightSubarray, int[] originalArray) {

		int i = 0;
		int j = 0;
		int k = 0;

		while (i < leftSubarray.length && j < rightSubarray.length) {
			if (leftSubarray[i] < rightSubarray[j])
				originalArray[k++] = leftSubarray[i++];
			else
				originalArray[k++] = rightSubarray[j++];
		}

		while (i < leftSubarray.length)
			originalArray[k++] = leftSubarray[i++];

		while (j < rightSubarray.length)
			originalArray[k++] = rightSubarray[j++];
	}
}

class MergeSortTask extends RecursiveAction {

	private int[] nums;

	public MergeSortTask(int[] nums) {
		this.nums = nums;
	}

	@Override
	protected void compute() {
		if (nums.length <= 10) {
			mergeSort(nums);
			return;
		}

		int middleIndex = nums.length / 2;

		int[] leftSubarray = Arrays.copyOfRange(nums, 0, middleIndex);
		int[] rightSubarray = Arrays.copyOfRange(nums, middleIndex, nums.length);

		MergeSortTask leftSorter = new MergeSortTask(leftSubarray);
		MergeSortTask rightSorter = new MergeSortTask(rightSubarray);

		invokeAll(leftSorter, rightSorter);

		merge(leftSubarray, rightSubarray, nums);

	}

	public void mergeSort(int[] nums) {

		if (nums.length <= 1)
			return;

		int mid = nums.length / 2;

		int[] left = Arrays.copyOfRange(nums, 0, mid);
		int[] right = Arrays.copyOfRange(nums, mid, nums.length);

		mergeSort(left);
		mergeSort(right);

		merge(left, right, nums);
	}

	private void merge(int[] leftSubarray, int[] rightSubarray, int[] originalArray) {

		int i = 0;
		int j = 0;
		int k = 0;

		while (i < leftSubarray.length && j < rightSubarray.length) {
			if (leftSubarray[i] < rightSubarray[j])
				originalArray[k++] = leftSubarray[i++];
			else
				originalArray[k++] = rightSubarray[j++];
		}

		while (i < leftSubarray.length)
			originalArray[k++] = leftSubarray[i++];

		while (j < rightSubarray.length)
			originalArray[k++] = rightSubarray[j++];
	}

}
