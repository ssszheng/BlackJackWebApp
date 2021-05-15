/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.massey.cs.webtech.s_20010847.server;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author szheng
 */

enum Suite {HEARTS, DIAMONDS, SPADES,CLUBS};
enum Value {ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING};


public class Card{
    public Suite suite;
    public Value value;
    public Card (Suite s, Value v) {
        suite = s;
        value = v;
    }
    public Card() {
    }
    
    public static List<Card> makeDeck() {
        List<Card> result = new ArrayList<>();
        for (Suite s : Suite.values ())
            for (Value v : Value.values ())
                result.add (new Card (s, v));
        return result;
    
    }
    @Override
    public String toString(){
        return suite + ":" + value;
}
}