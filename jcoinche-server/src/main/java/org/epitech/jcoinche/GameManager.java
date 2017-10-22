package org.epitech.jcoinche;

import java.util.Vector;

public class GameManager {
    private Deck    deck = new Deck();
    private boolean newTurn = true;
    private boolean bid = false;
    private int     playerTurn = 0;
    private int teamA = 0;
    private int teamB = 0;
    private Vector<Player> players;

    GameManager(Vector<Player> player) {
        players = player;
        players.elementAt(0).sendCommand("Vous êtes le joueur 1, vous êtes dans l'équipe A avec le joueur 3");
        players.elementAt(1).sendCommand("Vous êtes le joueur 2, vous êtes dans l'équipe B avec le joueur 4");
        players.elementAt(2).sendCommand("Vous êtes le joueur 3, vous êtes dans l'équipe A avec le joueur 1");
        players.elementAt(3).sendCommand("Vous êtes le joueur 4, vous êtes dans l'équipe B avec le joueur 2");
    }

    private void startTurn() {
        deck.newDeck();
        newTurn = false;
        for (Player player : players) {
            player.sendCommand("Les cartes sont distribuées");
            player.clearCards();
            for (int i = 0 ; i < 8 ; ++i) {
                player.addCard(deck.pickACard());
            }
            player.sendCardList();
        }
    }

    public void continueGame() {
        if (newTurn) {
            startTurn();
        }
    }
}
