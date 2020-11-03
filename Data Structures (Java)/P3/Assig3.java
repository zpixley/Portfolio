import java.io.*;
import java.util.*;

public class Assig3
{	
	static char[][] theBoard;
	static int rows, cols;
	public static void main(String[] args)
	{
		Scanner inScan = new Scanner(System.in);
		loadBoard();
		printBoard();
		
		System.out.println("Please enter the phrase (sep. by single spaces):");
		String s = inScan.nextLine().toLowerCase();
		while (!s.equals(""))
		{
			System.out.println("Looking for: " + s);
			String[] phrase = s.split(" ");
			System.out.println("containing " + phrase.length + " words");
			int x = 0, y = 0;
			boolean found = false;
			for (int r = 0; r < rows && !found; r++)
			{
				for (int c = 0; c < cols && !found; c++)
				{
					found = findWord(r, c, 1, 0, phrase);
					if (!found)
						found = findWord(r, c, 2, 0, phrase);
					if (!found)
						found = findWord(r, c, 3, 0, phrase);
					if (!found)
						found = findWord(r, c, 4, 0, phrase);
				}
			}
			
			if (found)
			{
				System.out.println("The phrase: " + s + "\nwas found: ");
				printBoard();
			}
			else
				System.out.println("The phrase: " + s + "\nwas not found");
			System.out.println("Please enter the phrase (sep. by single spaces):");
			s = inScan.nextLine().toLowerCase();
		}
	}
	
	public static boolean findWord(int r, int c, int dir, int index, String[] phrase)
	{
		String word = phrase[index];
		if (r == theBoard.length || c == theBoard[0].length)
			return false;
		if (index == phrase.length)
			return true;
		// right
		if (dir == 1)
		{
			for (int i = 0; i < word.length(); i++)
			{
				if (word.charAt(i) != theBoard[r][c])
					return false;
				else
				{
					theBoard[r][c] = Character.toUpperCase(theBoard[r][c]);
					c++;
				}
			}
			return true;
		}
		// down
		else if (dir == 2)
		{
			for (int i = 0; i < word.length(); i++)
			{
				if (word.charAt(i) != theBoard[r][c])
					return false;
				else
				{
					theBoard[r][c] = Character.toUpperCase(theBoard[r][c]);
					r++;
				}
			}
			return true;
		}
		// left
		else if (dir == 3)
		{
			for (int i = 0; i < word.length(); i++)
			{
				if (word.charAt(i) != theBoard[r][c])
					return false;
				else
				{
					theBoard[r][c] = Character.toUpperCase(theBoard[r][c]);
					c--;
				}
			}
			return true;
		}
		// up
		else if (dir == 4)
		{
			for (int i = 0; i < word.length(); i++)
			{
				if (word.charAt(i) != theBoard[r][c])
					return false;
				else
				{
					theBoard[r][c] = Character.toUpperCase(theBoard[r][c]);
					c++;
				}
			}
			return true;
		}
		return false;
	}
	
	public static void loadBoard()
	{
		Scanner inScan = new Scanner(System.in);
		Scanner fReader;
		File fName;
        String fString = "", word = "";
       
       	// Make sure the file name is valid
        while (true)
        {
           try
           {
               System.out.println("Please enter grid filename:");
               fString = inScan.nextLine();
               fName = new File(fString);
               fReader = new Scanner(fName);
               break;
           }
           catch (IOException e)
           {
               System.out.println("Problem " + e);
           }
        }

		// Parse input file to create 2-d grid of characters
		String [] dims = (fReader.nextLine()).split(" ");
		rows = Integer.parseInt(dims[0]);
		cols = Integer.parseInt(dims[1]);
		
		theBoard = new char[rows][cols];

		for (int i = 0; i < rows; i++)
		{
			String rowString = fReader.nextLine();
			for (int j = 0; j < rowString.length(); j++)
			{
				theBoard[i][j] = Character.toLowerCase(rowString.charAt(j));
			}
		}
	}
	
	public static void printBoard()
	{
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < cols; j++)
			{
				System.out.print(theBoard[i][j] + " ");
			}
			System.out.println();
		}
	}
}