enum StoneColor {
	BLACK(0, "BLACK"), WHITE(1, "WHITE");
	private int value;
	private String string;
	
	private StoneColor(int value, String string) {
		this.value = value;
		this.string = string;
	}
	
	public int getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return string;
	}
}