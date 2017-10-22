package org.epitech.jcoinche;

import java.util.Vector;

public class Calculator {

    private int winnerId = 0;
    private int points = 0;

    Calculator() {
    }

    public int  getWinnerId() {
        return (winnerId);
    }

    public int  getPoints() {
        return (points);
    }

    public void allAtout(Vector<Card> cards) {
        int team_a = 0;
        int team_b = 0;

        team_a = cards.get(0).getValue(2) + cards.get(2).getValue(2);
        team_b = cards.get(1).getValue(2) + cards.get(3).getValue(2);
        if (team_a < team_b) {
            winnerId = (cards.get(1).getValue(2) < cards.get(3).getValue(2)) ? 3 : 1;
        } else {
            winnerId = (cards.get(0).getValue(2) < cards.get(2).getValue(2)) ? 2 : 0;
        }
        points = team_a + team_b;
    }

    public void classic(Vector<Card> cards) {

        int team_a = 0;
        int team_b = 0;

        team_a = cards.get(0).getValue(3) + cards.get(2).getValue(3);
        team_b = cards.get(1).getValue(3) + cards.get(3).getValue(3);
        if (team_a < team_b) {
            winnerId = (cards.get(1).getValue(3) < cards.get(3).getValue(3)) ? 3 : 1;
        } else {
            winnerId = (cards.get(0).getValue(3) < cards.get(2).getValue(3)) ? 2 : 0;
        }
        points = team_a + team_b;
    }

    public void withAtout(Vector<Card> cards, int color) {
        int team_a = 0;
        int team_b = 0;

        team_a = getValue(cards.get(0), color) + getValue(cards.get(2), color);
        team_b = getValue(cards.get(1), color) + getValue(cards.get(3), color);
        points = team_a + team_b;
        winnerId = 0;
        winnerId = winner(cards.get(winnerId), cards.get(1), color, winnerId, 1);
        winnerId = winner(cards.get(winnerId), cards.get(2), color, winnerId, 2);
        winnerId = winner(cards.get(winnerId), cards.get(3), color, winnerId, 3);
    }

    public void withoutAtout(Vector<Card> cards, int atout) {
        if (atout == 1) {
            allAtout(cards);
        } else {
            classic(cards);
        }
    }

    public int  winner(Card a, Card b, int color, int ida, int idb) {
        if (a.isColor(color) && !b.isColor(color)) {
            return (ida);
        } else if (b.isColor(color) && !a.isColor(color)) {
            return (idb);
        } else if (a.isColor(color) && b.isColor(color)) {
            return (a.getValue(0) < b.getValue(0) ? idb : ida);
        } else {
            return (a.getValue(1) < b.getValue(1) ? idb : ida);
        }
    }

    public int  getValue(Card card, int color) {
        if (card.isColor(color)) {
            return (card.getValue(0));
        } else {
            return (card.getValue(1));
        }
    }

    public void calculate(Vector<Card> cards, int atout, int color) {
        if (atout == 0) {
            withAtout(cards, color);
        } else {
            withoutAtout(cards, atout);
        }
    }
}
