package Game.Items;

import Game.Map.Position;
import Game.Player.Wizard;

import java.util.Random;

public class UsableFactory {

    private final String[] adjectives;
    private final int[] healthpoints;
    private final int[] manapoints;

    public UsableFactory() {

        this.adjectives = new String[]{"Minor", "Normal", "Supreme"};
        this.healthpoints = new int[]{20, 25, 30};
        this.manapoints = new int[]{15, 20, 25};
    }

    public UsableItem makeUsable(Class playerClass, Position position) {
        int index;
        Random random = new Random();
        int rnd;

        // Choose adjective:
        rnd = random.nextInt(10);

        // Supreme Probability: 1/10
        if (rnd == 9) {
            index = 2;
        }
        // Normal Probability: 4/10
        else if (rnd >= 5) {
            index = 1;
        }
        // Minor Probability: 1/10
        else {
            index = 0;
        }

        // Choose type for wizard class. No point in generating mana potions for warrior.
        if (playerClass == Wizard.class) {
            rnd = random.nextInt(11);

            // Restoration Probability: 1/11
            if (rnd == 10) {
                return new UsableItem.UsableBuilder()
                        .named(adjectives[index] + " Restoration Potion")
                        .withSprite(adjectives[index] + "_restoration.png")
                        .atPosition(position)
                        .withItemEffect(new ItemEffect(EffectType.HP_REPLENISH, healthpoints[index]))
                        .withItemEffect(new ItemEffect(EffectType.MP_REPLENISH, manapoints[index]))
                        .withUses(2)
                        .build();
            }
            // Mana Probability: 5/11
            else if (rnd >= 5) {
                return new UsableItem.UsableBuilder()
                        .named(adjectives[index] + " Mana Potion")
                        .withSprite(adjectives[index] + "_mana.png")
                        .atPosition(position)
                        .withItemEffect(new ItemEffect(EffectType.MP_REPLENISH, manapoints[index]))
                        .withUses(2)
                        .build();
            }
            // Health Probability: 5/11
            else {
                return new UsableItem.UsableBuilder()
                        .named(adjectives[index] + " Health Potion")
                        .withSprite(adjectives[index] + "_healing.png")
                        .atPosition(position)
                        .withItemEffect(new ItemEffect(EffectType.HP_REPLENISH, healthpoints[index]))
                        .withUses(2)
                        .build();
            }
        }
        // Only Health potions for warrior.
        else {

            return new UsableItem.UsableBuilder()
                    .named(adjectives[index] + " Health Potion")
                    .withSprite(adjectives[index] + "_healing.png")
                    .atPosition(position)
                    .withItemEffect(new ItemEffect(EffectType.HP_REPLENISH, healthpoints[index]))
                    .withUses(2)
                    .build();
        }

    }
}
