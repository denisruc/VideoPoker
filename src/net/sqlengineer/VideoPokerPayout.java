package net.sqlengineer;

/**
 * Created by druckebusch on 1/6/17.
 */
public class VideoPokerPayout implements Payout {

    public static final int MAX_CREDITS = 5;

    public int pay(Hand hand, int credits) {

        if (credits > MAX_CREDITS) {
            throw new IllegalArgumentException("Maximum number of credits is " + MAX_CREDITS);
        }

        boolean isFlush = false;
        boolean isStraight = false;

        if (hand.isRoyalFlush()) {
            if (MAX_CREDITS == credits) {
                return credits * 800;
            } else {
                return credits * 250;
            }
        }

        if (hand.isFlush()) {
            isFlush = true;
        }

        if (hand.isStraight()) {
            isStraight = true;
        }

        if (isFlush && isStraight) {
            // straight flush
            return credits * 50;
        }

        if (hand.isFourOfAKind()) {
            return credits * 25;
        }

        if (hand.isFullHouse()) {
            return credits * 9;
        }

        if (isFlush) {
            return credits * 6;
        }

        if (isStraight) {
            return credits * 4;
        }

        if (hand.isThreeOfAKind()) {
            return credits * 3;
        }

        if (hand.isTwoPairs()) {
            return credits * 2;
        }

        if (hand.isTwoJacksOrHigher()) {
            return credits;
        }

        // That hand has nothing
        return 0;

    }

    @Override
    public int getMaxCredits() {
        return MAX_CREDITS;
    }

}
