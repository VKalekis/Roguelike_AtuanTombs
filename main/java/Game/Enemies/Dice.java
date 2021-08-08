package Game.Enemies;

import java.util.Random;

public class Dice {
    private final int numberOfDices;
    private final int numberOfSides;
    private final int bonus;
    private final Random randomRoll;

    public Dice(int numberOfDices, int numberOfSides) {
        this.numberOfDices = numberOfDices;
        this.numberOfSides = numberOfSides;
        this.bonus = 0;
        this.randomRoll = new Random();
    }

    public Dice(int numberOfDices, int numberOfSides, int bonus) {
        this.numberOfDices = numberOfDices;
        this.numberOfSides = numberOfSides;
        this.bonus = bonus;
        this.randomRoll = new Random();
    }

    @Override
    public String toString() {
        return numberOfDices + "d" + numberOfSides + "+" + bonus;
    }

    public int roll() {
        int rollsum = bonus;
        for (int i = 0; i < numberOfDices; i++) {
            rollsum += randomRoll.nextInt(numberOfSides) + 1;
        }
        return rollsum;
    }

//    public static void main(String[] args) {
//        Dice dice1 = new Dice(2,6);
//        Dice dice2 = new Dice(3,10,1);
//        System.out.println(dice1.roll());
//        System.out.println(dice2.roll());
//    }
}