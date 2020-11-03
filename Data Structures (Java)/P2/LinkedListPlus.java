// CS 0445 Spring 2017 
// LinkedListPlus<T> class partial implementation

// See the commented methods below.  You must complete this class by
// filling in the method bodies for the remaining methods.  Note that you
// may NOT add any new instance variables, but you may use method variables
// as needed.

public class LinkedListPlus<T> extends A2LList<T>
{
	// Default constructor simply calls super()
	public LinkedListPlus()
	{
		super();
	}
	
	// Copy constructor.  This is a "deepish" copy so it will make new
	// Node objects for all of the nodes in the old list.  However, it
	// is not totally deep since it does NOT make copies of the objects
	// within the Nodes -- rather it just copies the references.
	public LinkedListPlus(LinkedListPlus<T> oldList)
	{
		super();
		if (oldList.getLength() > 0)
		{
			// Special case for first Node since we need to set the
			// firstNode instance variable.
			Node temp = oldList.firstNode;		// front of old list
			Node newNode = new Node(temp.data);	// copy the data
			firstNode = newNode;				// set front of new list
			
			// Now we traverse the old list, appending a new Node with
			// the correct data to the end of the new list for each Node
			// in the old list.  Note how the loop is done and how the
			// Nodes are linked.
			Node currNode = firstNode;
			temp = temp.next;
			while (temp != null)
			{
				currNode.next = new Node(temp.data);
				temp = temp.next;
				currNode = currNode.next;
			}
			numberOfEntries = oldList.numberOfEntries;
		}			
	}

	// Make a StringBuilder then traverse the nodes of the list, appending the
	// toString() of the data for each node to the end of the StringBuilder.
	// Finally, return the StringBuilder as a String.
	public String toString()
	{
		StringBuilder b = new StringBuilder();
		for (Node curr = firstNode; curr != null; curr = curr.next)
		{
			b.append(curr.data.toString());
			b.append(" ");
		}
		return b.toString();
	}

	// Shift left num positions.  This will shift out the first num Nodes
	// of the list, with Node num+1 now being at the front.
	public void leftShift(int num)
	{
		if (num >= this.getLength())	// If we shift more than the number of
		{								// Nodes, just initialize to empty
			firstNode = null;
			numberOfEntries = 0;
		}
		else if (num > 0)
		{
			Node temp = firstNode;		// Start at front
			for (int i = 0; i < num-1; i++)	 // Get to node BEFORE the one that 
			{								 // should be the new firstNode
				temp = temp.next;
			}
			firstNode = temp.next;		// Set firstNode to Node after temp
			temp.next = null;		    // Disconnect old prefix of list (just
										// to be extra cautious)
			numberOfEntries = numberOfEntries - num;	// Update logical size
		}
	}

	// Remove num items from the end of the list
	public void rightShift(int num)
	{
		if (num >= this.getLength())
		{
			firstNode = null;
			numberOfEntries = 0;
		}
		else if (num > 0)
		{
			Node temp = firstNode;
			for (int i = 1; i <= numberOfEntries - num; i++)
			{
				if (i < numberOfEntries - num)
					temp = temp.next;
				else if (i == numberOfEntries - num)
					temp.next = null;
			}
			numberOfEntries = numberOfEntries - num;
		}
	}

	// Remove from the front and add at the end.  Note that this method should
	// not create any new Node objects -- it is simply moving them.  If num
	// is negative it should still work, actually doing a right rotation.
	public void leftRotate(int num)
	{
		if (num < 0)
			rightRotate(Math.abs(num));
		while (num >= numberOfEntries)
			num -= numberOfEntries;
		Node temp = firstNode;
		for (int i = 1; i < numberOfEntries; i++)
		{
			temp = temp.next;
		}
		temp.next = firstNode;
		for (int i = 1; i <= num; i++)
		{
			temp = temp.next;
			firstNode = firstNode.next;
		}
		temp.next = null;
	}

	// Remove from the end and add at the front.  Note that this method should
	// not create any new Node objects -- it is simply moving them.  If num
	// is negative it should still work, actually doing a left rotation.
	public void rightRotate(int num)
	{
		if (num < 0)
			num += numberOfEntries;
		leftRotate(numberOfEntries - num);
	}
	
	// Reverse the nodes in the list.  Note that this method should not create
	// any new Node objects -- it is simply moving them.
	/*public void reverse()									//first attempt, not as efficient as uncommented method
	{
		Node temp1 = firstNode, temp2 = null, tempFirst = firstNode;
		for (int i = 1; i < numberOfEntries; i++)
		{
			if (i == numberOfEntries - 1)
				temp2 = temp1;
			temp1 = temp1.next;
		}
		firstNode = temp1;
		for (int i = 1; i < numberOfEntries - 1; i++)
		{
			temp1.next = temp2;
			temp1 = temp2;
			temp2 = tempFirst;
			for (int j = 1; j < numberOfEntries - i - 1; j++)
				temp2 = temp2.next;
		}	
		temp1.next = temp2;
		if (temp2 != null)
			temp2.next = null;
	}*/

	public void reverse()
	{
		Node temp1 = null, temp2 = null;
		boolean first = true;
		for (Node curr = firstNode; curr != null; curr = temp1)
		{
			temp1 = curr.next;
			if (first)
			{
				curr.next = null;
				first = false;
				temp2 = curr;
			}
			else if (!first)
			{
				curr.next = temp2;
				temp2 = curr;
			}
		}
		firstNode = temp2;
	}
}
