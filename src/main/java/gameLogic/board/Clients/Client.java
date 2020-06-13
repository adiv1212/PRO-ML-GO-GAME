package gameLogic.board.Clients;

import gameLogic.stone.StoneColor;

public abstract class Client {
    private StoneColor color;

    public abstract void play();

    public Client() {
        this.color = null;
    }

    public StoneColor getColor() {
        return color;
    }

    public void setColor(StoneColor color) {
        this.color = color;
    }
}
