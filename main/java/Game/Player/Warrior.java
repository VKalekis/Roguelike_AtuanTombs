package Game.Player;

import java.util.Arrays;
import java.util.Map;

public class Warrior extends AbstractPlayer {

    public Warrior(String name) {
        super(name,
                "knight.png",
                Arrays.asList(SlotType.MAIN_HAND, SlotType.OFF_HAND),
                4);
        setXPProgression();
        setStatsProgression();
    }

    public void setXPProgression() {
        // MinHP for Level - Level
        this.xpLevelsProgression = Map.of(
                0, 1,
                300, 2,
                900, 3,
                2700, 4,
                6500, 5
        );
    }

    public void setStatsProgression() {
        //HP - MP - Str - Int
        // 1,2,3..: For progression (Level 1,2,3..)
        this.statsProgression = Map.of(
                1, new int[]{30, 0, 10, 0},
                2, new int[]{40, 0, 15, 0},
                3, new int[]{50, 0, 25, 0},
                4, new int[]{60, 0, 40, 0},
                5, new int[]{80, 0, 40, 0},
                6, new int[]{100, 0, 50, 0}
        );

        this.hitPoints = statsProgression.get(1)[0];
        this.maxHitPoints = hitPoints;

        this.manaPoints = statsProgression.get(1)[1];
        this.maxManaPoints = manaPoints;

        this.strength = statsProgression.get(1)[2];

        this.intelligence = statsProgression.get(1)[3];
    }

    @Override
    public int dealDamage() {
        return strength;
    }
}

