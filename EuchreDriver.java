package Euchre;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class EuchreDriver extends JFrame {
    private Container contents;
    private JButton[] cardButtons;
    private JLabel[] playerCardImages;
    private JLabel[] cpu1CardImages;
    private JLabel[] cpu2CardImages;
    private JLabel[] cpu3CardImages;
    
    private JLabel[] cardsPlayed;
    private JLabel flippedImage;
    private JLabel displayText;
    private JLabel cpuScoreImg;
    private JLabel playerScoreImg;
    
    private JLabel dealer;
    private JLabel dealerName;
    private JLabel caller;
    private JLabel callerName;
    private JLabel trump;
    private JLabel trumpImg;
    private ImageIcon[] trumpImgFiles;
    private JLabel cpuTricks;
    private JLabel playerTricks;
    private JLabel cpuTricksNum;
    private JLabel playerTricksNum;
    
    private ImageIcon[] cards;
    private ImageIcon[] sCards;
    
    private ImageIcon[] cpuScores;
    private ImageIcon[] playerScores;
    
    private Player[] players;
    private int playerChoice;
    
    
    private final int BACK_IMG = 24;
    private final int NONE_IMG = 25;
    
    private Pause pause;
    private final double PAUSE1 = 0.9;
    private final double DEAL_PAUSE = 0.35;
    
    public EuchreDriver() {
        super("Euchre by ZimmiSoft (beta)");
        
        contents = getContentPane();
        contents.setLayout(null);
        
        //buttons!
        int xPos = 130;
        cardButtons = new JButton[5];
        ButtonHandler bh_hand = new ButtonHandler();
        for(int i = 0; i < 5; ++i) {
            cardButtons[i] = new JButton("Card" + Integer.toString(i+1));
            cardButtons[i].setBounds(xPos, 580, 70, 30);
            xPos += 80;
            cardButtons[i].setVisible(false);
            contents.add(cardButtons[i]);
            cardButtons[i].addActionListener(bh_hand);
        }
        
        //load images
        int counter = 0;
        cards = new ImageIcon[26];
        for(int i = 0; i < 6; ++i)
            for(int j = 0; j < 4; ++j)
                cards[counter++] = 
                  new ImageIcon(Card.rankNames[i] + Card.suitNames[j] + ".gif");
        cards[counter++] = new ImageIcon("Back.gif");
        cards[counter] = new ImageIcon("NoneNone.gif");
        
        int sCounter = 0;
        sCards = new ImageIcon[26];
        for(int i = 0; i < 6; ++i)
            for(int j = 0; j < 4; ++j)
                sCards[sCounter++] = 
                  new ImageIcon("S" + Card.rankNames[i] + Card.suitNames[j] + ".gif");
        sCards[sCounter++] = new ImageIcon("SBack.gif");
        sCards[sCounter] = new ImageIcon("SNoneNone.gif");
        
        xPos = 130;
        int yPos = 105;
        playerCardImages = new JLabel[5];
        cpu1CardImages = new JLabel[5];
        cpu2CardImages = new JLabel[5];
        cpu3CardImages = new JLabel[5];
        
        for(int i = 0; i < 5; ++i) {
            playerCardImages[i] = new JLabel(cards[NONE_IMG]);
            cpu1CardImages[i] = new JLabel(cards[NONE_IMG]);
            cpu2CardImages[i] = new JLabel(cards[NONE_IMG]);
            cpu3CardImages[i] = new JLabel(cards[NONE_IMG]);
            
            playerCardImages[i].setBounds(xPos, 480, 70, 90);
            cpu1CardImages[i].setBounds(20, yPos, 90, 70);
            cpu2CardImages[i].setBounds(xPos, 20, 70, 90);
            cpu3CardImages[i].setBounds(540, yPos, 90, 70);
            contents.add(playerCardImages[i]);
            contents.add(cpu1CardImages[i]);
            contents.add(cpu2CardImages[i]);
            contents.add(cpu3CardImages[i]);
            xPos += 80;
            yPos += 75;
        }
        
        cardsPlayed = new JLabel[4];
        for(int i = 0; i < 4; ++i) {
            if(i % 2 == 0)
                cardsPlayed[i] = new JLabel(cards[NONE_IMG]);
            else
                cardsPlayed[i] = new JLabel(sCards[NONE_IMG]);
            contents.add(cardsPlayed[i]);
        }
        
        cardsPlayed[0].setBounds(290, 325, 70, 90);
        cardsPlayed[1].setBounds(185, 255, 90, 70);
        cardsPlayed[2].setBounds(290, 165, 70, 90);
        cardsPlayed[3].setBounds(375, 255, 90, 70);
        
        flippedImage = new JLabel(cards[NONE_IMG]);
        flippedImage.setBounds(290, 250, 70, 90);
        contents.add(flippedImage);
        
        
        cpuScores = new ImageIcon[11];
        playerScores = new ImageIcon[11];
        for(int i = 0; i < 11; i++) {
            cpuScores[i] = new ImageIcon("bScore" + i + ".gif");
            playerScores[i] = new ImageIcon("rScore" + i + ".gif");
        }
        cpuScoreImg = new JLabel(cpuScores[0]);
        playerScoreImg = new JLabel(playerScores[0]);
        cpuScoreImg.setBounds(23, 15, 84, 80);
        playerScoreImg.setBounds(543, 15, 84, 80);
        contents.add(cpuScoreImg);
        contents.add(playerScoreImg);
        
        displayText = new JLabel();
        displayText.setText("Welcome to Euchre!");
        displayText.setBounds(200, 440, 235, 30);
        displayText.setHorizontalAlignment(JLabel.CENTER);
        displayText.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
        contents.add(displayText);
        
        dealer = new JLabel("Dealer");
        dealer.setBounds(20, 510, 90, 30);
        dealer.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));
        dealer.setHorizontalAlignment(JLabel.CENTER);
        contents.add(dealer);
        dealerName = new JLabel("");
        dealerName.setBounds(20, 550, 90, 20);
        dealerName.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        dealerName.setHorizontalAlignment(JLabel.CENTER);
        contents.add(dealerName);
        caller = new JLabel("Caller");
        caller.setBounds(540, 510, 90, 30);
        caller.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));
        caller.setHorizontalAlignment(JLabel.CENTER);
        contents.add(caller);
        callerName = new JLabel("");
        callerName.setBounds(540, 550, 90, 20);
        callerName.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        callerName.setHorizontalAlignment(JLabel.CENTER);
        contents.add(callerName);
        trump = new JLabel("Trump");
        trump.setBounds(300, 620, 50, 22);
        trump.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        trump.setHorizontalAlignment(JLabel.CENTER);
        trump.setVisible(false);
        contents.add(trump);
        trumpImgFiles = new ImageIcon[5];
        for(int i = 0; i < 5; ++i) {
            trumpImgFiles[i] = new ImageIcon(Card.suitNames[i] + ".gif");
        }
        trumpImg = new JLabel(trumpImgFiles[Card.Suit.NONE.ordinal()]);
        trumpImg.setBounds(311, 645, 27, 28);
        contents.add(trumpImg);
        cpuTricks = new JLabel("CPU Tricks");
        cpuTricks.setBounds(150, 620, 100, 22);
        cpuTricks.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        cpuTricks.setHorizontalAlignment(JLabel.CENTER);
        cpuTricks.setVisible(false);
        contents.add(cpuTricks);
        playerTricks = new JLabel("Player Tricks");
        playerTricks.setBounds(410, 620, 100, 22);
        playerTricks.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        playerTricks.setHorizontalAlignment(JLabel.CENTER);
        playerTricks.setVisible(false);
        contents.add(playerTricks);
        cpuTricksNum = new JLabel("0");
        cpuTricksNum.setBounds(150, 645, 100, 25);
        cpuTricksNum.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        cpuTricksNum.setHorizontalAlignment(JLabel.CENTER);
        cpuTricksNum.setVisible(false);
        contents.add(cpuTricksNum);
        playerTricksNum = new JLabel("0");
        playerTricksNum.setBounds(410, 645, 100, 25);
        playerTricksNum.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        playerTricksNum.setHorizontalAlignment(JLabel.CENTER);
        playerTricksNum.setVisible(false);
        contents.add(playerTricksNum);
        
        setSize(670, 720);
        setVisible(true);
        
        pause = new Pause();
        
        //begin setup code for actual game
        Deck deck = new Deck();
        
        players = new Player[4];
        players[0] = new Player("Mike");
        players[1] = new CPU("CPU1");
        players[2] = new CPU("CPU2");
        players[3] = new CPU("CPU3");
        for(int i = 0; i < 4; ++i)
            players[i].setPlayerNum(i);
        
        int[] teamScores = {0, 0};
        HandInfo inf = new HandInfo();
        for(int i = 0; i < 4; ++i) {
            inf.trickAr[i] = new Card();
        }
        inf.alone = false;
        inf.dealer = 0;
        
        inf.trickAr = new Card[4];
        for(int i = 0; i < 4; ++i) {
            inf.trickAr[i] = new Card();
        }
        
        int[] tricksTaken = new int[2];
        
        while(teamScores[0] < 10 && teamScores[1] < 10) {
            dealerName.setText(players[inf.dealer].getName());
            trumpImg.setIcon(trumpImgFiles[Card.Suit.NONE.ordinal()]);
            trump.setVisible(false);
            cpuTricks.setVisible(false);
            playerTricks.setVisible(false);
            cpuTricksNum.setVisible(false);
            playerTricksNum.setVisible(false);
            callerName.setText("");
            for(int i = 0; i < 4; ++i)
                players[i].setUp1();
            
            deck.shuffle();
            Card flipped = deal(deck, inf);
            int imgIndex = flipped.s.ordinal() + (flipped.r.ordinal() * 4);
            flippedImage.setIcon(cards[imgIndex]);
            inf.caller = firstCall(flipped, inf);
            tricksTaken[0] = 0;
            tricksTaken[1] = 0;
            if(inf.caller == -1) {
                flippedImage.setIcon(cards[BACK_IMG]);
                inf.caller = secondCall(flipped, inf);
                trumpImg.setIcon(trumpImgFiles[inf.trump.ordinal()]);
                trump.setVisible(true);
                displayText.setText(players[inf.caller].getName() + 
                    " has called " + Card.suitNames[inf.trump.ordinal()] + ".");
                pause.stall(PAUSE1);
            }
            else {
                
                inf.trump = flipped.s;
                inf.alone = players[inf.caller].goAlone(inf.trump);
                trumpImg.setIcon(trumpImgFiles[inf.trump.ordinal()]);
                trump.setVisible(true);
                displayText.setText(players[inf.caller].getName() + 
                    " has called " + Card.suitNames[inf.trump.ordinal()] + ".");
                pause.stall(PAUSE1);
                int sel;
                if(inf.dealer != 0)
                    players[inf.dealer].pickUp(flipped);
                else {
                    playerPickUp(flipped);
                    playerCardImages[playerChoice].setIcon(cards[imgIndex]);
                }
            }
            flippedImage.setIcon(cards[NONE_IMG]);
            
            playerTricksNum.setText("0");
            cpuTricksNum.setText("0");
            cpuTricks.setVisible(true);
            playerTricks.setVisible(true);
            playerTricksNum.setVisible(true);
            cpuTricksNum.setVisible(true);
            
            callerName.setText(players[inf.caller].getName());
            
            for(int i = 0; i < 4; ++i)
                players[i].setUp2(inf.trump);
            
            playHand(tricksTaken, inf);
            if(inf.caller % 2 == 0) {
                if(tricksTaken[0] == 5) {
                    displayText.setText("You took all five!");
                    pause.stall(PAUSE1);
                    if(inf.alone)
                        teamScores[0] += 4;
                    else
                        teamScores[0] += 2;
                }
                else if(tricksTaken[0] >= 3) {
                    displayText.setText("You won the hand!");
                    pause.stall(PAUSE1);
                    teamScores[0] += 1;
                }
                else {
                    displayText.setText("You got euchred.");
                    pause.stall(PAUSE1);
                    teamScores[1] += 2;
                }
            }
            else {
                if(tricksTaken[1] == 5) {
                    displayText.setText("The CPUs took all five.");
                    pause.stall(PAUSE1);
                    if(inf.alone)
                        teamScores[1] += 4;
                    else
                        teamScores[1] += 2;
                }
                else if(tricksTaken[1] >= 3) {
                    displayText.setText("You lost the hand.");
                    pause.stall(PAUSE1);
                    teamScores[1] += 1;
                }
                else {
                    displayText.setText("You euchred us!");
                    pause.stall(PAUSE1);
                    teamScores[0] += 2;
                }
            }
            inf.dealer = (inf.dealer + 1) %4;
            playerScoreImg.setIcon(playerScores[teamScores[0]]);
            cpuScoreImg.setIcon(cpuScores[teamScores[1]]);
        }
        
        
        
        
        
        flippedImage.setIcon(cards[NONE_IMG]);
       
    }
    
    /**A function to deal a five card hand to 4 players.
     * @param d The playing deck.
     * @return The card "flipped up" as a result of the deal.
     */
    private Card deal(Deck d, HandInfo inf) {
        boolean firstTime = true;
        for(int i=(inf.dealer+1)%4; i!=(inf.dealer+1)%4 || firstTime; i=(i+1)%4) {
            firstTime = false;
            players[i].setUp1();
            for(int j = 0; j < 3; ++j) {
                Card next = d.getTop();
                players[i].addCard(next);
                if(i == 0) {
                    int index = next.s.ordinal() + (next.r.ordinal() * 4);
                    playerCardImages[j].setIcon(cards[index]);
                }
                else if(i == 1)
                    cpu1CardImages[j].setIcon(sCards[BACK_IMG]);
                else if(i == 2)
                    cpu2CardImages[j].setIcon(cards[BACK_IMG]);
                else
                    cpu3CardImages[j].setIcon(sCards[BACK_IMG]);
            }
            pause.stall(DEAL_PAUSE);
        }
        firstTime = true;
        for(int i=(inf.dealer+1)%4; i!=(inf.dealer+1)%4 || firstTime; i=(i+1)%4) {
            firstTime = false;
            for(int j = 3; j < 5; ++j) {
                Card next = d.getTop();
                players[i].addCard(next);
                if(i == 0) {
                    int index = next.s.ordinal() + (next.r.ordinal() * 4);
                    playerCardImages[j].setIcon(cards[index]);
                }
                else if(i == 1)
                    cpu1CardImages[j].setIcon(sCards[BACK_IMG]);
                else if(i == 2)
                    cpu2CardImages[j].setIcon(cards[BACK_IMG]);
                else
                    cpu3CardImages[j].setIcon(sCards[BACK_IMG]);
            }
            pause.stall(DEAL_PAUSE);
        }
        return d.getTop();
    }
    
    private int firstCall(Card flipped, HandInfo inf) {
        int playerNum = (inf.dealer + 1) % 4;
        int numPasses = 0;

        do {
            if(players[playerNum].callTrump1(flipped)) {
                //check alone later
                return playerNum;
            }
            else {
                ++numPasses;
                displayText.setText(players[playerNum].getName() + " passes.");
                pause.stall(PAUSE1);
                playerNum = (playerNum + 1) % 4;
            }
                
        } while(numPasses < 4);
        return -1;
    }
    
    private int secondCall(Card flipped, HandInfo inf) {
        int playerNum = (inf.dealer + 1) % 4;
        int numPasses = 0;
        do {
            inf.trump = players[playerNum].callTrump2(flipped, false);
            if(inf.trump != Card.Suit.NONE) {
                inf.alone = players[playerNum].goAlone(inf.trump);
                return playerNum;
            }
            else {
                ++numPasses;
                displayText.setText(players[playerNum].getName() + " passes.");
                pause.stall(PAUSE1);
                playerNum = (playerNum + 1) % 4;
            }
        } while(numPasses < 3);
        //screw the dealer
        do {
            inf.trump = players[inf.dealer].callTrump2(flipped, true);
        } while(inf.trump == Card.Suit.NONE);
        inf.alone = players[inf.dealer].goAlone(inf.trump);
        return inf.dealer;
    }
    
    private void playHand(int[] tricksTaken, HandInfo inf) {
        int curPlayer;
        int lead = (inf.dealer+1)%4;
        if(inf.alone && lead == (inf.caller+2)%4)
            lead = (lead+1)%4;
        
        for(inf.trickNum = 0; inf.trickNum < 5; ++inf.trickNum) {            
            //leader goes first with no restraint
            displayText.setText(players[lead].getName() + "'s turn.");
            if(lead != 0) {
                 int sel = players[lead].playCard(Card.Suit.NONE, inf, lead, 0);
                 playCard(lead, sel, inf);
                 pause.stall(PAUSE1);
            }
            else {
                playersTurn(Card.Suit.NONE, inf);
                pause.stall(PAUSE1);
            }
            
            curPlayer = (lead + 1) % 4;
            Card.Suit follow;
            if(inf.trickAr[lead].r == Card.Rank.JACK && inf.trump == inf.trickAr[lead].sameColor)
                follow = inf.trump;
            else
                follow = inf.trickAr[lead].s;
            //begin other players turns
            for(int i = 0; i < 3; ++i) {
                if(!(inf.alone && curPlayer == (inf.caller+2)%4)) {
                    displayText.setText(players[curPlayer].getName() + "'s turn.");
                    pause.stall(PAUSE1);
                    if(curPlayer != 0) {
                        int sel = players[curPlayer].playCard(follow, inf, lead, i+1);
                        playCard(curPlayer, sel, inf);
                   }
                   else {
                       playersTurn(follow, inf);
                   }
                    
                }
                curPlayer = (curPlayer + 1) % 4;
            }
            //check  who won and clear the cards
            lead = Player.whosWinning(lead, inf, 4);
            
            displayText.setText(players[lead].getName() + " won the trick.");
            pause.stall(PAUSE1);
            ++tricksTaken[lead%2];
            if(lead % 2 == 0)
                playerTricksNum.setText(new Integer(tricksTaken[0]).toString());
            else
                cpuTricksNum.setText(new Integer(tricksTaken[1]).toString());
            
            cardsPlayed[0].setIcon(cards[NONE_IMG]);
            cardsPlayed[1].setIcon(sCards[NONE_IMG]);
            cardsPlayed[2].setIcon(cards[NONE_IMG]);
            cardsPlayed[3].setIcon(sCards[NONE_IMG]);
        }
    }
    
    private void playersTurn(Card.Suit follow, HandInfo inf) {
        playerChoice = -1;
        
        //make card selection buttons available
        for(int i = 0; i < 5; ++i)
            cardButtons[i].setVisible(true);
        
        //make a message :)
        displayText.setText("Select a card to play.");
        do { 
            while(playerChoice == -1) {
                //can I stall the game like this? yup :)
            }

            if(players[0].isPlayable(playerChoice, follow, inf)) {
                playCard(0, playerChoice, inf);
            }
            else {
                playerChoice = -1;
                displayText.setText("Invalid pick, try again.");
            }
        } while(playerChoice == -1);
        
        for(int i = 0; i < 5; ++i)
            cardButtons[i].setVisible(false);
        
    }
    
    private void playerPickUp(Card flipped) {
        playerChoice = -1;
        
        //make card selection buttons available
        for(int i = 0; i < 5; ++i)
            cardButtons[i].setVisible(true);
        
        //make a message :)
        displayText.setText("Select a card to discard.");
        while(playerChoice == -1) {
                //can I stall the game like this? yup :)
        }
        
        players[0].hand[playerChoice].set(flipped);
        
        for(int i = 0; i < 5; ++i)
            cardButtons[i].setVisible(false);
    }
    
    private void playCard(int playerNum, int selection, HandInfo inf) {
        inf.trickAr[playerNum].set(players[playerNum].getCard(selection)); 
        if(playerNum % 2 == 0) {
            int index = players[playerNum].getCard(selection).s.ordinal() + 
                    (players[playerNum].getCard(selection).r.ordinal() * 4);
            cardsPlayed[playerNum].setIcon(cards[index]);
            players[playerNum].resetCard(selection);
            if(playerNum == 0)
                playerCardImages[selection].setIcon(cards[NONE_IMG]);
            else
                cpu2CardImages[selection].setIcon(cards[NONE_IMG]);
        }
        else {
            int index = players[playerNum].getCard(selection).s.ordinal() + 
                    (players[playerNum].getCard(selection).r.ordinal() * 4);
            cardsPlayed[playerNum].setIcon(sCards[index]);
            players[playerNum].resetCard(selection);
            if(playerNum == 1)
                cpu1CardImages[selection].setIcon(cards[NONE_IMG]);
            else
                cpu3CardImages[selection].setIcon(cards[NONE_IMG]);
        }  
    }
    
    private class ButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent ae) {
            if(ae.getSource() == cardButtons[0])
                playerChoice = 0;
            else if(ae.getSource() == cardButtons[1])
                playerChoice = 1;
            else if(ae.getSource() == cardButtons[2])
                playerChoice = 2;
            else if(ae.getSource() == cardButtons[3])
                playerChoice = 3;
            else if(ae.getSource() == cardButtons[4])
                playerChoice = 4;
        }
    }
    
    private class Pause {
        public void stall(double seconds) {
            try {
                Thread.sleep( (int)( seconds * 1000 ) );
            }
            catch ( InterruptedException e ) {
                e.printStackTrace( );
            }
        }
    }
    
    public static void main(String[] args) {
        EuchreDriver drv = new EuchreDriver();
        drv.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
