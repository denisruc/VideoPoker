package net.sqlengineer;

/**
 * Created by druckebusch on 1/5/17.
 */
public enum CardSuit {
    Spades ("♠"),
    Hearts ("♥"),
    Diamonds ("♦"),
    Clubs ("♣");

    private final String mValue;

    CardSuit(String value) {
        mValue = value;
    }

    @Override
    public String toString() {
        return mValue;
    }
}