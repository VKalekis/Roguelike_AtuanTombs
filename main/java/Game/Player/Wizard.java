package Game.Player;

import java.util.Arrays;
import java.util.Map;

public class Wizard extends AbstractPlayer {

    private final Map<Integer, Integer> manaCost;

    public Wizard(String name) {
        super(name,
                "wizard.png",
                Arrays.asList(SlotType.MAIN_HAND, SlotType.OFF_HAND),
                8);

        setXPProgression();
        setStatsProgression();

        this.manaCost = Map.of(
                1, 5,
                2, 5,
                3, 8,
                4, 8,
                5, 10,
                6, 12
        );
    }


    protected void setXPProgression() {
        // MinHP for Level - Level
        this.xpLevelsProgression = Map.of(
                0, 1,
                300, 2,
                900, 3,
                2700, 4,
                6500, 5
        );
    }

    protected void setStatsProgression() {
        //HP - MP - Str - Int
        // 1,2,3..: For progression (Level 1,2,3..)
        this.statsProgression = Map.of(
                1, new int[]{15, 40, 0, 20},
                2, new int[]{20, 50, 0, 25},
                3, new int[]{25, 60, 0, 30},
                4, new int[]{30, 80, 0, 40},
                5, new int[]{35, 100, 0, 55},
                6, new int[]{50, 120, 0, 80}
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
        if (manaPoints >= manaCost.get(level)) {
            manaPoints -= manaCost.get(level);
            return intelligence;
        }
        return 0;
    }
}

