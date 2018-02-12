package net.sqlengineer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *
 * Class to run a simulation based on a deck and a hand.
 *
 */
public class Simulation {

    private Hand hand = null;
    private Deck deck = null;
    private int credits = 0;
    private int nLoops = 0;
    private int timeoutInSeconds = 240;


    public Simulation(Deck deck, Hand hand, int betCredits, int nSimulations) {
        this.hand = hand;
        this.deck = deck;
        this.credits = betCredits;
        this.nLoops = nSimulations;
    }


    public Simulation(Deck deck, Hand hand, int betCredits, int nSimulations, int timeoutInSeconds) {
        this.hand = hand;
        this.deck = deck;
        this.credits = betCredits;
        this.nLoops = nSimulations;
        this.timeoutInSeconds = timeoutInSeconds;
    }

    public void runSim() throws InterruptedException {
        List<Card[]> combinations = getCombinations();
        ExecutorService executorService = Executors.newFixedThreadPool(combinations.size());
        CountDownLatch latch = new CountDownLatch(combinations.size());
        SimRunner[] runners = new SimRunner[combinations.size()];

        for (int i = 0; i < runners.length; i++) {
            runners[i] = new SimRunner(deck, hand, combinations.get(i), nLoops, latch, credits, new VideoPokerPayout());
            executorService.execute(runners[i]);
        }

        if (!latch.await(timeoutInSeconds, TimeUnit.SECONDS)) {
            System.out.println("SIMULATION DID NOT COMPLETE IN " + timeoutInSeconds + " seconds.");
        }

        executorService.shutdownNow();

        double max = 0;
        int maxIndex = -1;
        for (int i = 0; i < runners.length; i++) {
            if (runners[i].getResult() > max) {
                maxIndex = i;
                max = runners[i].getResult();
            }
        }

        System.out.print("BEST RESULTS BY HOLDING THESE CARDS ");
        Card[] keptCards = runners[maxIndex].getHeldCards();
        for (int i = 0; i < keptCards.length; i++) {
            System.out.print(keptCards[i] == null ? "NULL " : keptCards[i].toString() + " ");
        }

        System.out.println();
        System.out.println("AVERAGE PAYOUT WON BY KEEPING THOSE CARDS IS " + runners[maxIndex].getResult() + " credits");

    }


    public List<Card[]> getCombinations() {
        List<Card[]> ret = new ArrayList<Card[]>(32);
        ret.add(new Card[0]);
        populateCombinations(ret, 0);
        return ret;
    }

    private void populateCombinations(List<Card[]> ret, int index) {
        if (index >= hand.size()) {
            return;
        }

        int count = ret.size();
        for (int i = 0; i < count; i++) {
            Card[] cards = ret.get(i);
            Card[] newEntry = new Card[cards.length + 1];
            for (int j = 0; j < cards.length; j++) {
                newEntry[j] = cards[j];
            }
            newEntry[cards.length] = hand.get(index);
            ret.add(newEntry);
        }
        populateCombinations(ret, ++index);
    }


    private class SimRunner implements Runnable {

        private Deck deck = null;
        private Hand hand = null;
        private Card[] heldCards = null;
        private int nIterations = 0;
        private CountDownLatch latch = null;
        private int betCredits = 0;
        private Payout payout = null;

        private boolean isLogged = false;

        double result = 0;

        public SimRunner(Deck deck, Hand hand, Card[] heldCards, int nIterations, CountDownLatch latch, int betCredits, Payout payout) {
            this.deck = deck;
            this.hand = hand;
            this.heldCards = heldCards;
            this.nIterations = nIterations;
            this.latch = latch;
            this.betCredits = betCredits;
            this.payout = payout;
        }

        public Card[] getHeldCards() {
            return heldCards;
        }

        public double getResult() {
            return result;
        }

        @Override
        public void run() {

            if (isLogged) {
                System.out.println("Running thread with kept cards " + heldCards.toString());
            }

            if (heldCards.length == hand.size()) {
                result = payout.pay(hand, betCredits);
                latch.countDown();
                return;
            }

            int money = 0;
            for (int i = 0; i < nIterations; i++) {
                // Checking whether executor has asked us to stop
                if (Thread.interrupted()) {
                    return;
                }

                Deck simDeck = this.deck.clone();
                simDeck.shuffle(400);
                Hand simHand = hand.clone();
                simHand.keepAndDeal(heldCards, simDeck);
                money += payout.pay(simHand, betCredits);
                this.result = ((double) money) / ((double) (i + 1));

                if (isLogged) {
                    System.out.println(simHand.toString() + " " + payout.pay(simHand, betCredits) + " " + money);
                }
            }

            if (isLogged) {
                System.out.println("RESULT = " + this.result);
            }
            latch.countDown();
        }
    }
}
