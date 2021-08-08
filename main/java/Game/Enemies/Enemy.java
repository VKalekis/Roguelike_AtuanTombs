package Game.Enemies;

import Game.Map.Position;
import Game.Map.RoomMap;
import Game.State.Drawable;
import Game.State.Entity;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

abstract public class Enemy implements Entity, Drawable {

    private final String name;
    private int hitpoints;
    private final Weapon weapon;
    private final int experience;
    private final String announcementText;
    protected Position position;
    private final String sprite;
    private final Astar astar;
    private List<Position> nextMoves;

    public Enemy(String name, int hitpoints, Weapon weapon, int experience,
                 String announcementText, String sprite) {
        this.hitpoints = hitpoints;
        this.name = name;
        this.weapon = weapon;
        this.experience = experience;
        this.announcementText = announcementText;
        this.sprite = sprite;

        astar = new Astar();
        nextMoves = new LinkedList<>();
    }

    public int getExperience() {
        return experience;
    }

    public String getAnnouncementText() {
        return announcementText;
    }

    public int getHitpoints() {
        return hitpoints;
    }

    public boolean isAlive() {
        return hitpoints > 0;
    }

    public String getName() {
        return name;
    }

    public int dealDamage() {
        return weapon.hit();
    }

    public void takeDamage(int damage) {
        hitpoints -= damage;
    }

    public Position getPosition() {
        return position;
    }

    public void setStartingPosition(Position position) {
        this.position = position;
    }

    public void runAstar(RoomMap roomMap, Position goal) {
        nextMoves.clear();
        nextMoves = astar.run(roomMap, roomMap.getMapTile(this.getPosition()), roomMap.getMapTile(goal));
    }

    public Position getNextMove() {
        try {
            return nextMoves.get(1);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void move(Position position) {
        this.position = position;
    }

    @Override
    public List<String> getSprites() {
        return Arrays.asList(sprite);
    }

    @Override
    public Position getDrawablePosition() {
        return position;
    }

    @Override
    public String toString() {
        return "Enemy : " + name + " { hitpoints : " + hitpoints + ", weapon : " + weapon + " }";

    }

}
