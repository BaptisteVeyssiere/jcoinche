package org.epitech.jcoinche;

import java.util.Vector;

public class GameManager {
    private int  currentBid = 0;
    private int  currentColor = 0;
    private int  currentAtout = 0;
    private int currentBidOwner = 0;
    private int skipInARow = 0;
    private Deck    deck = new Deck();
    private boolean coinche = false;
    private boolean surcoinche = false;
    private boolean newTurn = true;
    private boolean bid = false;
    private boolean waitingResponse = false;
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
        skipInARow = 0;
        playerTurn = 0;
        currentBid = 0;
        for (Player player : players) {
            player.sendCommand("Les cartes sont distribuées");
            player.clearCards();
            for (int i = 0 ; i < 8 ; ++i) {
                player.addCard(deck.pickACard());
            }
            player.sendCardList();
        }
    }

    public void continueGame() throws InterruptedException {
        if (newTurn) {
            startTurn();
        } else if (!bid) {
            handleBid();
        } else if (bid) {
            sendMessageToAllExcept(playerTurn, "Les enchères sont terminées. Le joueur " + (playerTurn + 1) + " joue.");
            sendMessageTo(playerTurn, "Les enchères sont terminées, vous avez la main.");
        }
    }

    private void handleBid() throws InterruptedException {
        int         min;

        if (!waitingResponse) {
            waitingResponse = true;
            sendMessageTo(playerTurn, "A vous de jouer, vous pouvez:");
            if (currentBid < 160 && !coinche) {
                if (currentBid == 0)
                    min = 80;
                else
                    min = currentBid + 10;
                sendMessageTo(playerTurn, "Enchérir (minimum " + String.valueOf(min) + "): Syntaxe: Enchérir \"valeur\"");
            }
            if (currentBid > 0 && !coinche && !isInSameTeam(playerTurn, currentBidOwner)) {
                sendMessageTo(playerTurn, "Coincher");
            }
            else if (currentBid > 0 && coinche && playerTurn == currentBidOwner)
                sendMessageTo(playerTurn, "Surcoincher");
            sendMessageTo(playerTurn, "Passer");
            sendMessageToAllExcept(playerTurn, "Le joueur " + String.valueOf(playerTurn + 1) + " joue...");
        } else {
            if (players.elementAt(playerTurn).getCommandNbr() >= 1) {
                handleBidMessage(players.elementAt(playerTurn).getCommand());
            }
        }
    }

    private void handleBidMessage(String message) {
        String[] tmp;
        int nbr;
        int save;

        if (message.startsWith("Enchérir")) {
            if (coinche)
                return ;
                tmp = message.split(" ");
                if (tmp.length != 3)
                    return;
                try {
                    nbr = Integer.parseInt(tmp[1]);
                } catch (NumberFormatException e) {
                    return;
                }
             if (nbr <= currentBid || nbr > 160 ||
                    nbr % 10 != 0)
                return;
            save = currentColor;
            currentColor = 0;
            if (tmp[2].equals("Coeur"))
                currentAtout = 3;
            else if (tmp[2].equals("Trefle"))
                currentAtout = 0;
            else if (tmp[2].equals("Pique"))
                currentAtout = 1;
            else if (tmp[2].equals("Carreau"))
                currentAtout = 2;
            else if (tmp[2].equals("SA"))
                currentColor = 2;
            else if (tmp[2].equals("TA"))
                currentColor = 1;
            else {
                currentColor = save;
                return;
            }
            currentBid = nbr;
            currentBidOwner = playerTurn;
            sendMessageTo(playerTurn, "Vous menez l'enchère: " + nbr + " " + tmp[2]);
            sendMessageToAllExcept(playerTurn, "Le joueur " + (playerTurn + 1) + " enchérit: " + nbr + " " + tmp[2]);
            playerTurn = (playerTurn + 1) % 4;
            waitingResponse = false;
            skipInARow = 0;
            return ;
        }
        else if (message.startsWith("Passer")) {
            skipInARow++;
            waitingResponse = false;
            sendMessageTo(playerTurn, "Vous passez");
            sendMessageToAllExcept(playerTurn, "Le joueur " + (playerTurn + 1) + " passe");
            playerTurn = (playerTurn + 1) % 4;
            if (skipInARow == 3 && currentBid > 0) {
                playerTurn = currentBidOwner;
                bid = true;
            } else if (skipInARow == 4) {
                currentBid = 0;
                newTurn = true;
                playerTurn = 0;
                sendMessageToAllExcept(playerTurn, "Personne n'a placé d'offre, redistribution des cartes.");
                sendMessageTo(playerTurn, "Personne n'a placé d'offre, redistribution des cartes.");
            }
            if (coinche) {
                playerTurn = currentBidOwner;
                bid = true;
            }
            return ;
        }
        else if (message.startsWith("Coincher")) {
            if (currentBid == 0 || isInSameTeam(playerTurn, currentBidOwner))
                return ;
            coinche = true;
            waitingResponse = false;
            skipInARow = 0;
            sendMessageTo(playerTurn, "Vous coinchez");
            sendMessageToAllExcept(playerTurn, "Le joueur " + (playerTurn + 1) + "coinche");
            playerTurn = currentBidOwner;
            return ;
        }
        else if (message.startsWith("Surcoincher")) {
            if (!coinche || playerTurn != currentBidOwner)
                return;
            waitingResponse = false;
            surcoinche = true;
            skipInARow = 0;
            bid = true;
            sendMessageTo(playerTurn, "Vous surcoinchez");
            sendMessageToAllExcept(playerTurn, "Le joueur " + (playerTurn + 1) + " surcoinche");
        }
    }

    private boolean isInSameTeam(int playerA, int playerB) {
        if ((playerA == 0 && playerB == 2) ||
                (playerA == 1 && playerB == 3) ||
                (playerA == 2 && playerB == 0) ||
                (playerA == 3 && playerB == 1)) {
            return (true);
        }
        return (false);
    }

    private void sendMessageTo(int nbr, String message) {
        players.elementAt(nbr).sendCommand(message);
    }

    private void sendMessageToAllExcept(int nbr, String message) {
        for (int i = 0 ; i < 4 ; ++i) {
            if (i != nbr) {
                players.elementAt(i).sendCommand(message);
            }
        }
    }
}
