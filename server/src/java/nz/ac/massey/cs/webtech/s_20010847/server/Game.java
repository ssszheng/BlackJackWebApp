/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.massey.cs.webtech.s_20010847.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author szheng
 */
public class Game {
    private String status = "not start";
    private List<Card> cards;
    private List<Card> aiCards = new ArrayList<>();
    private List<Card> userCards = new ArrayList<>();
    private String winner = "None";
    private String whoseTurn = "User";
    private String message = "No message";
    
    public List<Card> getCards(){
        return cards;
    }
    
    public void createDeck(){
        cards = Card.makeDeck();      
    }
    
    public List<Card> shuffleCards(){
        Collections.shuffle(this.cards);
        return cards;
    }
    
    
    public void removeCard(Card card){
        cards.remove(card);
    }
    
    public List<Card> getAICards(){
        return aiCards;
    }
    public List<Card> getUserCards(){
        return userCards;
    }
    
    public void addAICards(Card card){
        aiCards.add(card);
    }
    public void adduserCards(Card card){
        userCards.add(card);
    }
      
    public String getWhoseTurn(){
        return whoseTurn;
    }
    
    public void setWhoseTurn(String name){
        whoseTurn = name;
    }
    
    public String getStatus(){
        return this.status;
    }
    public void setStatus(String s){
        this.status = s; 
    }
    
    public boolean ACEchecker(List<Card> cardlist){     
        for (Card c: cardlist){
            return c.value == Value.ACE;
        } 
        return false;
    }
    
    
    
    public int scoreWithoutACE(List<Card> cardlist){
        Integer score = 0;
        for (Card c: cardlist){
            switch(c.value){
                case TWO:
                    score+=2;
                    break;
                case THREE:
                    score+=3;
                    break;
                case FOUR:
                    score+=4;
                    break;
                case FIVE:
                    score+=5;
                    break;
                case SIX:
                    score+=6;
                    break;
                case SEVEN:
                    score+=7;
                    break;
                case EIGHT:
                    score+=8;
                    break;                 
                case NINE:
                    score+=9;
                    break;
                case TEN:
                    score+=10;
                    break;
                case JACK:
                    score+=10;
                    break;
                case QUEEN:
                    score+=10;
                    break;
                case KING:
                    score+=10;
                    break;                  
            }
           }
       return score;
    }
    
    
    
    
    
    
    public void setWinner(String winner){
        this.winner = winner;
    }
    
    public String getWinner(){
        return winner;
    }
    
    public void setMessage(String message){
        this.message = message;
    }
     public String setMessage(){
        return message;
    }
}