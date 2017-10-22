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

    public void allAtout(Vector<Card> cards, int first) {
        int team_a = 0;
        int team_b = 0;

        team_a = cards.get(0).getValue(2) + cards.get(2).getValue(2);
        team_b = cards.get(1).getValue(2) + cards.get(3).getValue(2);
        winnerId = 0;
        winnerId = winnerAll(cards.get(winnerId), cards.get(1), first, winnerId, 1);
        winnerId = winnerAll(cards.get(winnerId), cards.get(2), first, winnerId, 2);
        winnerId = winnerAll(cards.get(winnerId), cards.get(3), first, winnerId, 3);
        points = team_a + team_b;
    }

    public void classic(Vector<Card> cards, int first) {

        int team_a = 0;
        int team_b = 0;

        team_a = cards.get(0).getValue(3) + cards.get(2).getValue(3);
        team_b = cards.get(1).getValue(3) + cards.get(3).getValue(3);
        winnerId = 0;
        winnerId = winnerClassic(cards.get(winnerId), cards.get(1), first, winnerId, 1);
        winnerId = winnerClassic(cards.get(winnerId), cards.get(2), first, winnerId, 2);
        winnerId = winnerClassic(cards.get(winnerId), cards.get(3), first, winnerId, 3);
        points = team_a + team_b;
    }

    public void withAtout(Vector<Card> cards, int color, int first) {
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

    public void withoutAtout(Vector<Card> cards, int atout, int first) {
        if (atout == 1) {
            allAtout(cards, first);
        } else {
            classic(cards, first);
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

    public int  winnerClassic(Card a, Card b, int color, int ida, int idb) {
        if (a.isColor(color) && !b.isColor(color)) {
            return (ida);
        } else if (b.isColor(color) && !a.isColor(color)) {
            return (idb);
        } else if (a.isColor(color) && b.isColor(color)) {
            return (a.getValue(3) < b.getValue(3) ? idb : ida);
        } else {
            return (a.getValue(3) < b.getValue(3) ? idb : ida);
        }
    }

    public int  winnerAll(Card a, Card b, int color, int ida, int idb) {
        if (a.isColor(color) && !b.isColor(color)) {
            return (ida);
        } else if (b.isColor(color) && !a.isColor(color)) {
            return (idb);
        } else if (a.isColor(color) && b.isColor(color)) {
            return (a.getValue(2) < b.getValue(2) ? idb : ida);
        } else {
            return (a.getValue(2) < b.getValue(2) ? idb : ida);
        }
    }

    public int  getValue(Card card, int color) {
        if (card.isColor(color)) {
            return (card.getValue(0));
        } else {
            return (card.getValue(1));
        }
    }

    public void calculate(Vector<Card> cards, int atout, int color, int first) {
        if (atout == 0) {
            withAtout(cards, color, first);
        } else {
            withoutAtout(cards, atout, first);
        }
    }
}
