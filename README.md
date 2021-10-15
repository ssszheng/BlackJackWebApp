# BlackJack Game Web Application
A Java/JavaScript implementation of the BlackJack game.

## Technology: 
J2EE- Java Servlet 

## Library: 
Apache http client library etc.

## Server

A game engine that uses the http services. The game engine is implemented in multiple servlets providing the services.
The user will play the game with the computer (the dealer).

Each side is dealt 2 cards, and they take turns (user first) to decide whether to hit (deal one more card) or stand (stop receiving card).
Given a hand the user has at most two options to choose when making a move; hit or stand.
When it is the dealer’s turn, the computer’s action is predetermined;
hit when the value of dealer’s cards is 17 or less, otherwise stand. 
The hand which reaches 21 or gets closest to it wins. 
At the end of the game display the winner, and allow the user reset and start a new game.

Game state is maintained in a session. The server use a standard 52 card deck and maintain the state of user’s hand and the computer’s hand.
There are no bets involved (i.e. no money to keep track of). Game state (player hands, whoseTurn, winner, etc) is stored in a servlet session. 
This also work if the user disables cookies in the browser.

The server maintains the number of games played and percentage of games won by the users for as long as the server has been running. This game
stats is persistent so it can stored in a file  stats.json). It is to be updated and displayed with every game played.

## Test
Used the standard junit tests without any particular web application testing frameworks. 
For each service there is at least one test.

## User Interface
JSP page blackjack.jsp that displays the game board and has functionality to
create new games, makes moves, display winner, and game stats.
## Deployment
The web application deployed in a docker
container and host it on Heroku cloud.
To start a new game: https://web352assignment2.herokuapp.com/jack/start
