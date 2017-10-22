package org.epitech.jcoinche;

import java.util.Random;
import java.util.Vector;

public class Deck {
    private Vector<Card> card_list = new Vector<Card>();

    public void    newDeck() {
        card_list.clear();
        card_list.add(new Card(0, 0, 0, 0, 0, "7 de Trefle"));
        card_list.add(new Card(0, 0, 0, 0, 0, "8 de Trefle"));
        card_list.add(new Card(14, 0, 9, 0, 0, "9 de Trefle"));
        card_list.add(new Card(20, 2, 14, 2, 0, "Valet de Trefle"));
        card_list.add(new Card(3, 3, 2, 3, 0, "Dame de Trefle"));
        card_list.add(new Card(4, 4, 3, 4, 0, "Roi de Trefle"));
        card_list.add(new Card(10, 10, 5, 10, 0, "10 de Trefle"));
        card_list.add(new Card(11, 11, 7, 19, 0, "As de Trefle"));
        card_list.add(new Card(0, 0, 0, 0, 1, "7 de Pique"));
        card_list.add(new Card(0, 0, 0, 0, 1, "8 de Pique"));
        card_list.add(new Card(14, 0, 9, 0, 1, "9 de Pique"));
        card_list.add(new Card(20, 2, 14, 2, 1, "Valet de Pique"));
        card_list.add(new Card(3, 3, 2, 3, 1, "Dame de Pique"));
        card_list.add(new Card(4, 4, 3, 4, 1, "Roi de Pique"));
        card_list.add(new Card(10, 10, 5, 10, 1, "10 de Pique"));
        card_list.add(new Card(11, 11, 7, 19, 1, "As de Pique"));
        card_list.add(new Card(0, 0, 0, 0, 2, "7 de Carreau"));
        card_list.add(new Card(0, 0, 0, 0, 2, "8 de Carreau"));
        card_list.add(new Card(14, 0, 9, 0, 2, "9 de Carreau"));
        card_list.add(new Card(20, 2, 14, 2, 2, "Valet de Carreau"));
        card_list.add(new Card(3, 3, 2, 3, 2, "Dame de Carreau"));
        card_list.add(new Card(4, 4, 3, 4, 2, "Roi de Carreau"));
        card_list.add(new Card(10, 10, 5, 10, 2, "10 de Carreau"));
        card_list.add(new Card(11, 11, 7, 19, 2, "As de Carreau"));
        card_list.add(new Card(0, 0, 0, 0, 3, "7 de Coeur"));
        card_list.add(new Card(0, 0, 0, 0, 3, "8 de Coeur"));
        card_list.add(new Card(14, 0, 9, 0, 3, "9 de Coeur"));
        card_list.add(new Card(20, 2, 14, 2, 3, "Valet de Coeur"));
        card_list.add(new Card(3, 3, 2, 3, 3, "Dame de Coeur"));
        card_list.add(new Card(4, 4, 3, 4, 3, "Roi de Coeur"));
        card_list.add(new Card(10, 10, 5, 10, 3, "10 de Coeur"));
        card_list.add(new Card(11, 11, 7, 19, 3, "As de Coeur"));
    }

    public Card pickACard() {
        Random  rand = new Random();
        int     nbr;

        nbr = rand.nextInt(card_list.size());
        return (card_list.remove(nbr));
    }
}
