// CS 0445 Spring 2017
// This is a partial implementation of the ReallyLongInt class.  You need to
// complete the implementations of the remaining methods.  Also, for this class
// to work, you must complete the implementation of the LinkedListPlus class.
// See additional comments below.

public class ReallyLongInt 	extends LinkedListPlus<Integer> 
							implements Comparable<ReallyLongInt>
{
	// Instance variables are inherited.  You may not add any new instance variables
	
	// Default constructor
	private ReallyLongInt()
	{
		super();
	}

	// Note that we are adding the digits here in the FRONT. This is more efficient
	// (no traversal is necessary) and results in the LEAST significant digit first
	// in the list.  It is assumed that String s is a valid representation of an
	// unsigned integer with no leading zeros.
	public ReallyLongInt(String s)
	{
		super();
		char c;
		int digit;
		// Iterate through the String, getting each character and converting it into
		// an int.  Then make an Integer and add at the front of the list.  Note that
		// the add() method (from A2LList) does not need to traverse the list since
		// it is adding in position 1.  Note also the the author's linked list
		// uses index 1 for the front of the list.
		for (int i = 0; i < s.length(); i++)
		{
			c = s.charAt(i);
			if (('0' <= c) && (c <= '9'))
			{
				digit = c - '0';
				this.add(1, new Integer(digit));
			}
			else throw new NumberFormatException("Illegal digit " + c);
		}
	}

	// Simple call to super to copy the nodes from the argument ReallyLongInt
	// into a new one.
	public ReallyLongInt(ReallyLongInt rightOp)
	{
		super(rightOp);
	}
	
	// Method to put digits of number into a String.  Since the numbers are
	// stored "backward" (least significant digit first) we first reverse the
	// number, then traverse it to add the digits to a StringBuilder, then
	// reverse it again.  This seems like a lot of work, but given the
	// limitations of the super classes it is what we must do.
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		if (numberOfEntries > 0)
		{
			this.reverse();
			for (Node curr = firstNode; curr != null; curr = curr.next)
			{
				sb.append(curr.data);
			}
			this.reverse();
		}
		return sb.toString();
	}

	// You must implement the methods below.  See the descriptions in the
	// assignment sheet

	//	Iterates through ReallyLongInt with more entries. Adds element by 
	//	element, keeping track of carries, then adds to new ReallyLongInt sum
	public ReallyLongInt add(ReallyLongInt rightOp)
	{
		int k = 0;
		ReallyLongInt sum = new ReallyLongInt();
		Node temp2;
		boolean carry = false;
		if (rightOp.getLength() >= numberOfEntries)
		{
			temp2 = firstNode;
			for (Node temp1 = rightOp.firstNode; temp1 != null; temp1 = temp1.next)
			{
				if (temp2 != null)
				{
					if (carry)
						k = Integer.sum(temp1.data, temp2.data) + 1;
					else if (!carry)
						k = Integer.sum(temp1.data, temp2.data);
					if (k < 10)
						carry = false;
					else if (k >= 10)
					{
						k -= 10;
						carry = true;
					}
					sum.add(new Integer(k));
					temp2 = temp2.next;
				}
				else if (temp2 == null)
				{
					if (carry)
						k = Integer.sum(temp1.data, new Integer(1));
					else if (!carry)
						k = Integer.sum(temp1.data, new Integer(0));
					if (k < 10)
						carry = false;
					else if (k >= 10)
					{
						k -= 10;
						carry = true;
					}
					sum.add(new Integer(k));
				}
			}
		}
		else if (rightOp.getLength() < numberOfEntries)
		{
			temp2 = rightOp.firstNode;
			for (Node temp1 = firstNode; temp1 != null; temp1 = temp1.next)
			{
				if (temp2 != null)
				{
					if (carry)
						k = Integer.sum(temp1.data, temp2.data) + 1;
					else if (!carry)
						k = Integer.sum(temp1.data, temp2.data);
					if (k < 10)
						carry = false;
					else if (k >= 10)
					{
						k -= 10;
						carry = true;
					}
					sum.add(new Integer(k));
					temp2 = temp2.next;
				}
				else if (temp2 == null)
				{
					if (carry)
						k = Integer.sum(temp1.data, new Integer(1));
					else if (!carry)
						k = Integer.sum(temp1.data, new Integer(0));
					if (k < 10)
						carry = false;
					else if (k >= 10)
					{
						k -= 10;
						carry = true;
					}
					sum.add(new Integer(k));
				}
			}
		}
		if (carry)
		{
			sum.add(new Integer(1));
			carry = false;
		}
		return sum;
	}
	
	//	Checks if operation is valid. Subtracts element by element, keeping 
	//	track of borrows, then adds to new ReallyLongInt diff
	public ReallyLongInt subtract(ReallyLongInt rightOp)
	{
		ReallyLongInt diff = new ReallyLongInt();
		if (compareTo(rightOp) == -1)
			throw new ArithmeticException("Invalid Difference -- Negative Number");
		else
		{
			Node curr2 = rightOp.firstNode;
			boolean borrow = false;
			int k = 0;
			for (Node curr1 = firstNode; curr1 != null; curr1 = curr1.next)
			{
				if (curr2 != null)
				{
					if (borrow)
						k = curr1.data.intValue() - curr2.data.intValue() - 1;
					else if (!borrow)
						k = curr1.data.intValue() - curr2.data.intValue();
					if (k >= 0)
						borrow = false;
					if (k < 0)
					{
						k += 10;
						borrow = true;
					}
					curr2 = curr2.next;
				}
				else if (curr2 == null)
				{
					if (borrow)
						k = curr1.data.intValue() - 1;
					else if (!borrow)
						k = curr1.data.intValue();
					if (k >= 0)
						borrow = false;
					if (k < 0)
					{
						k += 10;
						borrow = true;
					}
				}
				diff.add(new Integer(k));
			}
		}
		diff.reverse();
		while (diff.firstNode.data.intValue() == 0)
			diff.leftShift(1);
		diff.reverse();
		return diff;
	}

	//	First checks if greater value can be determined by number of elements. 
	//	If not, iterates through ReallyLongInts from most significan to least 
	//	significant until a difference in value is found
	public int compareTo(ReallyLongInt rOp)
	{
		if (numberOfEntries < rOp.getLength())
			return -1;
		else if (numberOfEntries > rOp.getLength())
			return 1;
		else
		{
			reverse();
			rOp.reverse();
			Node n1 = firstNode;
			Node n2 = rOp.firstNode;
			try
			{
				while (n1.data.intValue() == n2.data.intValue())
				{
					n1 = n1.next;
					n2 = n2.next;
				}
			}catch(NullPointerException e){}
			reverse();
			rOp.reverse();
			try
			{
				if (n1.data.compareTo(n2.data) < 0)
					return -1;
				else if (n1.data.compareTo(n2.data) > 0)
					return 1;
			}
			catch(NullPointerException e){return 0;}
		}
		return 0;
	}

	//	Uses compareTo()
	public boolean equals(Object rightOp)
	{
		if (compareTo((ReallyLongInt) rightOp) == 0)
			return true;
		return false;
	}

	public void multTenToThe(int num)
	{
		for (int i = 0; i < num; i++)
		{
			add(1, new Integer(0));
		}
	}

	public void divTenToThe(int num)
	{
		for (int i = 0; i < num; i++)
		{
			leftShift(1);
		}
	}
}
