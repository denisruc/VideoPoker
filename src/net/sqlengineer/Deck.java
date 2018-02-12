package net.sqlengineer;

import java.util.Random;

/**
 * Created by druckebusch on 1/5/17.
 */
public class Deck {

    private Card[] cards;
    private Random rand;


    /**
     * Build a deck from multiple 52-card decks
     *
     * @param n Number of 52-card decks to use
     */
    public Deck(int n) {
        rand = new Random(System.currentTimeMillis());
        cards = new Card[52 * n];

        int idx = 0;
        for (int i = 0; i < n; i++) {
            for (CardSuit suit : CardSuit.values()) {
                for (CardValue value : CardValue.values()) {
                    cards[idx] = new Card(suit, value);
                    idx++;
                }
            }
        }
    }

    /**
     * Build one regular 52-card deck
     *
     */
    public Deck() {
        this(1);
    }

    protected Deck(Card[] cards) {
        this.cards = cards;
        this.rand = new Random(System.currentTimeMillis());
    }

    public int size() {
        if (null == cards) {
            return 0;
        }
        return cards.length;
    }

    public void shuffle(int n) {
        synchronized (cards) {
            for (int i = 0; i < n; i++) {
                int index1 = rand.nextInt(cards.length);
                int index2 = rand.nextInt(cards.length);
                if (index1 != index2) {
                    Card temp = cards[index1];
                    cards[index1] = cards[index2];
                    cards[index2] = temp;
                }
            }
        }
    }

    public Card dealCard() {
        Card ret = null;
        synchronized(cards) {
            if (0 == cards.length) {
                throw new IllegalStateException("Deck is empty. Unable to deal a card");
            }
            ret = cards[0];
            Card[] newArray = new Card[cards.length - 1];
            for (int i = 1; i < cards.length; i++) {
                newArray[i - 1] = cards[i];
            }
            cards = newArray;
        }
        return ret;
    }

    public void removeCard(CardSuit suit, CardValue value) {

        synchronized(cards) {
            Card[] newArray = new Card[cards.length - 1];
            int destIndex = 0;
            for (int srcIndex = 0; srcIndex < cards.length; srcIndex++) {
                Card card = cards[srcIndex];
                if (card.getSuit() != suit || card.getValue() != value) {
                    if (destIndex >= newArray.length) {
                        throw new IllegalArgumentException("The deck does not contain the card. Therefore it cannot be removed");
                    } else {
                        newArray[destIndex] = cards[srcIndex];
                        destIndex++;
                    }
                }
            }
            this.cards = newArray;
        }
    }

    public void print() {
        System.out.println();
        for (Card card : cards) {
            System.out.print(card.toString() + " ");
        }
        System.out.println();
    }

    public Deck clone() {
        Card[] newCards = cards.clone();
        return new Deck(newCards);
    }

}
