package Euchre;

public class Deck {
    private Card[] deck;
    private int dealIndex;
	
    /**Default constructor, creates ordered deck*/
    public Deck() {
        dealIndex = 0;
        deck = new Card[24];
        int i = 0;
        Card.Rank ra = Card.Rank.NINE;
        while(i < 6) {
            deck[i] = new Card(Card.Suit.HEARTS, ra);
            ra = Card.incrementRank(ra);
            ++i;
        }
        while(i < 12) {
            deck[i] = new Card(Card.Suit.DIAMONDS, ra);
            ra = Card.incrementRank(ra);
            ++i;
        }
        while(i < 18) {
            deck[i] = new Card(Card.Suit.SPADES, ra);
            ra = Card.incrementRank(ra);
            ++i;
        }
        while(i < 24) {
            deck[i] = new Card(Card.Suit.CLUBS, ra);
            ra = Card.incrementRank(ra);
            ++i;
        }
    }
	
    private int cut(Card[] top, Card[] bottom) {
		//get random integer
		int randomInteger = (int)(Math.floor(Math.random() * 23));
		int i = 0;
		//create top deck
		while(i <= randomInteger) {
			top[i].s = deck[i].s;
                        top[i].sameColor = deck[i].sameColor;
                        top[i].r = deck[i].r;
                        ++i;
                }
		//create bottom deck
		for(int j = 0; i < 24; j++) {
			bottom[j].s = deck[i].s;
                        bottom[j].sameColor = deck[i].sameColor;
                        bottom[j].r = deck[i].r;
                        ++i;
                }

		//note: cuts take place AFTER the randomInteger'th card
		return randomInteger;
	}
	
    public void shuffle() {
        dealIndex = 0;
        int shuffles, c, i, j, k; //c is cut position, i is top index, j is bottom index, k is deck index
        Card[] top = new Card[24];
        Card[] bottom = new Card[24];
        for(int z = 0; z < 24; ++z) {
            top[z] = new Card();
            bottom[z] = new Card();
        }
        shuffles = (int)(Math.floor(Math.random() * 5)) + 7;
        for(int index = 0; index < shuffles; index++) {
            c = cut(top, bottom);
            //determining the number of cards in each deck
            i = c;
            j = 22 - c;
            k = 23;

            //fill in the main deck with the two other decks
            while(i >= 0 && j >= 0) {
                deck[k].s = top[i].s;
                deck[k].sameColor = top[i].sameColor;
                deck[k--].r = top[i--].r;
                deck[k].s = bottom[j].s;
                deck[k].sameColor = bottom[j].sameColor;
                deck[k--].r = bottom[j--].r;
            }
			
            //once one deck runs out
            while(i >= 0) {
                deck[k].s = top[i].s;
                deck[k].sameColor = top[i].sameColor;
                deck[k--].r = top[i--].r;
            }
            while(j >= 0) {
                deck[k].s = bottom[j].s;
                deck[k].sameColor = bottom[j].sameColor;
                deck[k--].r = bottom[j--].r;
            }
        }
    }
    
    public Card getTop() {
        Card retCard = new Card();
        retCard.s = deck[dealIndex].s;
        retCard.r = deck[dealIndex].r;
        retCard.sameColor = deck[dealIndex].sameColor;
        ++dealIndex;
        return retCard;
    }
    
    /**for testing use only*/
    public Card at(int index) {
        return deck[index];
    }
}
