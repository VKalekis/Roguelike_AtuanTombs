package Game.Map;

import java.util.Objects;
import java.util.Random;

public class Position {
    private int i, j;

    public Position(int i, int j) {
        this.i = i;
        this.j = j;
    }

    // Constructor with no parameters -> 0,0
    public Position() {
        this.i = 0;
        this.j = 0;
    }

    public Position(Position position) {
        this.i = position.i;
        this.j = position.j;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public void shiftDir(String dir) {
        switch (dir) {
            case "N":
                this.j--;
                break;
            case "E":
                this.i++;
                break;
            case "S":
                this.j++;
                break;
            case "W":
                this.i--;
                break;
            default:
                System.out.println("Invalid direction");
                break;
        }
    }

    public void moveTo(int i, int j) {
        this.i = i;
        this.j = j;
    }

    public void randomize() {
        Random random = new Random();
        this.i = random.nextInt(60) + 1;
        this.j = random.nextInt(60) + 1;
    }

    public double getDistance(Position position1) {
        return Math.sqrt(Math.pow(this.i - position1.getI(), 2) + Math.pow(this.j - position1.getJ(), 2));
    }

    @Override
    public String toString() {
        return "(" + i + " " + j + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return i == position.i && j == position.j;
    }

    @Override
    public int hashCode() {
        return Objects.hash(i, j);
    }
}
