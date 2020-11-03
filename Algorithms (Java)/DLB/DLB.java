import java.io.*;

public class DLB {
	private Node root;
	private Node curr;

	public DLB()
	{
		this.root = null;
	}

	public DLB(char data, Node child, Node sibling) {
		this.root = new Node(data);
		if (child != null)
			root.setChild(child);
		if (sibling != null)
			root.setSibling(sibling);
	}
	
	private class Node {
		private char data;
		private Node sibling;
		private Node child;

		public Node () {
			data = 0;
			sibling = null;
			child = null;
		}

		public Node (char data) {
			this.data = data;
			sibling = null;
			child = null;
		}

		public Node (char data, Node sibling, Node child) {
			this.data = data;
			this.sibling = sibling;
			this.child = child;
		}

		public boolean hasData() {
			if (data == 0)
				return false;
			return true;
		}

		public char getData() {
			return data;
		}

		public boolean hasChild() {
			if (child != null)
				return true;
			return false;
		}

		public Node getChild() {
			return child;
		}

		public void setChild(Node c) {
			child = c;
		}
		
		public boolean hasSibling() {
			if (sibling != null)
				return true;
			return false;
		}
		
		public Node getSibling() {
			return sibling;
		}

		public void setSibling(Node s) {
			sibling = s;
		}
	}

	/**
	 * adds new word to DLB
	 * @param w word to be inserted into DLB
	 * @return true if success, false if failure
	 */
	public boolean put(String w) {
		curr = root;
		int i = 0;
		char[] word = w.toCharArray();
		int length = word.length;

		if (root == null) {
			root = new Node(word[i]);
			curr = root;
			i++;
		}
		
		else {
			while (i < length) {

				// Traverse siblings until desired character is found, create new node if not found
				while (curr.getData() != word[i]) {
					if (!curr.hasSibling()) {
						curr.setSibling(new Node(word[i]));
					}
					curr = curr.getSibling();
				}

				i++;

				// current node is correct, move to child/create new child
				if (curr.hasChild()) {
					curr = curr.getChild();
				}
				else {
					if (i < length) {
						curr.setChild(new Node(word[i]));
						curr = curr.getChild();
					}
				}
			}
		}
		// set terminator node
		curr.setChild(new Node('^'));
		return true;
	}

	/**
	 * searches for words that start with prefix
	 * @param prefix start of word
	 * @param options pointer to an array that will be filled with possible words
	 * @param num number of words desired
	 * @return number of words found that start with prefix (max 5)
	 */
	public int search(StringBuilder prefix, String[] options, int num) {
		int count = 0;
		int i = 0;
		StringBuilder word;
		if (root != null) {
			curr = root;

			while (i < prefix.length() && count <= num) {
				/** traverse breadth **/
				while (curr.getData() != prefix.charAt(i)) {
					if (!curr.hasSibling() && i != prefix.length()) {
						return count;
					}
					curr = curr.getSibling();
				}

				i++;


			}
		}

		return count;
	}

	/**
	 * exports contents of DLB to text file
	 * @param filename name of output file
	 * @return true if successful, false if failed
	 */
	public boolean export(String filename) {
		File outFile = new File(filename);
		try {
			PrintWriter writer = new PrintWriter(outFile);

			/** Recursively traverse DLB to gather all words and write each to file **/

			return true;
		} catch (FileNotFoundException e) {}
		return false;
	}
}