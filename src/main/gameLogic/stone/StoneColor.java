package main.gameLogic.stone;

public enum StoneColor {
    BLACK(0, "BLACK", 1),
    WHITE(1, "WHITE", -1);
    private int value;
    private String name;
    private int sign;

    private StoneColor(int value, String name, int sign) {
        this.value = value;
        this.name = name;
        this.sign = sign;
    }

    public static StoneColor getColor(int x) {
        switch (x) {
            case 1:
                return StoneColor.BLACK;
            case 2:
                return StoneColor.WHITE;
        }

        return null;
    }


    public int getValue() {
        return value;
    }

    public int getSign() {
        return sign;
    }

    @Override
    public String toString() {
        return name;
    }
}