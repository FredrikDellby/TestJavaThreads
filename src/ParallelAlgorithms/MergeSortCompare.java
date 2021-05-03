package ParallelAlgorithms;

import java.util.Arrays;
import java.util.Random;

public class MergeSortCompare {

	public static final int SIZE_OF_ARRAY = 1000000;
	public static Random random = new Random();

	public static void main(String[] args) throws Throwable {
		int numOfThreads = Runtime.getRuntime().availableProcessors();
		System.out.println("Number of threads/cores: " + numOfThreads);
		System.out.println("");

		int[] numbersParallel = createRandomArray();
		int[] numbersSequential = Arrays.copyOf(numbersParallel, SIZE_OF_ARRAY);
		MergeSort mergeSortParallel = new MergeSort(numbersParallel);

		// System.out.println("Array("+LENGTH+") elements before sort: ");
		// Print first 10 elements
		// printIntArray(a);

		// run the algorithm and time how long it takes
		long startTime = System.currentTimeMillis();
		mergeSortParallel.parallelMergeSort(0, numbersParallel.length - 1, numOfThreads);
		//mergeSort.showResult();
		long endTime = System.currentTimeMillis();

		// System.out.println("Array("+LENGTH+") elements after sort: ");
		// Print first 10 elements
		// printIntArray(a);
		System.out.printf("Time taken for %d elements parallel => %4d ms \n", SIZE_OF_ARRAY, endTime - startTime);
		System.out.println("");


		// Do the sequential mergeSort

		MergeSort mergeSortSequential = new MergeSort(numbersSequential);
		startTime = System.currentTimeMillis();
		mergeSortSequential.mergeSort(0, numbersSequential.length - 1);
		endTime = System.currentTimeMillis();

		// System.out.println("Array("+LENGTH+") elements after sort: ");
		// Print first 10 elements
		// printIntArray(a);
		System.out.printf("Time taken for %d elements sequential => %4d ms \n", SIZE_OF_ARRAY, endTime - startTime);
		System.out.println("");
	}

	public static int[] createRandomArray() {
		int[] a = new int[SIZE_OF_ARRAY];
		for (int i = 0; i < SIZE_OF_ARRAY; i++) {
			a[i] = random.nextInt(10000);
		}
		return a;
	}
}

class MergeSort {

	private int[] nums;
	private int[] tempArray;

	public MergeSort(int[] nums) {
		this.nums = nums;
		tempArray = new int[nums.length];
	}

	public void parallelMergeSort(int low, int high, int numOfThreads) {

		if (numOfThreads <= 1) {
			mergeSort(low, high);
			return;
		}

		int middleIndex = (low + high) / 2;

		Thread leftSorter = mergeSortThread(low, middleIndex, numOfThreads);
		Thread rightSorter = mergeSortThread(middleIndex+1, high, numOfThreads);

		leftSorter.start();
		rightSorter.start();

		try {
			leftSorter.join();
			rightSorter.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		merge(low, middleIndex, high);
	}

	private Thread mergeSortThread(int low, int high, int numOfThreads) {

		return new Thread() {
			@Override
			public void run() {
				parallelMergeSort(low, high, numOfThreads / 2);
			}
		};
	}

	public void mergeSort(int low, int high) {
		// Base case
		if (low >= high) {
			return;
		}

		int middle = (low + high) / 2;

		mergeSort(low, middle);
		mergeSort(middle + 1, high);
		merge(low, middle, high);
	}

	public void showResult() {
		for (int i = 0; i < nums.length; ++i) {
			System.out.print(nums[i] + " ");
		}
	}

	private void merge(int low, int middle, int high) {

		// Copy nums[i] -> temp[i]
		for (int i = low; i <= high; i++) {
			tempArray[i] = nums[i];
		}

		int i = low;
		int j = middle + 1;
		int k = low;

		// Copy the smallest values from either the left or the right side back
		// to the original array
		while ((i <= middle) && (j <= high)) {
			if (tempArray[i] <= tempArray[j]) {
				nums[k] = tempArray[i];
				i++;
			} else {
				nums[k] = tempArray[j];
				j++;
			}

			k++;
		}

		// Copy the rest of the left side of the array into the target array
		while (i <= middle) {
			nums[k] = tempArray[i];
			k++;
			i++;
		}
	}
}
