import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class MultiDS<T> implements PrimQ<T>, Reorder
{
	
	private int numElements, capacity;
	private T[] a;
	
	public MultiDS(int c)
	{
		@SuppressWarnings("unchecked")
		T[] tempA = (T[]) new Object[c];
		a = tempA;
		capacity = c;
		numElements = 0;
	}
	
	public boolean addItem(T item)
	{
		if (numElements < capacity)
		{
			a[numElements] = item;
			numElements++;
			return true;
		}
		else
			return false;
	}
	public T removeItem()
	{
		if (!empty())
		{
			Object tempVal = a[0];
			for (int i = 0; i < numElements - 1; i++)
			{
				a[i] = a[i + 1];
			}
			a[numElements - 1] = null;
			numElements--;
			@SuppressWarnings("unchecked")
			T val = (T) tempVal;
			return val;
		}
		else
			return null;
	}
	public boolean full()
	{
		if (capacity == numElements)
			return true;
		else
			return false;
	}
	public boolean empty()
	{
		if (numElements == 0)
			return true;
		else
			return false;
	}
	public int size()
	{
		return numElements;
	}
	public void clear()
	{
		numElements = 0;
		for (int i = 0; i < capacity; i++)
			a[i] = null;
	}
	
	public void reverse()
	{
		@SuppressWarnings("unchecked")
		T[] tempA = (T[]) new Object[capacity];
		for (int i = 0; i < numElements; i++)
		{
			tempA[i] = a[numElements - i - 1];
		}
		a = tempA;
	}
	public void shiftRight()
	{
		@SuppressWarnings("unchecked")
		T j = a[numElements - 1];
		for (int i = numElements - 1; i > 0; i--)
		{
			a[i] = a[i - 1];
		}
		a[0] = (T) j;
	}
	public void shiftLeft()
	{
		@SuppressWarnings("unchecked")
		T j = a[0];
		for (int i = 0; i < numElements - 1; i++)
		{
			a[i] = a[i + 1];
		}
		a[numElements - 1] = (T) j;
	}
	public void shuffle()
	{
		Random rand = new Random();
		@SuppressWarnings("unchecked")
		T[] tempA = (T[]) new Object[capacity];
		for (int i = 0; i < numElements; i++)
		{
			int r = rand.nextInt(numElements);
			while (tempA[r] != null)
				r = rand.nextInt(numElements);
			tempA[r] = a[i];
		}
		a = tempA;
	}
	
	public String toString()
	{
		StringBuilder s = new StringBuilder("Contents:\n");
		for (int i = 0; i < numElements; i++)
		{
			s.append(a[i].toString() + " ");
		}
		return s.toString();
	}
}