package net.sqlengineer;

/**
 * Created by druckebusch on 1/6/17.
 */
public interface Payout {

    public int pay(Hand hand, int credit);

    public int getMaxCredits();
}
