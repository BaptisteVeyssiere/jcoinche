package org.epitech.jcoinche;

public class CalculationArray {

    public class Team {
        public int  pts = 0;
        public int  plis = 0;
        public int  belote = 0;
        public int  bonus = 0;
    }

    private Team    team_a = new Team();
    private Team    team_b = new Team();
    private int     bid;
    private int     bidId;
    private int     coinche;

    CalculationArray() {
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public void setBidId(int bidId) {
        this.bidId = bidId;
    }

    public void setCoinche(int coinche) {
        this.coinche = coinche;
    }

    public void setTeam(int team, int plis, int belote, int bonus) {
        if (team == 0) {
            team_a.plis = plis;
            team_a.belote = belote;
            team_a.bonus = bonus;
        } else {
            team_b.plis = plis;
            team_b.belote = belote;
            team_b.bonus = bonus;
        }
    }

    public int getPts(int team) {
        if (team == 0) {
            return (team_a.pts);
        } else {
            return (team_b.pts);
        }
    }

    public void calculateAttack(Team a, Team b) {
        if (a.plis >= 82 && bid <= (a.plis + a.belote)) {
            a.pts = (a.plis + a.belote + bid) * coinche + a.bonus;
        } else {
            a.pts = a.belote;
        }
    }

    public void calculateDefense(Team a, Team b) {
        if (b.plis >= 82 && bid <= (b.plis + b.belote)) {
            if (coinche != 1) {
                a.pts = a.belote;
            } else {
                a.pts = a.belote + a.plis;
            }
        } else {
            a.pts = (162 + bid + a.belote) * coinche;
        }
    }

    public void calculate() {
        if (bidId == 0 || bidId == 2) {
            calculateAttack(team_a, team_b);
            calculateDefense(team_b, team_a);
        } else {
            calculateAttack(team_b, team_a);
            calculateDefense(team_a, team_b);
        }
    }
}
