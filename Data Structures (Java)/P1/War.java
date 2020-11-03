public class War
{
	@SuppressWarnings("unchecked")
	static MultiDS<Card> theDeck = createDeck(), deck0 = new MultiDS<Card>(52), deck1 = new MultiDS<Card>(52), disc0 = new MultiDS<Card>(52), disc1 = new MultiDS<Card>(52), hand = new MultiDS<Card>(52);
	static int round = 1;
	public static void main(String[] args)
	{
		int roundLimit = Integer.parseInt(args[0]);
		boolean done = false;
		System.out.println("Welcome to the Game of War!\n");
		System.out.println("Now dealing the cards to the players...\n");
		for (int i = 0; i < 52; i++)
		{
			if (i % 2 == 0)
				deck0.addItem(theDeck.removeItem());
			else
				deck1.addItem(theDeck.removeItem());
		}
		System.out.println("Here is Player 0's Hand:\n" + deck0 + "\n");
		System.out.println("Here is Player 1's Hand:\n" + deck1 + "\n");
		System.out.println("Starting the WAR!\n");
		while (!done)
		{
			while (round <= roundLimit)
			{
				if (deck0.empty())
				{
					System.out.println("\t\tGetting and shuffling the pile for player 0");
					while (!disc0.empty())
					deck0.addItem(disc0.removeItem());
					deck0.shuffle();
				}
				Card c0 = deck0.removeItem();
				if (deck1.empty())
				{
					System.out.println("\t\tGetting and shuffling the pile for player 1");
					while (!disc1.empty())
						deck1.addItem(disc1.removeItem());
					deck1.shuffle();
				}
				Card c1 = deck1.removeItem();
				eval(c0, c1, 0);
			}
			
			if (round >= roundLimit)
			{
				round--;
				while (!disc0.empty())
					deck0.addItem(disc0.removeItem());
				while (!disc1.empty())
					deck1.addItem(disc1.removeItem());
				System.out.println("After " + round + " rounds here is the status:");
				System.out.println("\tPlayer 0 has " + deck0.size() + " cards");
				System.out.println("\tPlayer 1 has " + deck1.size() + " cards");
				if (deck0.size() > deck1.size())
					System.out.println("Player 0 is the WINNER!");
				else if (deck0.size() < deck1.size())
					System.out.println("Player 1 is the WINNER!");
				else
					System.out.println("It is a STALEMATE");
				done = true;
			}
			else if (deck0.empty())
			{
				System.out.println("\nPlayer 0 is out of cards!");
				System.out.println("Player 1 is the WINNER!");
				done = true;
			}
			else if (deck1.empty())
			{
				System.out.println("\nPlayer 1 is out of cards!");
				System.out.println("Player 0 is the WINNER!");
				done = true;
			}
		}
	}
	
	public static MultiDS createDeck()
	{
		MultiDS<Card> theDeck = new MultiDS<Card>(52);
		for (Card.Suits s: Card.Suits.values())
		{
			for (Card.Ranks r: Card.Ranks.values())
			{
				theDeck.addItem(new Card(s, r));
			}
		}
		theDeck.shuffle();
		return theDeck;
	}
	
	public static void eval(Card c0, Card c1, int k)
	{
		if (k == 0)
		{
			hand.addItem(c0);
			hand.addItem(c1);
		}
		if (c0.compareTo(c1) > 0)
		{
			System.out.println("Player 0 Wins Rnd " + round + ": " + c0 + " beats " + c1 + " : " + hand.size() +" cards");
			while (!hand.empty())
				disc0.addItem(hand.removeItem());
			round++;
		}
		else if (c0.compareTo(c1) < 0)
		{
			System.out.println("Player 1 Wins Rnd " + round + ": " + c0 + " loses to " + c1 + " : " + hand.size() +" cards");
			while (!hand.empty())
				disc1.addItem(hand.removeItem());
			round++;
		}
		else if (c0.compareTo(c1) == 0)
		{
			System.out.println("WAR: " + c0 + " ties " + c1 + "\n");
			if (deck0.empty())
			{
				System.out.println("\t\tGetting and shuffling the pile for player 0");
				while (!disc0.empty())
					deck0.addItem(disc0.removeItem());
				deck0.shuffle();
			}
			hand.addItem(deck0.removeItem());
			if (deck1.empty())
			{
				System.out.println("\t\tGetting and shuffling the pile for player 1");
				while (!disc1.empty())
					deck1.addItem(disc1.removeItem());
				deck1.shuffle();
			}
			hand.addItem(deck1.removeItem());
			if (deck0.empty())
			{
				System.out.println("\nPlayer 0 is out of cards!");
				System.out.println("Player 1 is the WINNER!");
				System.exit(0);
			}
			Card c2 = deck0.removeItem();
			if (deck1.empty())
			{
				System.out.println("\nPlayer 1 is out of cards!");
				System.out.println("Player 0 is the WINNER!");
				System.exit(0);
			}
			Card c3 = deck1.removeItem();
			hand.addItem(c2);
			hand.addItem(c3);
			eval(c2, c3, 1);
		}
	}
}