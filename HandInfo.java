/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Euchre;

/**
 *
 * @author OWNER
 */
public class HandInfo {
    public int dealer;
    public int caller;
    public boolean alone;
    Card[] trickAr;
    Card.Suit trump;
    int trickNum;
    
    public HandInfo() {
        dealer = 0;
        caller = -1;
        alone = false;
        trickAr = new Card[4];
        trump = Card.Suit.NONE;
        trickNum = 0;
    }
    
    
}
