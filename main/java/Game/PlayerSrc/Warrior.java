package Game.PlayerSrc;

import java.util.Arrays;
import java.util.HashMap;

public class Warrior extends AbstractPlayer {

    public Warrior() {
        super("knight.png",
                Arrays.asList(SlotType.MAIN_HAND, SlotType.OFF_HAND),
                4);
        setXPProgression();
        setStatsProgression();
    }

    public void setXPProgression() {
        // MinHP for Level - Level
//        this.xpLevelsProgression = Map.of(
//                0, 1,
//                300, 2,
//                900, 3,
//                2700, 4,
//                6500, 5
//        );
        this.xpLevelsProgression = new HashMap<>();
        this.xpLevelsProgression.put(0,1);
        this.xpLevelsProgression.put(300,2);
        this.xpLevelsProgression.put(900,3);
        this.xpLevelsProgression.put(2700,4);
        this.xpLevelsProgression.put(6500,5);


    }

    public void setStatsProgression() {
        HashMap<Integer, int[]> playerProgression = new HashMap<>();

        //HP - MP - Str - Int
        // 1,2,3..: For progression (Level 1,2,3..)
        playerProgression.put(1, new int[]{30,0,10,0});
        playerProgression.put(2, new int[]{40,0,15,0});
        playerProgression.put(3, new int[]{50,0,25,0});
        playerProgression.put(4, new int[]{60,0,40,0});
        playerProgression.put(5, new int[]{80,0,40,0});
        playerProgression.put(6, new int[]{100,0,50,0});

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
        return 100;
    }
}

