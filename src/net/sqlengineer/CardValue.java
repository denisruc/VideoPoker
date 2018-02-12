package net.sqlengineer;

/**
 * Created by druckebusch on 1/5/17.
 */
public enum CardValue {
    Ace ("1"),
    Two ("2"),
    Three ("3"),
    Four ("4"),
    Five ("5"),
    Six ("6"),
    Seven ("7"),
    Eight ("8"),
    Nine ("9"),
    Ten ("10"),
    Jack ("J"),
    Queen ("Q"),
    King ("K");

    private final String mValue;

    CardValue(String value) {
        mValue = value;
    }

    CardValue(int value) {
        mValue = Integer.toString(value);
    }

    @Override
    public String toString() {
        return mValue;
    }
}
