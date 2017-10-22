package org.epitech.jcoinche;

public class Card {
    private int atout;
    private int natout;
    private int tout_atout;
    private int sans_atout;
    private int color;
    private String name;

    Card(int vala, int valb, int valc, int vald, int col, String cardName) {
        atout = vala;
        natout = valb;
        tout_atout = valc;
        sans_atout = vald;
        color = col;
        name = cardName;
    }

    public int getValue(int at) {
        if (at == 0)
            return (atout);
        else if (at == 1)
            return (natout);
        else if (at == 2)
            return (tout_atout);
        else
            return (sans_atout);
    }

    public boolean isColor(int col) {
        return (color == col);
    }

    public int getColor() {
        return (color);
    }

    public String getName() {
        return (name);
    }
}
