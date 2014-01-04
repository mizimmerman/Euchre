package Euchre;

public class Card {
    public static enum Suit { HEARTS, DIAMONDS, SPADES, CLUBS, NONE };
    public static enum Rank { NINE, TEN, JACK, QUEEN, KING, ACE, NONE };

    
    public static String[] suitNames = {"Hearts", "Diamonds", "Spades", "Clubs", "None"};
    public static String[] rankNames = {"Nine", "Ten", "Jack", "Queen", "King", "Ace", "None"};

    public Suit s;
    public Rank r;
    public Suit sameColor;

    /**Default constructor*/
    public Card() {
        s = Suit.NONE;
        sameColor = Suit.NONE;
        r = Rank.NONE;
    }

    /**Overloaded constructor*/
    public Card(Suit su, Rank ra) {
        s = su;
        r = ra;
        if(su == Suit.HEARTS)
            sameColor = Suit.DIAMONDS;
        else if(su == Suit.DIAMONDS)
            sameColor = Suit.HEARTS;
        else if(su == Suit.SPADES)
            sameColor = Suit.CLUBS;
        else if(su == Suit.CLUBS)
            sameColor = Suit.SPADES;
        else
            System.out.println("Suit was NONE, this should not print.");
    }

    public static Rank incrementRank(Rank ra) {
            if(ra == Rank.NINE)
                    ra = Rank.TEN;
            else if(ra == Rank.TEN)
                    ra = Rank.JACK;
            else if(ra == Rank.JACK)
                    ra = Rank.QUEEN;
            else if(ra == Rank.QUEEN)
                    ra = Rank.KING;
            else if(ra == Rank.KING)
                    ra = Rank.ACE;
            else /*Ace or none*/
                    ra = Rank.NINE;
            return ra;
    }

    public static Suit incrementSuit(Suit su) {
            if(su == Suit.HEARTS)
                    su = Suit.DIAMONDS;
            else if(su == Suit.DIAMONDS)
                    su = Suit.SPADES;
            else if(su == Suit.SPADES)
                    su = Suit.CLUBS;
            else /*Clubs or none*/
                    su = Suit.HEARTS;
            return su;
    }

    public void set(Card c) {
        r = c.r;
        s = c.s;
        sameColor = c.sameColor;
    }
    
    public String toString() {
    return Card.rankNames[r.ordinal()] + " of " + Card.suitNames[s.ordinal()];
    }

    /**Method to check whether the current card beats rhs. Assume the 
     * current card was played first.*/
    public boolean isLessThan(Card rhs, Suit trump) {
        if(r == Rank.JACK && s == trump)
                return false; /*this is right bauer*/
        else if(rhs.r == Rank.JACK && rhs.s == trump)
                return true; /*rhs is right bauer*/
        else if(r == Rank.JACK && sameColor == trump)
                return false; /*neither is right, this is left*/
        else if(rhs.r == Rank.JACK && rhs.sameColor == trump)
                return true; /*neither is right, rhs is left*/
        else if(s == rhs.s)
                return r.ordinal() < rhs.r.ordinal(); /*no bauers, same suit*/
        else if(rhs.s == trump)
                return true; /*no bauers, rhs is trump, this isn't*/
        else
                return false; /*assume this was played first, or is trump*/
    }

    public boolean isFollowingSuit(Suit follow, Suit trump) {
        //if same suit, trump, including left
        if(follow == trump && (s == follow || (sameColor == trump && r == Rank.JACK)))
            return true;
        //else if same suit, not trump color
        else if(s == follow && sameColor != trump)
            return true;		
        //else if same suit, trump color, not left
        else if(s == follow && (sameColor == trump && r != Rank.JACK))
            return true;
        else
            return false;
    }
}



