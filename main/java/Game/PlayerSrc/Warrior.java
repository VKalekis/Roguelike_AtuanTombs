package Game.PlayerSrc;

import java.util.Arrays;
import java.util.HashMap;

public class Warrior extends AbstractPlayer {

    public Warrior() {
        super("wizard.png",
                Arrays.asList(SlotType.WAND, SlotType.MAIN_HAND, SlotType.FINGER));
        setXPprogression();
        setStatsProgression();
    }


    public void setXPprogression() {
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
        playerProgression.put(1, new int[]{15, 40, 0, 20});
        playerProgression.put(2, new int[]{5, 10, 0, 5});
        playerProgression.put(3, new int[]{5, 10, 0, 5});
        playerProgression.put(4, new int[]{5, 20, 0, 10});
        playerProgression.put(5, new int[]{5, 20, 0, 15});
        playerProgression.put(6, new int[]{15, 20, 0, 25});

        this.playerStatsProgression = playerProgression;

        this.hitPoints = playerProgression.get(1)[0];
        this.maxHitPoints = playerProgression.get(1)[0];

        this.manaPoints = playerProgression.get(1)[1];
        this.maxManaPoints = playerProgression.get(1)[1];

        this.strength = playerProgression.get(1)[2];

        this.intelligence = playerProgression.get(1)[3];
    }


//    public static void main(String[] args) {
//        Wizard wizard = new Wizard(new Position(1, 2));
//        System.out.println(wizard.getStats());
//        wizard.addXP(350);
//        System.out.println(wizard.getStats());
//    }
}

