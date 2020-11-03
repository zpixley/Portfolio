import java.util.*;
import java.io.*;

public class Assig5
{
	static String[] table = new String[26];
	static BinaryNode<Character> root = new BinaryNode('\0');
	public static void main(String[] args)
	{
		try{
			String menu = "\nPlease choose from the following:\n1) Encode a text string\n2) Decode a Huffman string\n3) Quit";
			Scanner inScan = new Scanner(System.in);
			File f = new File(args[0]);
			Scanner fScan = new Scanner(f);
			try{restoreTree(fScan, root);}catch(Exception e){}
			System.out.println("\nThe Huffman Tree has been restored");
			buildTable(new StringBuilder(), root);
			System.out.println(menu);
			int choice = inScan.nextInt();
			//	start of main program loop
			while (choice != 3)
			{
				//	encode string
				if (choice == 1)
				{
					System.out.println("Enter a String from the following characters:");
					for (int i = 0; table[i] != null; i++)
						System.out.print((char) (i + 65));
					System.out.println();
					String word = inScan.next().toUpperCase();
					System.out.print(encodeString(word));
				}
				//	decode string
				else if (choice == 2)
				{
					printTable();
					System.out.println("Please enter a Huffman string (one line, no spaces)");
					String huffString = inScan.next();
					System.out.print(decode(new StringBuilder("Text String:\n"), root, huffString, 0));
				}
				System.out.println(menu);
				choice = inScan.nextInt();
			}
			System.out.println("Good-bye");
		}catch(FileNotFoundException e){System.out.println("Invalid filename");}
	}
	
	public static void restoreTree(Scanner fScan, BinaryNode<Character> node) throws Exception
	{
		String s = fScan.nextLine();
		//	base case, node is leaf
		if (s.charAt(0) == 'L')
		{
			String[] line = s.split(" ");
			node.setData(line[1].charAt(0));
		}
		else
		{
			node.setData('\0');
			node.setLeftChild(new BinaryNode<Character>('\0'));
			node.setRightChild(new BinaryNode<Character>('\0'));
			try{
				restoreTree(fScan, node.getLeftChild());
				restoreTree(fScan, node.getRightChild());
			}catch(Exception e){}
		}
	}
	
	public static void buildTable(StringBuilder code, BinaryNode<Character> node)
	{
		if (node.getData() != '\0')
		{
			table[(int) node.getData() - 65] = code.toString();
		}
		else
		{
			// check left child
			code.append("0");
			buildTable(code, node.getLeftChild());
			code.deleteCharAt(code.length() - 1);
			// check right child
			code.append("1");
			buildTable(code, node.getRightChild());
			code.deleteCharAt(code.length() - 1);
		}
	}
	
	public static void printTable()
	{
		System.out.println("Here is the encoding table:");
		for (int i = 0; table[i] != null; i++)
				System.out.println((char)(i + 65) + ": " + table[i]);
	}

	public static String encodeString(String word)
	{
		StringBuilder code = new StringBuilder("Huffman String:\n");
		for (int i = 0; i < word.length(); i++)
		{
			//	if requested letter has no code, invalid entry
			if (table[word.charAt(i) - 65] == null)
				return "There was an error in your text string\n";
			code.append(table[word.charAt(i) - 65] + "\n");
		}
		return code.toString();
	}
	
	public static String decode(StringBuilder word, BinaryNode<Character> node, String code, int loc)
	{		
		if (loc > code.length() - 1)
		{
			word.append(node.getData() + "\n");
		}
		else
		{
			//	found leaf
			if (node.getData() != '\0')
			{
				word.append(node.getData());
				return decode(word, root, code, loc);
			}
			//	move to left child
			else if (code.charAt(loc) == '0')
			{
				decode(word, node.getLeftChild(), code, loc + 1);
			}
			//	move to right child
			else if (code.charAt(loc) == '1')
			{
				decode(word, node.getRightChild(), code, loc + 1);
			}
		}
		for (int i = 0; i < word.length(); i++)
		{
			//	if there are any null characters, entry was invalid
			if (word.charAt(i) == '\0')
				return "There was an error in your Huffman string\n";
		}
		return word.toString();
	}
}