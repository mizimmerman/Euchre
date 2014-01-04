/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Euchre;

/**
 *
 * @author OWNER
 */
public class CPU extends Player {
    private enum Risk { ALONE, SAFE, STANDARD, GUTSY, NO};
    
    int[] numSuits;
    int[] handValues;
    int[] numOfEachSuit;
    Card[] hasSeen;
    int hasSeenIndex;
    
    public CPU() {
        super();
        numSuits = new int[4];
        handValues = new int[4];
        numOfEachSuit = new int[4];
        hasSeen = new Card[24];
        for(int i = 0; i < 24; ++i)    
            hasSeen[i] = new Card();
        hasSeenIndex = 0;
    }
    
    public CPU(String n){
        super(n);
        numSuits = new int[4];
        handValues = new int[4];
        numOfEachSuit = new int[4];
        hasSeen = new Card[24];
        for(int i = 0; i < 24; ++i)    
            hasSeen[i] = new Card();
        hasSeenIndex = 0;
    }

    public boolean callTrump1(Card flipped) {
        findAllValues();
        Risk rLevel = determineRisk();
        if(decision(flipped.s).ordinal() <= rLevel.ordinal())
            return true;
        else
            return false;
    }
    
    public void pickUp(Card flipped) {
        int selection = -1;
        do {
        int[] tempNumEach = {0, 0, 0, 0};
        
        for(int i = 0; i < 5; ++i) {
            if(hand[i].sameColor == flipped.s && hand[i].r == Card.Rank.JACK)
                ++tempNumEach[flipped.s.ordinal()];
            else
                ++tempNumEach[hand[i].s.ordinal()];
        }
        int fewestCards = 6;
        Card.Suit fewestCardsIndex = Card.Suit.NONE;
        Card.Suit checker = Card.Suit.HEARTS;
        Card.Suit[] ties = new Card.Suit[3];
        int tiesIndex = 0;
        do {
            //if current suit has the same number of cards as the low, add to ties
            if(tempNumEach[checker.ordinal()] == fewestCards && checker != flipped.s) {
                if(tiesIndex == 0) {
                    ties[tiesIndex++] = checker;
                    ties[tiesIndex++] = fewestCardsIndex;
                }
                else
                    ties[tiesIndex++] = checker;
            }
            //else if current suit has lowest cards, set as low
            else if(tempNumEach[checker.ordinal()] < fewestCards && checker != flipped.s && tempNumEach[checker.ordinal()] != 0) {
                fewestCards = tempNumEach[checker.ordinal()];
                fewestCardsIndex = checker;
                tiesIndex = 0;
            }
            checker = Card.incrementSuit(checker);
        } while(checker != Card.Suit.HEARTS);
        
        //deal with ties (pick lowest card if just one of each suit)
        //if there's two of two suits (that means 2,2,1 trump), just get rid of a random low one for now
	
        if(tiesIndex > 0) {
            Card[] tiesC = new Card[4];
            int tiesCIndex = 0;
            int tiesSel = -1;
            //populate an array of the possible cards to get rid of
            for(int i = 0; i < tiesIndex; ++i) {
                for(int j = 0; j < 5; ++j) {
                    if(ties[i] == hand[j].s && !(ties[i] == flipped.sameColor && hand[j].r == Card.Rank.JACK)) {
                        tiesC[tiesCIndex] = new Card();
                        tiesC[tiesCIndex++].set(hand[j]);
                    }
                }
            }
            //pick which one is lowest
            for(int i = 0; i < tiesCIndex; ++i)
                if(tiesSel == -1 || tiesC[i].r.ordinal() < tiesC[tiesSel].r.ordinal())
                    tiesSel = i;
            for(int i = 0; i < 5; ++i) {
                if(tiesC[tiesSel].s == hand[i].s && tiesC[tiesSel].r == hand[i].r) {
                    selection = i;
                    break;
                }
            }
        }
        else /*no ties*/ {
            for(int i = 0; i < 5; ++i) {
                if(fewestCardsIndex == hand[i].s && !(fewestCardsIndex == flipped.sameColor && hand[i].r != Card.Rank.JACK)) {
                    selection = i;
                    break;
                }
            }
        }
            if(selection == -1)
                System.out.println(this);
        }
        while(selection == -1);
        hand[selection].set(flipped);
    }
    
    private void findAllValues() {
        Card.Suit trump = Card.Suit.HEARTS;
        setNumSuits();
        for(int i = 0; i < 4; ++i) {
            handValues[i] = findSingleValue(trump);
            trump = Card.incrementSuit(trump);
        }
    }
    
    private void setNumSuits() {
        int[] suitArr = new int[4];
        Card.Suit trump = Card.Suit.HEARTS;
        do {
            //determine how many of each suit there are
            for(int i = 0; i < 5; ++i) {
                if(hand[i].sameColor == trump && hand[i].r == Card.Rank.JACK)
                    ++suitArr[trump.ordinal()];
                else
                    ++suitArr[hand[i].s.ordinal()];
            }
            
            //any non-zero suit amount increments numSuits
            for(int i = 0; i < 4; ++i)
                if(suitArr[i] != 0)
                    ++numSuits[trump.ordinal()];
            
            //reset suitArr
            for(int i = 0; i < 4; ++i)
                suitArr[i] = 0;
            
            trump = Card.incrementSuit(trump);
        } 
        while(trump != Card.Suit.HEARTS);
    }
    
    private int findSingleValue(Card.Suit s) {
        int value = 0;
        //decide value for number of suits
        if(numSuits[s.ordinal()] == 1)
            value += 15;
        else if(numSuits[s.ordinal()] == 2)
            value += 10;
        else if(numSuits[s.ordinal()] == 3)
            value += 3;
        
        //decide value for individual cards
        for(int i = 0; i < 5; ++i) {
            if(hand[i].s == s) {
                if(hand[i].r == Card.Rank.JACK)
                    value += 20;
                else if(hand[i].r == Card.Rank.ACE)
                    value += 10;
                else if(hand[i].r == Card.Rank.KING)
                    value += 8;
                else if(hand[i].r == Card.Rank.QUEEN)
                    value += 6;
                else if(hand[i].r == Card.Rank.TEN)
                    value += 4;
                else /*nine*/
                    value += 3;
            }
            else if(hand[i].r == Card.Rank.ACE)
                value += 2;
            else if(hand[i].r == Card.Rank.KING)
                value += 1;
            else if(hand[i].sameColor == s && hand[i].r == Card.Rank.JACK)
                value += 14;
	}
        return value;
    }
    
    private Risk determineRisk() {
        return Risk.STANDARD;
    }
    
    private Risk decision(Card.Suit s) {
        if(handValues[s.ordinal()] > 44)
            return Risk.ALONE;
	else if(handValues[s.ordinal()] > 36)
            return Risk.SAFE;
	else if(handValues[s.ordinal()] > 28)
            return Risk.STANDARD;
	else if(handValues[s.ordinal()] > 20)
            return Risk.GUTSY;
	else
            return Risk.NO;
    }
    
    public Card.Suit callTrump2(Card flipped, boolean screwed) {
        Risk rLevel = determineRisk();

        handValues[flipped.s.ordinal()] = -1; //make sure turned down suit can't be called

        int heartsval = handValues[0];
        int diamondsval = handValues[1];
        int spadesval = handValues[2];
        int clubsval = handValues[3];

        if(heartsval >= diamondsval && heartsval >= spadesval && heartsval >= clubsval) {
            if(decision(Card.Suit.HEARTS).ordinal() <= rLevel.ordinal() || screwed)
                return Card.Suit.HEARTS;
        }
        else if(diamondsval >= heartsval && diamondsval >= spadesval && diamondsval >= clubsval) {
                if(decision(Card.Suit.DIAMONDS).ordinal() <= rLevel.ordinal() || screwed)
                        return Card.Suit.DIAMONDS;
        }
        else if(spadesval >= diamondsval && spadesval >= heartsval && heartsval >= clubsval) {
                if(decision(Card.Suit.SPADES).ordinal() <= rLevel.ordinal() || screwed)
                        return Card.Suit.SPADES;
        }
        else {
            if(decision(Card.Suit.CLUBS).ordinal() <= rLevel.ordinal() || screwed)
                return Card.Suit.CLUBS;
        }
        return Card.Suit.NONE;
    }

    private void setNumOfEachSuit(Card.Suit s) {
        for(int i = 0; i < 5; ++i) {
            if(hand[i].sameColor == s && hand[i].r == Card.Rank.JACK)
                ++numOfEachSuit[s.ordinal()];
            else
                ++numOfEachSuit[hand[i].s.ordinal()];
        }
    }
    
    public boolean goAlone(Card.Suit trump) {
        return decision(trump) == Risk.ALONE;
    }
    
    public void setUp1() {
        handIndex = 0;
        hasSeenIndex = 0;
    }
    
    public void setUp2(Card.Suit trump) {
        setNumOfEachSuit(trump);
    }
    
    public int playCard(Card.Suit follow, HandInfo inf, int leader, int numPlays) {
        int selection = -1;
        do {
            if(inf.trickNum == 4) {
                for(int i = 0; i < 5; ++i)
                    if(hand[i].s != Card.Suit.NONE)
                        selection = i;
            }
            else if(leader == playerNum)
                selection = playLead(inf);
            else {
                int curLeader = whosWinning(leader, inf, numPlays);
                if(curLeader % 2 == playerNum % 2)
                    selection = playOff(inf, follow);
                else
                    selection = playToWin(inf, follow, leader, numPlays);
            }
            if(selection == -1) {
                System.out.println("You died with CPU card picking");
                System.out.println(toString());
            }
                        
        } while(selection == -1);
        Card temp = hand[selection];

	if(temp.sameColor == inf.trump && temp.r == Card.Rank.JACK)
		--numOfEachSuit[temp.sameColor.ordinal()];
	else
		--numOfEachSuit[temp.s.ordinal()];
        
        return selection;
    }
    
    private int playLead(HandInfo inf) {
        int selection = -1;
        if(numOfEachSuit[inf.trump.ordinal()] == 5-inf.trickNum) {
            //all they got is trump, find highest probs
            for(int i = 0; i < 5; ++i)
                if(hand[i].s != Card.Suit.NONE && 
                  (selection == -1 || hand[selection].isLessThan(hand[i], inf.trump)))
                    selection = i;
        } else {
            for(int i = 0; i < 5; ++i) {
                if(hand[i].s != Card.Suit.NONE) {
                    //they have a good hand and have trump left: play it, and play it high
                    if(handValues[inf.trump.ordinal()] > 42 && numOfEachSuit[inf.trump.ordinal()] != 0) {
                        if(hand[i].s == inf.trump || (hand[i].sameColor == inf.trump && hand[i].r == Card.Rank.JACK))
                            if(selection == -1 || hand[selection].isLessThan(hand[i], inf.trump))
                                selection = i;
                    }
                    else /*average or bad hand, or no trump: play highest offsuit*/ {
                        if(!(hand[i].s == inf.trump || (hand[i].sameColor == inf.trump && hand[i].r == Card.Rank.JACK)))
                            if(selection == -1 || hand[i].r.ordinal() > hand[selection].r.ordinal())
                                selection = i;
                    }
                }
            }
        }
        return selection;
    }
    
    private int playOff(HandInfo inf, Card.Suit follow) {
        boolean mustFollow = false;
        boolean[] playable = {false, false, false, false, false};
        int selection = -1;
        
        for(int i = 0; i < 5; ++i) {
            //if trump, including left
            if(follow == inf.trump &&
                    (hand[i].s == follow ||
                    (hand[i].sameColor == inf.trump && hand[i].r == Card.Rank.JACK))) {
                mustFollow = true;
                playable[i] = true;
            }
            //else if same suit, not trump color
            else if(hand[i].s == follow && hand[i].sameColor != inf.trump) {
                mustFollow = true;
                playable[i] = true;
            }
            //else if same suit, trump color, not left
            else if(hand[i].s == follow &&
                    (hand[i].sameColor == inf.trump && hand[i].r != Card.Rank.JACK)) {
                mustFollow = true;
                playable[i] = true;
            }
        }
        if(mustFollow) {
            for(int i = 0; i < 5; ++i) {
                if(playable[i])
                    if(selection == -1 || hand[i].isLessThan(hand[selection], inf.trump))
                        selection = i;
            }
        } 
        else {
            if(numOfEachSuit[inf.trump.ordinal()] == 5-inf.trickNum) {
                //all they got is trump
                for(int i = 0; i < 5; ++i) {
                    if(hand[i].s != Card.Suit.NONE && 
                       (selection == -1 || hand[i].isLessThan(hand[selection], inf.trump))) {
                        selection = i;
                    }
                }
            }
            else {
                Card.Suit fewestCards = Card.Suit.NONE;
                if((numOfEachSuit[Card.Suit.HEARTS.ordinal()] <= numOfEachSuit[Card.Suit.DIAMONDS.ordinal()] || Card.Suit.DIAMONDS == inf.trump || numOfEachSuit[Card.Suit.DIAMONDS.ordinal()] == 0 || numOfEachSuit[Card.Suit.DIAMONDS.ordinal()] == -1) && 
                   (numOfEachSuit[Card.Suit.HEARTS.ordinal()] <= numOfEachSuit[Card.Suit.SPADES.ordinal()] || Card.Suit.SPADES == inf.trump || numOfEachSuit[Card.Suit.SPADES.ordinal()] == 0 || numOfEachSuit[Card.Suit.SPADES.ordinal()] == -1) &&
                   (numOfEachSuit[Card.Suit.HEARTS.ordinal()] <= numOfEachSuit[Card.Suit.CLUBS.ordinal()] || Card.Suit.CLUBS == inf.trump || numOfEachSuit[Card.Suit.CLUBS.ordinal()] == 0 || numOfEachSuit[Card.Suit.CLUBS.ordinal()] == -1) &&
                   (Card.Suit.HEARTS != inf.trump && numOfEachSuit[Card.Suit.HEARTS.ordinal()] != 0))
                    fewestCards = Card.Suit.HEARTS;
                else if((numOfEachSuit[Card.Suit.DIAMONDS.ordinal()] <= numOfEachSuit[Card.Suit.HEARTS.ordinal()] || Card.Suit.HEARTS == inf.trump || numOfEachSuit[Card.Suit.HEARTS.ordinal()] == 0 || numOfEachSuit[Card.Suit.HEARTS.ordinal()] == -1) && 
                   (numOfEachSuit[Card.Suit.DIAMONDS.ordinal()] <= numOfEachSuit[Card.Suit.SPADES.ordinal()] || Card.Suit.SPADES == inf.trump || numOfEachSuit[Card.Suit.SPADES.ordinal()] == 0 || numOfEachSuit[Card.Suit.SPADES.ordinal()] == -1) &&
                   (numOfEachSuit[Card.Suit.DIAMONDS.ordinal()] <= numOfEachSuit[Card.Suit.CLUBS.ordinal()] || Card.Suit.CLUBS == inf.trump || numOfEachSuit[Card.Suit.CLUBS.ordinal()] == 0 || numOfEachSuit[Card.Suit.CLUBS.ordinal()] == -1) &&
                   (Card.Suit.DIAMONDS != inf.trump && numOfEachSuit[Card.Suit.DIAMONDS.ordinal()] > 0))
                    fewestCards = Card.Suit.DIAMONDS;
                else if((numOfEachSuit[Card.Suit.SPADES.ordinal()] <= numOfEachSuit[Card.Suit.DIAMONDS.ordinal()] || Card.Suit.DIAMONDS == inf.trump || numOfEachSuit[Card.Suit.DIAMONDS.ordinal()] == 0 || numOfEachSuit[Card.Suit.DIAMONDS.ordinal()] == -1) && 
                   (numOfEachSuit[Card.Suit.SPADES.ordinal()] <= numOfEachSuit[Card.Suit.HEARTS.ordinal()] || Card.Suit.HEARTS == inf.trump || numOfEachSuit[Card.Suit.HEARTS.ordinal()] == 0 || numOfEachSuit[Card.Suit.HEARTS.ordinal()] == -1) &&
                   (numOfEachSuit[Card.Suit.SPADES.ordinal()] <= numOfEachSuit[Card.Suit.CLUBS.ordinal()] || Card.Suit.CLUBS == inf.trump || numOfEachSuit[Card.Suit.CLUBS.ordinal()] == 0 || numOfEachSuit[Card.Suit.CLUBS.ordinal()] == -1) &&
                   (Card.Suit.SPADES != inf.trump && numOfEachSuit[Card.Suit.SPADES.ordinal()] > 0))
                    fewestCards = Card.Suit.SPADES;
                else if((numOfEachSuit[Card.Suit.CLUBS.ordinal()] <= numOfEachSuit[Card.Suit.DIAMONDS.ordinal()] || Card.Suit.DIAMONDS == inf.trump || numOfEachSuit[Card.Suit.DIAMONDS.ordinal()] <= 0) && 
                   (numOfEachSuit[Card.Suit.CLUBS.ordinal()] <= numOfEachSuit[Card.Suit.HEARTS.ordinal()] || Card.Suit.HEARTS == inf.trump || numOfEachSuit[Card.Suit.HEARTS.ordinal()] <= 0) &&
                   (numOfEachSuit[Card.Suit.CLUBS.ordinal()] <= numOfEachSuit[Card.Suit.SPADES.ordinal()] || Card.Suit.SPADES == inf.trump || numOfEachSuit[Card.Suit.SPADES.ordinal()] <= 0) &&
                   (Card.Suit.CLUBS != inf.trump && numOfEachSuit[Card.Suit.CLUBS.ordinal()] > 0))
                    fewestCards = Card.Suit.CLUBS;
                else
                    System.out.println("Die at fewestCards");

                //decide what to play
                if(numOfEachSuit[inf.trump.ordinal()] > 0) {
                    //short suit if possible
                    for(int i = 0; i < 5; ++i) {
                        if(hand[i].s == fewestCards && (selection == -1 || hand[i].isLessThan(hand[selection], inf.trump)))
                            selection = i;
                    }
                }
                else {
                    //keep best possible cards, esp aces
                    for(int i = 0; i < 5; ++i) {
                        if(hand[i].s != Card.Suit.NONE && 
                           (selection == -1 || hand[i].r.ordinal() <= hand[selection].r.ordinal()))
                            selection = i;
                    }
                }
            }
        }
        return selection;
    }
    
    private int playToWin(HandInfo inf, Card.Suit follow, int leader, int numPlays) {
        boolean mustFollow = false;
        boolean[] playable = {false, false, false, false, false};
        int selection = -1;
        
        for(int i = 0; i < 5; ++i) {
            //if trump, including left
            if(follow == inf.trump &&
                    (hand[i].s == follow ||
                    (hand[i].sameColor == inf.trump && hand[i].r == Card.Rank.JACK))) {
                mustFollow = true;
                playable[i] = true;
            }
            //else if same suit, not trump color
            else if(hand[i].s == follow && hand[i].sameColor != inf.trump) {
                mustFollow = true;
                playable[i] = true;
            }
            //else if same suit, trump color, not left
            else if(hand[i].s == follow &&
                    (hand[i].sameColor == inf.trump && hand[i].r != Card.Rank.JACK)) {
                mustFollow = true;
                playable[i] = true;
            }
        }
        if(!mustFollow)
            for(int i = 0; i < 5; ++i)
                if(hand[i].s != Card.Suit.NONE)
                    playable[i] = true;
        
        Card curWinner = inf.trickAr[Player.whosWinning(leader, inf, numPlays)];
        for(int i = 0; i < 5; ++i) {
            if(playable[i] && curWinner.isLessThan(hand[i], inf.trump))
                selection = i;
        }
        if(selection == -1)
            return playOff(inf, follow); //can't win
        else
            return selection;
    }
}

