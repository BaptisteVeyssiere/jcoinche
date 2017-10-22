package org.epitech.jcoinche;

import com.sun.media.jfxmedia.events.PlayerTimeListener;

import java.util.Arrays;
import java.util.Vector;

public class GameManager {
    private Calculator calc = new Calculator();
    private CalculationArray calcArray = new CalculationArray();
    private int  currentBid = 0;
    private int  currentColor = 0;
    private int  currentAtout = 0;
    private int currentBidOwner = 0;
    private int turnNbr;
    private int mainColor;
    private Card winningCard;
    private int skipInARow = 0;
    private Deck    deck = new Deck();
    private Card[] pli = new Card[4];
    private boolean first = true;
    private boolean round = false;
    private boolean coinche = false;
    private boolean surcoinche = false;
    private boolean newTurn = true;
    private boolean bid = false;
    private boolean turn = false;
    private boolean waitingResponse = false;
    private int     playerTurn = 0;
    private int teamA = 0;
    private int teamB = 0;
    private int pliA = 0;
    private int pliB = 0;
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
        waitingResponse = false;
        playerTurn = 0;
        currentBid = 0;
        bid = false;
        pliA = 0;
        pliB = 0;
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
            if (!turn) {
                waitingResponse = false;
                turn = true;
                sendMessageToAllExcept(playerTurn, "Les enchères sont terminées. Le joueur " + (playerTurn + 1) + " joue.");
                sendMessageTo(playerTurn, "Les enchères sont terminées, vous avez la main.");
            }
            if (!round) {
                round = true;
                turnNbr = 0;
                waitingResponse = false;
                for (Player player : players) {
                    player.sendCardList();
                }
                first = true;
                for (int i = 0 ; i < 4 ; ++i)
                    pli[i] = null;
            }
            if (!waitingResponse) {
                turnNbr++;
                if (turnNbr == 5) {
                    calc.calculate(new Vector<Card>(Arrays.asList(pli)), currentColor, currentAtout, mainColor);
                    playerTurn = calc.getWinnerId();
                    sendMessageToAll("Le joueur " + (playerTurn + 1) + " remporte le pli.");
                    if (playerTurn == 0 || playerTurn == 2)
                        pliA += calc.getPoints();
                    else
                        pliB += calc.getPoints();
                    sendMessageToAll("Pli A: " + pliA);
                    sendMessageToAll("Pli B: " + pliB);
                    if (players.elementAt(0).getCardNbr() == 0) {
                        if (playerTurn == 0 || playerTurn == 2)
                            pliA += 10;
                        else
                            pliB += 10;
                        calcArray.setBid(currentBid);
                        calcArray.setBidId(currentBidOwner);
                        if (surcoinche) {
                            calcArray.setCoinche(4);
                        } else if (coinche) {
                            calcArray.setCoinche(2);
                        } else {
                            calcArray.setCoinche(1);
                        }
                        calcArray.setTeam(0, pliA, 0, 0);
                        calcArray.setTeam(1, pliB, 0, 0);
                        calcArray.calculate();
                        teamA += calcArray.getPts(0);
                        sendMessageToAll("L'équipe A gagne " + calcArray.getPts(0) + " points.");
                        sendMessageToAll("L'équipe B gagne " + calcArray.getPts(1) + " points.");
                        teamB += calcArray.getPts(1);
                        if (teamA > 700 && teamA > teamB) {
                            sendMessageToAll("L'équipe A remporte la partie.");
                            throw new RuntimeException();
                        } else if (teamB > 700 && teamB > teamA) {
                            sendMessageToAll("L'équipe B remporte la partie.");
                            throw new RuntimeException();
                        }
                        newTurn = true;
                        bid = false;
                        currentBid = 0;
                    }
                    round = false;
                }
                sendMessageTo(playerTurn, "A vous de jouer");
                sendMessageToAllExcept(playerTurn, "Le joueur " + (playerTurn + 1) + " choisit une carte");
                waitingResponse = true;
            } else {
                if (players.elementAt(playerTurn).getCommandNbr() >= 1)
                    handleRoundMessage(players.elementAt(playerTurn).getCommand());
            }
        }

    }

    private void handleRoundMessage(String message) {
        Card tmp;
        int tmpColor;

        try {
            tmp = players.elementAt(playerTurn).getCardByName(message);
        } catch (RuntimeException e) {
            return ;
        }
        if (currentColor == 0 && currentAtout == tmp.getColor())
            tmpColor = 0;
        else if (currentColor == 0 && currentAtout != tmp.getColor())
            tmpColor = 1;
        else if (currentColor == 1)
            tmpColor = 2;
        else
            tmpColor = 3;

        if (first) {
            first = false;
            pli[playerTurn] = tmp;
            waitingResponse = false;
            mainColor = tmp.getColor();
            winningCard = tmp;
            players.elementAt(playerTurn).removeCardByName(message);
            sendMessageToAllExcept(playerTurn, "Le joueur " + (playerTurn + 1) + " a joué: " + tmp.getName());
            playerTurn = (playerTurn + 1) % 4;
            return ;
        } else {
            if (tmp.getColor() == mainColor && tmp.getValue(tmpColor) > winningCard.getValue(tmpColor)) {
                players.elementAt(playerTurn).removeCardByName(message);
                sendMessageToAllExcept(playerTurn, "Le joueur " + (playerTurn + 1) + " a joué: " + tmp.getName());
                winningCard = tmp;
                pli[playerTurn] = tmp;
                playerTurn = (playerTurn + 1) % 4;
                waitingResponse = false;
            } else if (currentColor != 0 && players.elementAt(playerTurn).hasBetterCardSameColor(winningCard.getValue(tmpColor), mainColor, tmpColor)) {
                sendMessageTo(playerTurn, "Vous ne pouvez pas jouer cette carte.");
                return ;
            } else if (currentColor == 0) {
                if (winningCard.getColor() == currentAtout && players.elementAt(playerTurn).hasBetterCardSameColor(winningCard.getValue(tmpColor), mainColor, tmpColor)) {
                    sendMessageTo(playerTurn, "Vous ne pouvez pas jouer cette carte.");
                    return ;
                } else if (winningCard.getColor() != currentAtout && players.elementAt(playerTurn).hasBetterCardSameColor(winningCard.getValue(1), mainColor, 1)) {
                    players.elementAt(playerTurn).sendCommand("Vous ne pouvez pas jouer cette carte.");
                    return ;
                } else if (winningCard.getColor() != currentAtout && !players.elementAt(playerTurn).hasBetterCardSameColor(winningCard.getValue(tmpColor), mainColor, 1)) {
                    if (tmp.getColor() == currentAtout) {
                        winningCard = tmp;
                        pli[playerTurn] = tmp;
                        mainColor = tmp.getColor();
                        waitingResponse = false;
                        players.elementAt(playerTurn).removeCardByName(message);
                        sendMessageToAllExcept(playerTurn, "Le joueur " + (playerTurn + 1) + " a joué: " + tmp.getName());
                        playerTurn = (playerTurn + 1) % 4;
                    }
                    else if (tmp.getColor() != currentAtout && players.elementAt(playerTurn).hasBetterCardSameColor(-1, currentAtout, tmpColor)) {
                        players.elementAt(playerTurn).sendCommand("Vous ne pouvez pas jouer cette carte.");
                        return ;
                    }
                }
            } else {
                pli[playerTurn] = tmp;
                waitingResponse = false;
                players.elementAt(playerTurn).removeCardByName(message);
                sendMessageToAllExcept(playerTurn, "Le joueur " + (playerTurn + 1) + " a joué: " + tmp.getName());
                playerTurn = (playerTurn + 1) % 4;
            }
        }

    }

    private void sendMessageToAll(String message) {
        for (Player player : players) {
            player.sendCommand(message);
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
