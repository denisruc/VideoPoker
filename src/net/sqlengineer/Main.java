package net.sqlengineer;

public class Main {

    public static void main(String[] args) {

        Card[] cards = new Card[5];
        cards[0] = new Card("Spades", "Eight");
        cards[1] = new Card("Spades", "Nine");
        cards[2] = new Card("Spades", "Ten");
        cards[3] = new Card("Spades", "Queen");
        cards[4] = new Card("Clubs", "King");


        Deck deck = new Deck();
        Hand hand = new Hand(cards, deck);

        Simulation sim = new Simulation(deck,  hand, new VideoPokerPayout().getMaxCredits(), 1000000, 360);

        try {
            sim.runSim();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /*

        Card[] keptCardsScenario1 = new Card[1];
        keptCardsScenario1[0] = cards[0];

        Card[] keptCardsScenario2 = new Card[2];
        keptCardsScenario2[0] = cards[4];
        keptCardsScenario2[1] = cards[0];


        Payout payout = new VideoPokerPayout();
        final int iterations = 10000000;


        int money = 0;
        for (int i = 0; i < iterations; i++) {
            Deck deck = new Deck();
            Hand hand = new Hand(cards, deck);
            deck.shuffle(200);
            hand.keepAndDeal(keptCardsScenario1, deck);
            money += payout.pay(hand, payout.getMaxCredits());
        }
        float value = ((float)money) / ((float)iterations);
        System.out.println("Scenario 1 value = " + value);

        money = 0;
        for (int i = 0; i < iterations; i++) {
            Deck deck = new Deck();
            deck.shuffle(200);
            Hand hand = new Hand(cards, deck);
            hand.keepAndDeal(keptCardsScenario2, deck);
            money += payout.pay(hand, payout.getMaxCredits());
        }
        value = ((float)money) / ((float)iterations);
        System.out.println("Scenario 2 value = " + value);

        */

    }
}
