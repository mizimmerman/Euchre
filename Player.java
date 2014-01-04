package Euchre;

import javax.swing.JOptionPane;

public class Player {
	protected Card[] hand;
	protected String name;
	protected int playerNum;
	protected int handIndex;
	
	public Player() {
		handIndex = 0;
		hand = new Card[5];
                for(int i = 0; i < 5; ++i) {
                    hand[i] = new Card();
                }
	}
	
	public Player(String n) {
		handIndex = 0;
		name = n;
		hand = new Card[5];
                for(int i = 0; i < 5; ++i) {
                    hand[i] = new Card();
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
	
    public Card.Suit callTrump2(Card flipped, boolean screwed) {
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
        //cpu overrides
    }
    
    public boolean goAlone(Card.Suit trump) {
        String title = "Go alone?";
        String message = "You called " + Card.suitNames[trump.ordinal()] +". Go alone?";
        int selection;
        selection = JOptionPane.showOptionDialog(null, message, title, 
                                JOptionPane.YES_NO_OPTION, 0, null, null, null);
        if(selection == JOptionPane.YES_OPTION)			
            return true;
        else
            return false;
    }
    
    public void resetCard(int index) {
        hand[index].r = Card.Rank.NONE;
        hand[index].s = Card.Suit.NONE;
        hand[index].sameColor = Card.Suit.NONE;
    }
    
    public int playCard(Card.Suit follow, HandInfo inf, int leader, int numPlays) {
        //cpu overrides
        return 0;
    }
    
    public boolean isPlayable(int selection, Card.Suit follow, HandInfo inf) {
        if(hand[selection].s == Card.Suit.NONE)
            return false;
        else if(follow == Card.Suit.NONE)
            return true;
        
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
    
    public void setUp1() {
        handIndex = 0;
    }
    
    public void setUp2(Card.Suit trump) {
        //CPU overrides this
    }
    
    /**@return A string format of the player's hand*/
    public String toString() {
        String str =  name + "'s hand:\n";
        for(int i = 0; i < 5; i++)
            str = str + getCard(i).toString() + "\n";
        str += "\n";
        return str;
    }
    
    public static int whosWinning(int leader, HandInfo inf, int numPlays) {
        int curWinner = leader;
        Card curWinnerC = new Card();
        for(int i = (leader+1)%4; i != (leader+numPlays)%4; i=(i+1)%4) {
            curWinnerC.set(inf.trickAr[curWinner]);
            if(!(inf.alone && i == (inf.caller+2)%4))
                if(i != leader)
                    if(curWinnerC.isLessThan(inf.trickAr[i], inf.trump))
                        curWinner = i;
        }
        return curWinner;
    }
}
