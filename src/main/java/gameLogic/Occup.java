package gameLogic;

import gameLogic.stone.StoneColor;

public enum Occup {
    BLACK, WHITE, UNDECIDED;

    public static Occup getOccup(StoneColor color) {
        switch (color) {
            case BLACK:
                return BLACK;
            case WHITE:
                return WHITE;
            default:
                return UNDECIDED;
        }
    }
}