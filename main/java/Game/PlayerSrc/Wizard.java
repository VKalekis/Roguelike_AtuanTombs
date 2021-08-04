package Game.PlayerSrc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Wizard extends AbstractPlayer {

    private Map<Integer, Integer> manaCost;

    public Wizard() {
        super("wizard.png",
                Arrays.asList(SlotType.MAIN_HAND, SlotType.OFF_HAND),
                15);

        setXPProgression();

        setStatsProgression();

        this.manaCost = new HashMap<>();
        this.manaCost.put(1,5);
        this.manaCost.put(2,5);
        this.manaCost.put(3,8);
        this.manaCost.put(4,8);
        this.manaCost.put(5,10);
        this.manaCost.put(6,12);
    }


    protected void setXPProgression() {
        // MinHP for Level - Level
//        this.xpLevelsProgression = Map.of(
//                0, 1,
//                300, 2,
//                900, 3,
//                2700, 4,
//                6500, 5
//        );
        this.xpLevelsProgression = new HashMap<>();
        this.xpLevelsProgression.put(0, 1);
        this.xpLevelsProgression.put(300, 2);
        this.xpLevelsProgression.put(900, 3);
        this.xpLevelsProgression.put(2700, 4);
        this.xpLevelsProgression.put(6500, 5);


    }

    protected void setStatsProgression() {
        HashMap<Integer, int[]> playerProgression = new HashMap<>();

        //HP - MP - Str - Int
        // 1,2,3..: For progression (Level 1,2,3..)
        playerProgression.put(1, new int[]{15, 40, 0, 20});
        playerProgression.put(2, new int[]{20, 50, 0, 25});
        playerProgression.put(3, new int[]{25, 60, 0, 30});
        playerProgression.put(4, new int[]{30, 80, 0, 40});
        playerProgression.put(5, new int[]{35, 100, 0, 55});
        playerProgression.put(6, new int[]{50, 120, 0, 80});

        this.statsProgression = playerProgression;

        this.hitPoints = playerProgression.get(1)[0];
        this.maxHitPoints = playerProgression.get(1)[0];

        this.manaPoints = playerProgression.get(1)[1];
        this.maxManaPoints = playerProgression.get(1)[1];

        this.strength = playerProgression.get(1)[2];

        this.intelligence = playerProgression.get(1)[3];
    }

    @Override
    public int dealDamage() {
//        if (manaPoints>=manaCost.get(level)) {
//            manaPoints -= manaCost.get(level);
//            return 100;
//        }
//        return 0;
        return 100;
    }
}

