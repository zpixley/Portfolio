import java.util.*;
import java.io.*;

public class Assig4
{
	public static int MIN_SIZE = 5; // for quick sort
	
	public static void main(String args[])
	{
		Scanner inScan = new Scanner(System.in);
		boolean traceOutputMode = false;
		System.out.print("Enter array size: ");
		int size = inScan.nextInt();
		System.out.print("Enter number of trials: ");
		int trials = inScan.nextInt();
		System.out.print("Enter file name: ");
		String fileName = inScan.next();
		try{
			PrintWriter writer = new PrintWriter(fileName);
			if (size <= 20)
				traceOutputMode = true;
			randomTest(size, trials, writer, traceOutputMode);
			sortedTest(size, trials, writer, traceOutputMode);
			reverseTest(size, trials, writer, traceOutputMode);
			writer.close();
		}catch(IOException e){}
	}
	
	public static void randomTest(int size, int trials, PrintWriter writer, boolean traceOutputMode)
	{
		Integer[] array = new Integer[size];
		double average = 0;
		if (size <= 100000)
		{
			// Simple Quicksort
			for (int i = 0; i < trials; i++)
			{
				Random rand = new Random(i);
				for (int j = 0; j < size; j++)
					array[j] = rand.nextInt();
				if (traceOutputMode)
				{
					System.out.println("Simple Quicksort");
					System.out.println(size);
					System.out.println("Random");
					for (Integer j : array)
						System.out.print(j + " ");
					System.out.println();
				}
				long start = System.nanoTime();
				simpleQuickSort(array, size);
				long finish = System.nanoTime();
				double delta = finish - start;
				average += delta;
				if (traceOutputMode)
				{
					for (Integer j : array)
						System.out.print(j + " ");
					System.out.println();
				}
			}
			average = average / trials / 1000000000;
			writer.println("Algorithm: Simple Quicksort");
			writer.println("Array Size: " + size);
			writer.println("Order: Random");
			writer.println("Number of trials: " + trials);
			writer.println("Average Time: " + average + " sec.");
			writer.println();
			// Insertion Sort
			average = 0;
			for (int i = 0; i < trials; i++)
			{
				Random rand = new Random(i);
				for (int j = 0; j < size; j++)
					array[j] = rand.nextInt();
				if (traceOutputMode)
				{
					System.out.println("Insertion Sort");
					System.out.println(size);
					System.out.println("Random");
					for (Integer j : array)
						System.out.print(j + " ");
					System.out.println();
				}
				long start = System.nanoTime();
				insertionSort(array, array.length);
				long finish = System.nanoTime();
				long delta = finish - start;
				average += delta;
				if (traceOutputMode)
				{
					for (Integer j : array)
						System.out.print(j + " ");
					System.out.println();
				}
			}
			average = average / trials / 1000000000;
			writer.println("Algorithm: Insertion Sort");
			writer.println("Array Size: " + size);
			writer.println("Order: Random");
			writer.println("Number of trials: " + trials);
			writer.println("Average Time: " + average + " sec.");
			writer.println();
			// Shell Sort
			average = 0;
			for (int i = 0; i < trials; i++)
			{
				Random rand = new Random(i);
				for (int j = 0; j < size; j++)
					array[j] = rand.nextInt();
				if (traceOutputMode)
				{
					System.out.println("Shell Sort");
					System.out.println(size);
					System.out.println("Random");
					for (Integer j : array)
						System.out.print(j + " ");
					System.out.println();
				}
				long start = System.nanoTime();
				insertionSort(array, array.length);
				long finish = System.nanoTime();
				long delta = finish - start;
				average += delta;
				if (traceOutputMode)
				{
					for (Integer j : array)
						System.out.print(j + " ");
					System.out.println();
				}
			}
			average = average / trials / 1000000000;
			writer.println("Algorithm: Shell Sort");
			writer.println("Array Size: " + size);
			writer.println("Order: Random");
			writer.println("Number of trials: " + trials);
			writer.println("Average Time: " + average + " sec.");
			writer.println();
		}
		// Median of Three (5)
		average = 0;
		MIN_SIZE = 5;
		for (int i = 0; i < trials; i++)
		{
			Random rand = new Random(i);
			for (int j = 0; j < size; j++)
				array[j] = rand.nextInt();
			if (traceOutputMode)
			{
				System.out.println("Median of Three (5)");
				System.out.println(size);
				System.out.println("Random");
				for (Integer j : array)
					System.out.print(j + " ");
				System.out.println();
			}
			long start = System.nanoTime();
			quickSort(array, array.length);
			long finish = System.nanoTime();
			long delta = finish - start;
			average += delta;
			if (traceOutputMode)
			{
				for (Integer j : array)
					System.out.print(j + " ");
				System.out.println();
			}
		}
		average = average / trials / 1000000000;
		writer.println("Algorithm: Median of Three (5)");
		writer.println("Array Size: " + size);
		writer.println("Order: Random");
		writer.println("Number of trials: " + trials);
		writer.println("Average Time: " + average + " sec.");
		writer.println();
		// Median of Three (20)
		average = 0;
		MIN_SIZE = 20;
		for (int i = 0; i < trials; i++)
		{
			Random rand = new Random(i);
			for (int j = 0; j < size; j++)
				array[j] = rand.nextInt();
			if (traceOutputMode)
			{
				System.out.println("Median of Three (20)");
				System.out.println(size);
				System.out.println("Random");
				for (Integer j : array)
					System.out.print(j + " ");
				System.out.println();
			}
			long start = System.nanoTime();
			quickSort(array, array.length);
			long finish = System.nanoTime();
			long delta = finish - start;
			average += delta;
			if (traceOutputMode)
			{
				for (Integer j : array)
					System.out.print(j + " ");
				System.out.println();
			}
		}
		average = average / trials / 1000000000;
		writer.println("Algorithm: Median of Three (20)");
		writer.println("Array Size: " + size);
		writer.println("Order: Random");
		writer.println("Number of trials: " + trials);
		writer.println("Average Time: " + average + " sec.");
		writer.println();
		// Median of Three (100)
		average = 0;
		MIN_SIZE = 100;
		for (int i = 0; i < trials; i++)
		{
			Random rand = new Random(i);
			for (int j = 0; j < size; j++)
				array[j] = rand.nextInt();
			if (traceOutputMode)
			{
				System.out.println("Median of Three (100)");
				System.out.println(size);
				System.out.println("Random");
				for (Integer j : array)
					System.out.print(j + " ");
				System.out.println();
			}
			long start = System.nanoTime();
			quickSort(array, array.length);
			long finish = System.nanoTime();
			long delta = finish - start;
			average += delta;
			if (traceOutputMode)
			{
				for (Integer j : array)
					System.out.print(j + " ");
				System.out.println();
			}
		}
		average = average / trials / 1000000000;
		writer.println("Algorithm: Median of Three (100)");
		writer.println("Array Size: " + size);
		writer.println("Order: Random");
		writer.println("Number of trials: " + trials);
		writer.println("Average Time: " + average + " sec.");
		writer.println();
		// Random Pivot
		average = 0;
		for (int i = 0; i < trials; i++)
		{
			Random rand = new Random(i);
			for (int j = 0; j < size; j++)
				array[j] = rand.nextInt();
			if (traceOutputMode)
			{
				System.out.println("Random Pivot (5)");
				System.out.println(size);
				System.out.println("Random");
				for (Integer j : array)
					System.out.print(j + " ");
				System.out.println();
			}
			long start = System.nanoTime();
			randomPivotQuickSort(array, array.length);
			long finish = System.nanoTime();
			long delta = finish - start;
			average += delta;
			if (traceOutputMode)
			{
				for (Integer j : array)
					System.out.print(j + " ");
				System.out.println();
			}
		}
		average = average / trials / 1000000000;
		writer.println("Algorithm: Random Pivot (5)");
		writer.println("Array Size: " + size);
		writer.println("Order: Random");
		writer.println("Number of trials: " + trials);
		writer.println("Average Time: " + average + " sec.");
		writer.println();
		// MergeSort
		average = 0;
		for (int i = 0; i < trials; i++)
		{
			Random rand = new Random(i);
			for (int j = 0; j < size; j++)
				array[j] = rand.nextInt();
			if (traceOutputMode)
			{
				System.out.println("MergeSort");
				System.out.println(size);
				System.out.println("Random");
				for (Integer j : array)
					System.out.print(j + " ");
				System.out.println();
			}
			long start = System.nanoTime();
			mergeSort(array, array.length);
			long finish = System.nanoTime();
			long delta = finish - start;
			average += delta;
			if (traceOutputMode)
			{
				for (Integer j : array)
					System.out.print(j + " ");
				System.out.println();
			}
		}
		average = average / trials / 1000000000;
		writer.println("Algorithm: MergeSort");
		writer.println("Array Size: " + size);
		writer.println("Order: Random");
		writer.println("Number of trials: " + trials);
		writer.println("Average Time: " + average + " sec.");
		writer.println();
	}
	
	public static void sortedTest(int size, int trials, PrintWriter writer, boolean traceOutputMode)
	{
		Integer[] array = new Integer[size];
		double average = 0;
		if (size <= 100000)
		{
			// Simple Quicksort
			for (int i = 0; i < trials; i++)
			{
				for (int j = 0; j < size; j++)
					array[j] = j + 1;
				if (traceOutputMode)
				{
					System.out.println("Simple Quicksort");
					System.out.println(size);
					System.out.println("Sorted");
					for (Integer j : array)
						System.out.print(j + " ");
					System.out.println();
				}
				long start = System.nanoTime();
				simpleQuickSort(array, size);
				long finish = System.nanoTime();
				double delta = finish - start;
				average += delta;
				if (traceOutputMode)
				{
					for (Integer j : array)
						System.out.print(j + " ");
					System.out.println();
				}
			}
			average = average / trials / 1000000000;
			writer.println("Algorithm: Simple Quicksort");
			writer.println("Array Size: " + size);
			writer.println("Order: Sorted");
			writer.println("Number of trials: " + trials);
			writer.println("Average Time: " + average + " sec.");
			writer.println();
			// Insertion Sort
			average = 0;
			for (int i = 0; i < trials; i++)
			{
				Random rand = new Random(i);
				for (int j = 0; j < size; j++)
					array[j] = j + 1;
				if (traceOutputMode)
				{
					System.out.println("Insertion Sort");
					System.out.println(size);
					System.out.println("Sorted");
					for (Integer j : array)
						System.out.print(j + " ");
					System.out.println();
				}
				long start = System.nanoTime();
				insertionSort(array, array.length);
				long finish = System.nanoTime();
				long delta = finish - start;
				average += delta;
				if (traceOutputMode)
				{
					for (Integer j : array)
						System.out.print(j + " ");
					System.out.println();
				}
			}
			average = average / trials / 1000000000;
			writer.println("Algorithm: Insertion Sort");
			writer.println("Array Size: " + size);
			writer.println("Order: Sorted");
			writer.println("Number of trials: " + trials);
			writer.println("Average Time: " + average + " sec.");
			writer.println();
			// Shell Sort
			average = 0;
			for (int i = 0; i < trials; i++)
			{
				Random rand = new Random(i);
				for (int j = 0; j < size; j++)
					array[j] = j + 1;
				if (traceOutputMode)
				{
					System.out.println("Shell Sort");
					System.out.println(size);
					System.out.println("Sorted");
					for (Integer j : array)
						System.out.print(j + " ");
					System.out.println();
				}
				long start = System.nanoTime();
				insertionSort(array, array.length);
				long finish = System.nanoTime();
				long delta = finish - start;
				average += delta;
				if (traceOutputMode)
				{
					for (Integer j : array)
						System.out.print(j + " ");
					System.out.println();
				}
			}
			average = average / trials / 1000000000;
			writer.println("Algorithm: Shell Sort");
			writer.println("Array Size: " + size);
			writer.println("Order: Sorted");
			writer.println("Number of trials: " + trials);
			writer.println("Average Time: " + average + " sec.");
			writer.println();
		}
		// Median of Three (5)
		MIN_SIZE = 5;
		average = 0;
		for (int i = 0; i < trials; i++)
		{
			for (int j = 0; j < size; j++)
				array[j] = j + 1;
			if (traceOutputMode)
			{
				System.out.println("Median of Three (5)");
				System.out.println(size);
				System.out.println("Sorted");
				for (Integer j : array)
					System.out.print(j + " ");
				System.out.println();
			}
			long start = System.nanoTime();
			quickSort(array, size);
			long finish = System.nanoTime();
			double delta = finish - start;
			average += delta;
			if (traceOutputMode)
			{
				for (Integer j : array)
					System.out.print(j + " ");
				System.out.println();
			}
		}
		average = average / trials / 1000000000;
		writer.println("Algorithm: Median of Three (5)");
		writer.println("Array Size: " + size);
		writer.println("Order: Sorted");
		writer.println("Number of trials: " + trials);
		writer.println("Average Time: " + average + " sec.");
		writer.println();
		// Median of Three (20)
		MIN_SIZE = 20;
		average = 0;
		for (int i = 0; i < trials; i++)
		{
			for (int j = 0; j < size; j++)
				array[j] = j + 1;
			if (traceOutputMode)
			{
				System.out.println("Median of Three (20)");
				System.out.println(size);
				System.out.println("Sorted");
				for (Integer j : array)
					System.out.print(j + " ");
				System.out.println();
			}
			long start = System.nanoTime();
			quickSort(array, size);
			long finish = System.nanoTime();
			double delta = finish - start;
			average += delta;
			if (traceOutputMode)
			{
				for (Integer j : array)
					System.out.print(j + " ");
				System.out.println();
			}
		}
		average = average / trials / 1000000000;
		writer.println("Algorithm: Median of Three (20)");
		writer.println("Array Size: " + size);
		writer.println("Order: Sorted");
		writer.println("Number of trials: " + trials);
		writer.println("Average Time: " + average + " sec.");
		writer.println();
		// Median of Three (100)
		MIN_SIZE = 100;
		average = 0;
		for (int i = 0; i < trials; i++)
		{
			for (int j = 0; j < size; j++)
				array[j] = j + 1;
			if (traceOutputMode)
			{
				System.out.println("Median of Three (100)");
				System.out.println(size);
				System.out.println("Sorted");
				for (Integer j : array)
					System.out.print(j + " ");
				System.out.println();
			}
			long start = System.nanoTime();
			quickSort(array, size);
			long finish = System.nanoTime();
			double delta = finish - start;
			average += delta;
			if (traceOutputMode)
			{
				for (Integer j : array)
					System.out.print(j + " ");
				System.out.println();
			}
		}
		average = average / trials / 1000000000;
		writer.println("Algorithm: Median of Three (100)");
		writer.println("Array Size: " + size);
		writer.println("Order: Sorted");
		writer.println("Number of trials: " + trials);
		writer.println("Average Time: " + average + " sec.");
		writer.println();
		// Random Pivot (5)
		average = 0;
		for (int i = 0; i < trials; i++)
		{
			for (int j = 0; j < size; j++)
				array[j] = j + 1;
			if (traceOutputMode)
			{
				System.out.println("Random Pivot (5)");
				System.out.println(size);
				System.out.println("Sorted");
				for (Integer j : array)
					System.out.print(j + " ");
				System.out.println();
			}
			long start = System.nanoTime();
			randomPivotQuickSort(array, size);
			long finish = System.nanoTime();
			double delta = finish - start;
			average += delta;
			if (traceOutputMode)
			{
				for (Integer j : array)
					System.out.print(j + " ");
				System.out.println();
			}
		}
		average = average / trials / 1000000000;
		writer.println("Algorithm: Random Pivot (5)");
		writer.println("Array Size: " + size);
		writer.println("Order: Sorted");
		writer.println("Number of trials: " + trials);
		writer.println("Average Time: " + average + " sec.");
		writer.println();
		// MergeSort
		average = 0;
		for (int i = 0; i < trials; i++)
		{
			for (int j = 0; j < size; j++)
				array[j] = j + 1;
			if (traceOutputMode)
			{
				System.out.println("MergeSort");
				System.out.println(size);
				System.out.println("Sorted");
				for (Integer j : array)
					System.out.print(j + " ");
				System.out.println();
			}
			long start = System.nanoTime();
			mergeSort(array, size);
			long finish = System.nanoTime();
			double delta = finish - start;
			average += delta;
			if (traceOutputMode)
			{
				for (Integer j : array)
					System.out.print(j + " ");
				System.out.println();
			}
		}
		average = average / trials / 1000000000;
		writer.println("Algorithm: MergeSort");
		writer.println("Array Size: " + size);
		writer.println("Order: Sorted");
		writer.println("Number of trials: " + trials);
		writer.println("Average Time: " + average + " sec.");
		writer.println();
	}
	
	public static void reverseTest(int size, int trials, PrintWriter writer, boolean traceOutputMode)
	{
		Integer[] array = new Integer[size];
		double average = 0;
		if (size <= 100000)
		{
			// Simple Quicksort
			for (int i = 0; i < trials; i++)
			{
				for (int j = 0; j < size; j++)
					array[j] = size - j;
				if (traceOutputMode)
				{
					System.out.println("Simple Quicksort");
					System.out.println(size);
					System.out.println("Reverse");
					for (Integer j : array)
						System.out.print(j + " ");
					System.out.println();
				}
				long start = System.nanoTime();
				simpleQuickSort(array, size);
				long finish = System.nanoTime();
				double delta = finish - start;
				average += delta;
				if (traceOutputMode)
				{
					for (Integer j : array)
						System.out.print(j + " ");
					System.out.println();
				}
			}
			average = average / trials / 1000000000;
			writer.println("Algorithm: Simple Quicksort");
			writer.println("Array Size: " + size);
			writer.println("Order: Reverse");
			writer.println("Number of trials: " + trials);
			writer.println("Average Time: " + average + " sec.");
			writer.println();
			// Insertion Sort
			average = 0;
			for (int i = 0; i < trials; i++)
			{
				Random rand = new Random(i);
				for (int j = 0; j < size; j++)
					array[j] = size - j;
				if (traceOutputMode)
				{
					System.out.println("Insertion Sort");
					System.out.println(size);
					System.out.println("Reverse");
					for (Integer j : array)
						System.out.print(j + " ");
					System.out.println();
				}
				long start = System.nanoTime();
				insertionSort(array, array.length);
				long finish = System.nanoTime();
				long delta = finish - start;
				average += delta;
				if (traceOutputMode)
				{
					for (Integer j : array)
						System.out.print(j + " ");
					System.out.println();
				}
			}
			average = average / trials / 1000000000;
			writer.println("Algorithm: Insertion Sort");
			writer.println("Array Size: " + size);
			writer.println("Order: Reverse");
			writer.println("Number of trials: " + trials);
			writer.println("Average Time: " + average + " sec.");
			writer.println();
			// Shell Sort
			average = 0;
			for (int i = 0; i < trials; i++)
			{
				Random rand = new Random(i);
				for (int j = 0; j < size; j++)
					array[j] = size - j;
				if (traceOutputMode)
				{
					System.out.println("Shell Sort");
					System.out.println(size);
					System.out.println("Reverse");
					for (Integer j : array)
						System.out.print(j + " ");
					System.out.println();
				}
				long start = System.nanoTime();
				insertionSort(array, array.length);
				long finish = System.nanoTime();
				long delta = finish - start;
				average += delta;
				if (traceOutputMode)
				{
					for (Integer j : array)
						System.out.print(j + " ");
					System.out.println();
				}
			}
			average = average / trials / 1000000000;
			writer.println("Algorithm: Shell Sort");
			writer.println("Array Size: " + size);
			writer.println("Order: Reverse");
			writer.println("Number of trials: " + trials);
			writer.println("Average Time: " + average + " sec.");
			writer.println();
		}
		// Median of Three (5)
		MIN_SIZE = 5;
		average = 0;
		for (int i = 0; i < trials; i++)
		{
			for (int j = 0; j < size; j++)
				array[j] = size - j;
			if (traceOutputMode)
			{
				System.out.println("Median of Three (5)");
				System.out.println(size);
				System.out.println("Reverse");
				for (Integer j : array)
					System.out.print(j + " ");
				System.out.println();
			}
			long start = System.nanoTime();
			quickSort(array, size);
			long finish = System.nanoTime();
			double delta = finish - start;
			average += delta;
			if (traceOutputMode)
			{
				for (Integer j : array)
					System.out.print(j + " ");
				System.out.println();
			}
		}
		average = average / trials / 1000000000;
		writer.println("Algorithm: Median of Three (5)");
		writer.println("Array Size: " + size);
		writer.println("Order: Reverse");
		writer.println("Number of trials: " + trials);
		writer.println("Average Time: " + average + " sec.");
		writer.println();
		// Median of Three (20)
		MIN_SIZE = 20;
		average = 0;
		for (int i = 0; i < trials; i++)
		{
			for (int j = 0; j < size; j++)
				array[j] = size - j;
			if (traceOutputMode)
			{
				System.out.println("Median of Three (20)");
				System.out.println(size);
				System.out.println("Reverse");
				for (Integer j : array)
					System.out.print(j + " ");
				System.out.println();
			}
			long start = System.nanoTime();
			quickSort(array, size);
			long finish = System.nanoTime();
			double delta = finish - start;
			average += delta;
			if (traceOutputMode)
			{
				for (Integer j : array)
					System.out.print(j + " ");
				System.out.println();
			}
		}
		average = average / trials / 1000000000;
		writer.println("Algorithm: Median of Three (20)");
		writer.println("Array Size: " + size);
		writer.println("Order: Reverse");
		writer.println("Number of trials: " + trials);
		writer.println("Average Time: " + average + " sec.");
		writer.println();
		// Median of Three (100)
		MIN_SIZE = 100;
		average = 0;
		for (int i = 0; i < trials; i++)
		{
			for (int j = 0; j < size; j++)
				array[j] = size - j;
			if (traceOutputMode)
			{
				System.out.println("Median of Three (100)");
				System.out.println(size);
				System.out.println("Reverse");
				for (Integer j : array)
					System.out.print(j + " ");
				System.out.println();
			}
			long start = System.nanoTime();
			quickSort(array, size);
			long finish = System.nanoTime();
			double delta = finish - start;
			average += delta;
			if (traceOutputMode)
			{
				for (Integer j : array)
					System.out.print(j + " ");
				System.out.println();
			}
		}
		average = average / trials / 1000000000;
		writer.println("Algorithm: Median of Three (100)");
		writer.println("Array Size: " + size);
		writer.println("Order: Reverse");
		writer.println("Number of trials: " + trials);
		writer.println("Average Time: " + average + " sec.");
		writer.println();
		// Random Pivot (5)
		average = 0;
		for (int i = 0; i < trials; i++)
		{
			for (int j = 0; j < size; j++)
				array[j] = size - j;
			if (traceOutputMode)
			{
				System.out.println("Random Pivot (5)");
				System.out.println(size);
				System.out.println("Reverse");
				for (Integer j : array)
					System.out.print(j + " ");
				System.out.println();
			}
			long start = System.nanoTime();
			randomPivotQuickSort(array, size);
			long finish = System.nanoTime();
			double delta = finish - start;
			average += delta;
			if (traceOutputMode)
			{
				for (Integer j : array)
					System.out.print(j + " ");
				System.out.println();
			}
		}
		average = average / trials / 1000000000;
		writer.println("Algorithm: Random Pivot (5)");
		writer.println("Array Size: " + size);
		writer.println("Order: Reverse");
		writer.println("Number of trials: " + trials);
		writer.println("Average Time: " + average + " sec.");
		writer.println();
		// MergeSort
		average = 0;
		for (int i = 0; i < trials; i++)
		{
			for (int j = 0; j < size; j++)
				array[j] = size - j;
			if (traceOutputMode)
			{
				System.out.println("MergeSort");
				System.out.println(size);
				System.out.println("Reverse");
				for (Integer j : array)
					System.out.print(j + " ");
				System.out.println();
			}
			long start = System.nanoTime();
			mergeSort(array, size);
			long finish = System.nanoTime();
			double delta = finish - start;
			average += delta;
			if (traceOutputMode)
			{
				for (Integer j : array)
					System.out.print(j + " ");
				System.out.println();
			}
		}
		average = average / trials / 1000000000;
		writer.println("Algorithm: MergeSort");
		writer.println("Array Size: " + size);
		writer.println("Order: Reverse");
		writer.println("Number of trials: " + trials);
		writer.println("Average Time: " + average + " sec.");
		writer.println();
	}
	
	public static <T extends Comparable<? super T>> void simpleQuickSort(T[] array, int n)
	{
		simpleQuickSort(array, 0, n-1);
	} // end simpleQuickSort

	public static <T extends Comparable<? super T>> void simpleQuickSort(T[] array, int first, int last)
	{
		if (first < last)
		{
			// create the partition: Smaller | Pivot | Larger
			int pivotIndex = simplePartition(array, first, last);
	      
			// sort subarrays Smaller and Larger
			simpleQuickSort(array, first, pivotIndex-1);
			simpleQuickSort(array, pivotIndex+1, last);
		} // end if
	}  // end simpleQuickSort
	private static <T extends Comparable<? super T>> int simplePartition(T[] a, int first, int last)
	{
		int pivotIndex = last;  // simply pick pivot as rightmost element
		T pivot = a[pivotIndex];

		// determine subarrays Smaller = a[first..endSmaller]
		//                 and Larger  = a[endSmaller+1..last-1]
		// such that elements in Smaller are <= pivot and 
		// elements in Larger are >= pivot; initially, these subarrays are empty

		int indexFromLeft = first; 
		int indexFromRight = last - 1; 

		boolean done = false;
		while (!done)
		{
			// starting at beginning of array, leave elements that are < pivot; 
			// locate first element that is >= pivot
			while (a[indexFromLeft].compareTo(pivot) < 0)
				indexFromLeft++;

			// starting at end of array, leave elements that are > pivot; 
			// locate first element that is <= pivot

			while (a[indexFromRight].compareTo(pivot) > 0 && indexFromRight > first)
				indexFromRight--;

			// Assertion: a[indexFromLeft] >= pivot and 
			//            a[indexFromRight] <= pivot.

			if (indexFromLeft < indexFromRight)
			{
				swap(a, indexFromLeft, indexFromRight);
				indexFromLeft++;
				indexFromRight--;
			}
			else 
				done = true;
		} // end while

		// place pivot between Smaller and Larger subarrays
		swap(a, pivotIndex, indexFromLeft);
		pivotIndex = indexFromLeft;

		// Assertion:
		// Smaller = a[first..pivotIndex-1]
		// Pivot = a[pivotIndex]
		// Larger  = a[pivotIndex + 1..last]

		return pivotIndex; 
	}  // end partition
//-----------------------------------------------------------------------------------------------

	// RANDOM PIVOT QUICKSORT
	public static <T extends Comparable<? super T>> void randomPivotQuickSort(T[] array, int n)
	{
		randomPivotQuickSort(array, 0, n-1);
	} // end randomPivotQuickSort
	
	public static <T extends Comparable<? super T>> void randomPivotQuickSort(T[] array, int first, int last)
	{
		if (last - first + 1 < 5)
			insertionSort(array, first, last);
		else
		{
			// create the partition: Smaller | Pivot | Larger
			int pivotIndex = randomPartition(array, first, last);
	      
			// sort subarrays Smaller and Larger
			randomPivotQuickSort(array, first, pivotIndex-1);
			randomPivotQuickSort(array, pivotIndex+1, last);
		} // end if
	}  // end randomPivotQuickSort
	
	private static <T extends Comparable<? super T>> int randomPartition(T[] a, int first, int last)
	{
		Random rand = new Random();
		int pivotIndex = rand.nextInt(last - first + 1) + first;  // pivot is random index in array
		T pivot = a[pivotIndex];
		swap(a, pivotIndex, last);
		pivotIndex = last;

		// determine subarrays Smaller = a[first..endSmaller]
		//                 and Larger  = a[endSmaller+1..last-1]
		// such that elements in Smaller are <= pivot and 
		// elements in Larger are >= pivot; initially, these subarrays are empty

		int indexFromLeft = first; 
		int indexFromRight = last - 1; 

		boolean done = false;
		while (!done)
		{
			// starting at beginning of array, leave elements that are < pivot; 
			// locate first element that is >= pivot
			while (a[indexFromLeft].compareTo(pivot) < 0)
				indexFromLeft++;

			// starting at end of array, leave elements that are > pivot; 
			// locate first element that is <= pivot

			while (a[indexFromRight].compareTo(pivot) > 0 && indexFromRight > first)
				indexFromRight--;

			// Assertion: a[indexFromLeft] >= pivot and 
			//            a[indexFromRight] <= pivot.

			if (indexFromLeft < indexFromRight)
			{
				swap(a, indexFromLeft, indexFromRight);
				indexFromLeft++;
				indexFromRight--;
			}
			else 
				done = true;
		} // end while

		// place pivot between Smaller and Larger subarrays
		swap(a, pivotIndex, indexFromLeft);
		pivotIndex = indexFromLeft;

		// Assertion:
		// Smaller = a[first..pivotIndex-1]
		// Pivot = a[pivotIndex]
		// Larger  = a[pivotIndex + 1..last]

		return pivotIndex; 
	}  // end partition
//-----------------------------------------------------------------------------------------------
	
	// MERGE SORT
	public static <T extends Comparable<? super T>> void mergeSort(T[] a, int n)
	{
		mergeSort(a, 0, n - 1);
	} // end mergeSort

	public static <T extends Comparable<? super T>> void mergeSort(T[] a, int first, int last)
	{
	  T[] tempArray = (T[])new Comparable<?>[a.length];
	  mergeSort(a, tempArray, first, last);
	} // end mergeSort
	
	private static <T extends Comparable<? super T>> void mergeSort(T[] a, T[] tempArray, int first, int last)
	{
	   if (first < last)
	   {  // sort each half
	      int mid = (first + last)/2;// index of midpoint
	      mergeSort(a, tempArray, first, mid);  // sort left half array[first..mid]
	      mergeSort(a, tempArray, mid + 1, last); // sort right half array[mid+1..last]

			if (a[mid].compareTo(a[mid + 1]) > 0)      // Question 2, Chapter 9
	     	 	merge(a, tempArray, first, mid, last); // merge the two halves
	   //	else skip merge step
	   }  // end if
	}  // end mergeSort
	
	private static <T extends Comparable<? super T>> void merge(T[] a, T[] tempArray, int first, int mid, int last)
	{
		// Two adjacent subarrays are a[beginHalf1..endHalf1] and a[beginHalf2..endHalf2].
		int beginHalf1 = first;
		int endHalf1 = mid;
		int beginHalf2 = mid + 1;
		int endHalf2 = last;

		// while both subarrays are not empty, copy the
	   // smaller item into the temporary array
		int index = beginHalf1; // next available location in
								            // tempArray
		for (; (beginHalf1 <= endHalf1) && (beginHalf2 <= endHalf2); index++)
	   {  // Invariant: tempArray[beginHalf1..index-1] is in order
	   
	      if (a[beginHalf1].compareTo(a[beginHalf2]) <= 0)
	      {  
	      	tempArray[index] = a[beginHalf1];
	        beginHalf1++;
	      }
	      else
	      {  
	      	tempArray[index] = a[beginHalf2];
	        beginHalf2++;
	      }  // end if
	   }  // end for

	   // finish off the nonempty subarray

	   // finish off the first subarray, if necessary
	   for (; beginHalf1 <= endHalf1; beginHalf1++, index++)
	      // Invariant: tempArray[beginHalf1..index-1] is in order
	      tempArray[index] = a[beginHalf1];

	   // finish off the second subarray, if necessary
		for (; beginHalf2 <= endHalf2; beginHalf2++, index++)
	      // Invariant: tempa[beginHalf1..index-1] is in order
	      tempArray[index] = a[beginHalf2];
		
	   // copy the result back into the original array
	   for (index = first; index <= last; index++)
	      a[index] = tempArray[index];
	}  // end merge
// -------------------------------------------------------------------------------

	// QUICK SORT
	public static <T extends Comparable<? super T>> void quickSort(T[] array, int n)
	{
		quickSort(array, 0, n-1);
	} // end quickSort
	
	/** Sorts an array into ascending order. Uses quick sort with
	 *  median-of-three pivot selection for arrays of at least 
	 *  MIN_SIZE elements, and uses insertion sort for other arrays. */
	public static <T extends Comparable<? super T>> void quickSort(T[] a, int first, int last)
	{
		if (last - first + 1 < MIN_SIZE)
		{
			insertionSort(a, first, last);
		}
		else
		{
			// create the partition: Smaller | Pivot | Larger
			int pivotIndex = partition(a, first, last);
		
			// sort subarrays Smaller and Larger
			quickSort(a, first, pivotIndex - 1);
			quickSort(a, pivotIndex + 1, last);
		} // end if
	} // end quickSort

	// 12.17
	/** Task: Partitions an array as part of quick sort into two subarrays
	 *        called Smaller and Larger that are separated by a single
	 *        element called the pivot. 
	 *        Elements in Smaller are <= pivot and appear before the 
	 *        pivot in the array.
	 *        Elements in Larger are >= pivot and appear after the 
	 *        pivot in the array.
	 *  @param a      an array of Comparable objects
	 *  @param first  the integer index of the first array element; 
	 *                first >= 0 and < a.length 
	 *  @param last   the integer index of the last array element; 
	 *                last - first >= 3; last < a.length
	 *  @return the index of the pivot */
	private static <T extends Comparable<? super T>> int partition(T[] a, int first, int last)
	{
		int mid = (first + last)/2;
		sortFirstMiddleLast(a, first, mid, last);
	  
		// Assertion: The pivot is a[mid]; a[first] <= pivot and 
		// a[last] >= pivot, so do not compare these two array elements
		// with pivot.
	  
		// move pivot to next-to-last position in array
		swap(a, mid, last - 1);
		int pivotIndex = last - 1;
		T pivot = a[pivotIndex];

		// determine subarrays Smaller = a[first..endSmaller]
		// and                 Larger  = a[endSmaller+1..last-1]
		// such that elements in Smaller are <= pivot and 
		// elements in Larger are >= pivot; initially, these subarrays are empty

		int indexFromLeft = first + 1; 
		int indexFromRight = last - 2; 
		boolean done = false;
		while (!done)
		{
			// starting at beginning of array, leave elements that are < pivot;
			// locate first element that is >= pivot; you will find one,
			// since last element is >= pivot
			while (a[indexFromLeft].compareTo(pivot) < 0)
				indexFromLeft++;
	      
			// starting at end of array, leave elements that are > pivot; 
			// locate first element that is <= pivot; you will find one, 
			// since first element is <= pivot
			while (a[indexFromRight].compareTo(pivot) > 0)
				indexFromRight--;
			
			assert a[indexFromLeft].compareTo(pivot) >= 0 && 
				a[indexFromRight].compareTo(pivot) <= 0;
	        
			if (indexFromLeft < indexFromRight)
			{
				swap(a, indexFromLeft, indexFromRight);
				indexFromLeft++;
				indexFromRight--;
			}
			else
				done = true;
		} // end while

		// place pivot between Smaller and Larger subarrays
		swap(a, pivotIndex, indexFromLeft);
		pivotIndex = indexFromLeft;
		
		// Assertion:
		//   Smaller = a[first..pivotIndex-1]
		//   Pivot = a[pivotIndex]
		//   Larger = a[pivotIndex+1..last]
		
		return pivotIndex;
	} // end partition
	
	// 12.16
	/** Task: Sorts the first, middle, and last elements of an 
	 *        array into ascending order.
	 *  @param a      an array of Comparable objects
	 *  @param first  the integer index of the first array element; 
	 *                first >= 0 and < a.length 
	 *  @param mid    the integer index of the middle array element
	 *  @param last   the integer index of the last array element; 
	 *                last - first >= 2, last < a.length */
	private static <T extends Comparable<? super T>> void sortFirstMiddleLast(T[] a, int first, int mid, int last)
	{
		order(a, first, mid); // make a[first] <= a[mid]
		order(a, mid, last);  // make a[mid] <= a[last]
		order(a, first, mid); // make a[first] <= a[mid]
	} // end sortFirstMiddleLast

	/** Task: Orders two given array elements into ascending order
	 *        so that a[i] <= a[j].
	 *  @param a  an array of Comparable objects
	 *  @param i  an integer >= 0 and < array.length
	 *  @param j  an integer >= 0 and < array.length */
	private static <T extends Comparable<? super T>> void order(T[] a, int i, int j)
	{
		if (a[i].compareTo(a[j]) > 0)
			swap(a, i, j);
	} // end order
	
	public static <T extends Comparable<? super T>> void insertionSort(T[] a, int n)
	{
		insertionSort(a, 0, n - 1);
	} // end insertionSort
	
	public static <T extends Comparable<? super T>> void insertionSort(T[] a, int first, int last)
	{
		int unsorted, index;
		
		for (unsorted = first + 1; unsorted <= last; unsorted++)
		{   // Assertion: a[first] <= a[first + 1] <= ... <= a[unsorted - 1]
		
			T firstUnsorted = a[unsorted];
			insertInOrder(firstUnsorted, a, first, unsorted - 1);
		} // end for
	} // end insertionSort
	
	private static <T extends Comparable<? super T>> void insertInOrder(T element, T[] a, int begin, int end)
	{
		int index;
		
		for (index = end; (index >= begin) && (element.compareTo(a[index]) < 0); index--)
		{
			a[index + 1] = a[index]; // make room
		} // end for
		
		// Assertion: a[index + 1] is available
		a[index + 1] = element;  // insert
	} // end insertInOrder

	private static void swap(Object [] a, int i, int j)
	{
		Object temp = a[i];
		a[i] = a[j];
		a[j] = temp; 
	} // end swap

	public static void shellSort(int[] array)
	{
		int inner, outer;
		int temp;
		
		int h = 1;
		while (h <= array.length / 3)
			h = h * 3 + 1;
		while (h > 0)
		{
			for (outer = h; outer < array.length; outer++)
			{
				temp = array[outer];
				inner = outer;
				
				while (inner > h - 1 && array[inner - h] >= temp)
				{
					array[inner] = array[inner - h];
					inner -= h;
				}
				array[inner] = temp;
			}
			h = (h - 1) / 3;
		}
	}
}