package net.sqlengineer;

public class Card {

    private CardSuit m_suit;
    private CardValue m_value;

    public Card(String suit, String value) {
        this.m_suit = CardSuit.valueOf(suit);
        this.m_value = CardValue.valueOf(value);
    }

    public Card(CardSuit suit, CardValue value) {
        this.m_suit = suit;
        this.m_value = value;
    }

    public CardSuit getSuit() {
        return m_suit;
    }

    public CardValue getValue() {
        return m_value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Card) {
            return (((Card) obj).getSuit() == m_suit && ((Card) obj).getValue() == m_value);
        } else {
            return super.equals(obj);
        }
    }

    @Override
    public String toString() {
        return  m_value.toString() + m_suit.toString();
    }
}
