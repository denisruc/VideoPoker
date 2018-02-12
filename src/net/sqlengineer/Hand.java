package net.sqlengineer;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.lang.IllegalArgumentException;

/**
 * This class implements the player's hand
 *
 */
public class Hand {

    Card[] mCards;
    boolean isSorted = false;
    Map<CardValue, Integer> counts = null;

    /**
     * Builds a hand by dealing cards out of a deck
     *
     * @param num number of cards to deal
     * @param deck deck to deal cards from
     */
    public Hand(int num, Deck deck) {
        mCards = new Card[num];
        for (int i = 0; i < num; i++) {
            mCards[i] = deck.dealCard();
        }
    }

    /**
     * Creates the hand object out of an array of cards.
     *
     * @param cards
     */
    protected Hand(Card[] cards) {
        mCards = cards;
        isSorted = false;
        counts = null;
    }

    public int size() {
        if (null == mCards) {
            return 0;
        }
        return mCards.length;
    }

    public Card get(int index) {
        if (null == mCards) {
            return null;
        }
        if (index < 0 || index >= mCards.length) {
            throw new IndexOutOfBoundsException();
        }
        return mCards[index];
    }

    /**
     * Builds a hand by removing specific cards from a deck
     *
     * @param cards cards to put into the hand
     * @param deck deck to deal cards from
     */
    public Hand(Card[] cards, Deck deck) {
        mCards = cards;
        if (null == cards) {
            return;
        }
        for (int i = 0; i < cards.length; i++) {
            deck.removeCard(cards[i].getSuit(), cards[i].getValue());
        }
    }



    public boolean contains(Card card) {
        if (null == card) {
            return false;
        }
        for (int i = 0; i < mCards.length; i++) {
            if (card.equals(mCards[i])) {
                return true;
            }
        }
        return false;
    }

    public boolean contains(Card[] cards) {
        // Empty or null cards array. We return true.
        if (null == cards || 0 == cards.length) {
            return true;
        }
        for (int i = 0; i < cards.length; i++) {
            if (!this.contains(cards[i])) {
                return false;
            }
        }
        return true;
    }

    public void keepAndDeal(Card[] cardsToKeep, Deck deck) {

        // check that all the cards passed in the array are contained in the hand
        if (!contains(cardsToKeep)) {
            throw new IllegalArgumentException("Cards to keep are not present in the current hand.");
        }

        if (cardsToKeep != null && mCards.length == cardsToKeep.length) {
            // Keep everything
            return;
        }
        if (cardsToKeep.length > mCards.length) {
            throw new IllegalArgumentException("Illegal number of cards to hold");
        }

        isSorted = false;
        counts = null;
        for (int i = 0; i < mCards.length; i++) {
            Card currentCard = mCards[i];
            boolean isKept = false;
            for (int j = 0; j < cardsToKeep.length; j++) {
                if (currentCard.equals(cardsToKeep[j])) {
                    isKept = true;
                    break;
                }
            }
            if (!isKept) {
                mCards[i] = deck.dealCard();
            }
        }
    }

    protected void computeCounts() {

        if (counts != null) {
            return;
        }

        HashMap<CardValue, Integer> ret = new HashMap<CardValue, Integer>();
        for (int i = 0; i < mCards.length; i++) {
            CardValue value = mCards[i].getValue();
            int count = ret.getOrDefault(value, 0);
            ret.put(value, new Integer(count + 1));
        }
        counts = ret;
    }



    public void sort() {

        if (isSorted || null == mCards) {
            return;
        }

        Arrays.sort(mCards, new Comparator<Card>() {
            @Override
            public int compare(Card c1, Card c2) {
                return c1.getValue().ordinal() - c2.getValue().ordinal();
            }
        });

        isSorted = true;
    }

    public boolean isFlush() {
        if (null == mCards || 0 == mCards.length) {
            return false;
        }
        CardSuit suit = mCards[0].getSuit();
        for (int i = 1; i < mCards.length; i++) {
            if (suit != mCards[i].getSuit()) {
                return false;
            }
        }
        return true;
    }

    public boolean isRoyalFlush() {

        if (null == mCards || mCards.length != 5) {
            return false;
        }

        if (!isFlush()) {
            return false;
        }

        sort();

        if (mCards[0].getValue() != CardValue.valueOf("Ace")) {
            return false;
        }

        if (mCards[1].getValue() != CardValue.valueOf("Ten")) {
            return false;
        }

        if (mCards[2].getValue() != CardValue.valueOf("Jack")) {
            return false;
        }

        if (mCards[3].getValue() != CardValue.valueOf("Queen")) {
            return false;
        }

        if (mCards[4].getValue() != CardValue.valueOf("King")) {
            return false;
        }

        return true;

    }

    public boolean isStraight() {
        sort();
        boolean ret = true;
        int ordinal = mCards[0].getValue().ordinal();
        for (int i = 1; i < mCards.length; i++) {
            ordinal++;
            if (mCards[i].getValue().ordinal() != ordinal) {
                ret = false;
                break;
            }
        }

        if (ret) {
            return true;
        }

        // It doesn't look like we have a straight. However we must check the alternative case with Ace and King
        if (mCards[0].getValue() == CardValue.valueOf("Ace") && mCards[mCards.length - 1].getValue() == CardValue.valueOf("King")) {
            // we know there are at least two cards. If there was only 1 we would have returned true already
            ordinal = mCards[1].getValue().ordinal();
            for (int i = 2; i < mCards.length; i++) {
                ordinal++;
                if (mCards[i].getValue().ordinal() != ordinal) {
                    return false;
                }
            }
            return true;
        } else {
            // No Ace and King. This case does not apply.
            return false;
        }
    }

    public boolean isFullHouse() {
        computeCounts();
        if (mCards == null || mCards.length != 5 || counts.size() != 2) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isFourOfAKind() {
        if (computeMaxCount() == 4) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isThreeOfAKind() {
        if (computeMaxCount() == 3) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isTwoPairs() {
        computeCounts();
        int pairCount = 0;
        for (Integer count : counts.values()) {
            if (2 == count) {
                pairCount++;
            }
        }
        if (pairCount >= 2) {
            return true;
        }
        return false;
    }


    public boolean isTwoJacksOrHigher() {
        computeCounts();
        if (2 == counts.getOrDefault(CardValue.Ace, 0)) {
            return true;
        }
        if (2 == counts.getOrDefault(CardValue.King, 0)) {
            return true;
        }
        if (2 == counts.getOrDefault(CardValue.Queen, 0)) {
            return true;
        }
        if (2 == counts.getOrDefault(CardValue.Jack, 0)) {
            return true;
        }
        return false;
    }

    protected int computeMaxCount() {
        computeCounts();
        int max = 0;
        for (CardValue key : counts.keySet()) {
            int count = counts.get(key);
            if (count > max) {
                max = count;
            }
        }
        return max;
    }


    public String toString() {
        String ret = "";
        for (int i = 0; i < mCards.length; i++) {
            ret += mCards[i].toString() + " ";
        }
        return ret;
    }

    @Override
    public Hand clone() {
        return new Hand(mCards.clone());
    }

}
