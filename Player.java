package Euchre;

import javax.swing.JOptionPane;

public class Player {
	private Card[] hand;
	private String name;
	private int playerNum;
	private int handIndex;
        public String[] handImgFiles;
	
	public Player() {
		handIndex = 0;
		hand = new Card[5];
                handImgFiles = new String[5];
                for(int i = 0; i < 5; ++i) {
                    hand[i] = new Card();
                    handImgFiles[i] = "NoneNone.gif";
                }
	}
	
	public Player(String n) {
		handIndex = 0;
		name = n;
		hand = new Card[5];
                handImgFiles = new String[5];
                for(int i = 0; i < 5; ++i) {
                    hand[i] = new Card();
                    handImgFiles[i] = "NoneNone.gif";
                }
	}
	
	public void setName(String n) {
		name = n;
	}
	
	public String getName() {
		return name;
	}
	
	public void setPlayerNum(int n) {
		playerNum = n;
	}
	
	public int getPlayerNum() {
		return playerNum;
	}
	
    public void addCard(Card c) {
        hand[handIndex].r = c.r;
        hand[handIndex].s = c.s;
        hand[handIndex].sameColor = c.sameColor;
        handImgFiles[handIndex] = Card.rankNames[c.r.ordinal()] 
                + Card.suitNames[c.s.ordinal()] + ".gif";
        ++handIndex;
    }
	
	public Card getCard(int i) {
		return hand[i];
	}
	
    public boolean callTrump1(Card flipped) {
        String title = "Call trump?";
        String message = flipped + " was flipped. Call trump?";
        int selection;
        selection = JOptionPane.showOptionDialog(null, message, title, 
                                JOptionPane.YES_NO_OPTION, 0, null, null, null);
        if(selection == JOptionPane.YES_OPTION)			
            return true;
        else
            return false;
    }
	
    public Card.Suit callTrump2(Card flipped) {
        String selection = new String();
        Card.Suit select = Card.Suit.HEARTS;
        String title = "Call trump?";
        String message = flipped + " was turned down. Call trump?";
        int called = JOptionPane.showOptionDialog(null, message, title, 
                                JOptionPane.YES_NO_OPTION, 0, null, null, null);
        if(called == JOptionPane.YES_OPTION) {
            selection = JOptionPane.showInputDialog(null, "Which suit? " 
                                                + Card.suitNames[flipped.s.ordinal()] 
                                                + " cannot be called.");
            while(true) {
                selection = selection.toLowerCase();
                if(selection.equals("hearts"))
                    return Card.Suit.HEARTS;
                else if(selection.equals("diamonds"))
                    return Card.Suit.DIAMONDS;
                else if(selection.equals("spades"))
                    return Card.Suit.SPADES;
                else if(selection.equals("clubs"))
                    return Card.Suit.CLUBS;
                else
                    selection = JOptionPane.showInputDialog(null, "Invalid input. Which suit? " 
                                                        + Card.suitNames[flipped.s.ordinal()] 
							+ " cannot be called.");
            }
        }
        else
            return Card.Suit.NONE;
    }
	
    public void pickUp(Card flipped) {
        String selectStr = JOptionPane.showInputDialog(null, "Select a card to discard (1-5).");
        boolean goodInput = false;
        int selection = -1;
        do {
            try {
                // attempt to convert the String to an int
                selection = Integer.parseInt(selectStr);
                if(selection >= 1 && selection <= 5)
                    goodInput = true;
                else
                    selectStr = JOptionPane.showInputDialog(null, "Invalid card number. Select a card to discard (1-5).");
            }
            catch (NumberFormatException nfe) {
                selectStr = JOptionPane.showInputDialog(null, "Invalid input. Select a card to discard (1-5).");
            }
        } while (!goodInput);
        JOptionPane.showMessageDialog(null, "You picked up the " + flipped);
            hand[selection] = flipped;
    }
    
    public void playCard(int selection, Card.Suit follow, HandInfo inf, 
                            int leader, int numPlays) {
        
        
        
    }
    
    private boolean isPlayable(int selection, Card.Suit follow, HandInfo inf, 
                                int leader, int numPlays) {
        if(hand[selection].s == Card.Suit.NONE)
            return false;
        
        boolean mustFollow = false;
        for(int i = 0; i < 5; ++i) {
            //if trump and trump lead (including left)
            if(follow == inf.trump &&
               (hand[i].s == follow ||
               (hand[i].sameColor == inf.trump && hand[i].r == Card.Rank.JACK)))
                mustFollow = true;
            //else if same suit as lead and not trump color
            else if(hand[i].s == follow && hand[i].sameColor != inf.trump)
                mustFollow = true;
            //else if same suit as lead and trump color (so uninclude jack)
            else if(hand[i].s == follow &&
                    (hand[i].sameColor == inf.trump && hand[i].r != Card.Rank.JACK))
                mustFollow = true;
        }
        if(!mustFollow || isFollowing(hand[selection], follow, inf.trump))
            return true;
        else
            return false;
    }
    
    private boolean isFollowing(Card c, Card.Suit follow, Card.Suit trump) {
        //if trump and trump lead (including left)
            if(follow == trump &&
               (c.s == follow ||
               (c.sameColor == trump && c.r == Card.Rank.JACK)))
                return true;
            //else if same suit as lead and not trump color
            else if(c.s == follow && c.sameColor != trump)
                return true;
            //else if same suit as lead and trump color (so uninclude jack)
            else if(c.s == follow &&
                    (c.sameColor == trump && c.r != Card.Rank.JACK))
                return true;
            else
                return false;
    }
    
    /**@return A string format of the player's hand*/
    public String toString() {
        String str =  name + "'s hand:\n";
        for(int i = 0; i < 5; i++)
            str = str + getCard(i).toString() + "\n";
        str += "\n";
        return str;
    }
}