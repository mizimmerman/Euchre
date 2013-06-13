package Euchre;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class EuchreDriver extends JFrame {
    private Container contents;
    private JButton[] cardButtons;
    private JLabel[] playerCardImages;
    private ImageIcon[] cards;
    
    private Player[] players;
    
    public EuchreDriver() {
        super("Euchre by Z-Soft");
        
        contents = getContentPane();
        contents.setLayout(null);
        
        int xPos = 95;
        cardButtons = new JButton[5];
        ButtonHandler bh_hand = new ButtonHandler();
        for(int i = 0; i < 5; ++i) {
            cardButtons[i] = new JButton("Card" + Integer.toString(i+1));
            cardButtons[i].setBounds(xPos, 520, 70, 30);
            xPos += 80;
            contents.add(cardButtons[i]);
            cardButtons[i].addActionListener(bh_hand);
        }
        
        //load images
        int counter = 0;
        cards = new ImageIcon[25];
        for(int i = 0; i < 6; ++i)
            for(int j = 0; j < 4; ++j)
                cards[counter++] = 
                  new ImageIcon(Card.rankNames[i] + Card.suitNames[j] + ".gif");
        cards[counter] = new ImageIcon("NoneNone.gif");
        
        xPos = 95;
        playerCardImages = new JLabel[5];
        for(int i = 0; i < 5; ++i) {
            playerCardImages[i] = new JLabel(cards[counter]);
            playerCardImages[i].setBounds(xPos, 420, 70, 90);
            contents.add(playerCardImages[i]);
            xPos += 80;
        }
        
        //begin setup code for actual game
        players = new Player[4];
        for(int i = 0; i < 4; ++i) {
            players[i] = new Player("Play" + i);
        }        
        
        Deck deck = new Deck();
        deck.shuffle();
        
        setSize(600, 650);
        setVisible(true);
        
        Card flipped = deal(deck);
        System.out.println(flipped);
        for(int i = 0; i < 4; i++)
            System.out.println(players[i]);
    }
    
    /**A function to deal a five card hand to 4 players.
     * @param d The playing deck.
     * @return The card "flipped up" as a result of the deal.
     */
    private Card deal(Deck d) {
        for(int i = 0; i < 4; ++i) {
            for(int j = 0; j < 3; ++j) {
                Card next = d.getTop();
                players[i].addCard(next);
                if(i == 0) {
                    int index = next.s.ordinal() + (next.r.ordinal() * 4);
                    playerCardImages[j].setIcon(cards[index]);
                }
            }
        }
        for(int i = 0; i < 4; ++i)
            for(int j = 3; j < 5; ++j) {
                Card next = d.getTop();
                players[i].addCard(next);
                if(i == 0) {
                    int index = next.s.ordinal() + (next.r.ordinal() * 4);
                    playerCardImages[j].setIcon(cards[index]);
                }
            }
        return d.getTop();
    }
    
    private class ButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent ae) {
            if(ae.getSource() == cardButtons[0])
                //try to play card 0
                System.out.print("placeholder");
            else if(ae.getSource() == cardButtons[1])
                //try to play card 1
                System.out.print("placeholder");
            else if(ae.getSource() == cardButtons[2])
                //try to play card 2
                System.out.print("placeholder");
            else if(ae.getSource() == cardButtons[3])
                //try to play card 3
                System.out.print("placeholder");
            else if(ae.getSource() == cardButtons[4])
                //try to play card 4
                System.out.print("placeholder");
        }
    }
    
    public static void main(String[] args) {
        EuchreDriver drv = new EuchreDriver();
        drv.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}