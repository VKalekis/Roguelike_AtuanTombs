package Game.Items;

import Game.Map.Position;
import Game.PlayerSrc.AbstractPlayer;
import Game.PlayerSrc.SlotType;
import Game.PlayerSrc.Warrior;
import Game.PlayerSrc.Wizard;

import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

public class EquippableFactory {

    private String[] adjectives;
    private Map<Class, String[]> availableWeaponTypes;
    private Map<String, Integer> numberOfSprites;
    private Map<String, SlotType> slots;
    private String[] surnames;

    public EquippableFactory() {

        this.adjectives = new String[]{"Fierce", "Stubborn", "Pure", "Ancient"};

        this.availableWeaponTypes = Map.of(
                Warrior.class, new String[]{"Sword", "Axe", "Shield"},
                Wizard.class, new String[]{"Staff", "Shield"});

        this.numberOfSprites = Map.of(
                "Sword", 13,
                "Axe", 8,
                "Shield", 5,
                "Staff", 4
        );

        this.slots = Map.of(
                "Sword", SlotType.MAIN_HAND,
                "Axe", SlotType.MAIN_HAND,
                "Shield", SlotType.OFF_HAND,
                "Staff", SlotType.MAIN_HAND
        );

        this.surnames = new String[]{"of the Champion", "of Erreth-Akbe", "of Rok"};
    }

    public EquippableItem makeEquippable(AbstractPlayer player, Position position) {

        int itemLevel = 5 * player.getLevel() + 5;
        StringBuilder name = new StringBuilder();
        String weaponType;
        Random random = new Random();

        // Adjective
        name.append(adjectives[random.nextInt(adjectives.length)]).append(" ");
        // Weapon type
        if (player.getClass() == Wizard.class) {
            weaponType = availableWeaponTypes.get(Wizard.class)[random.nextInt(availableWeaponTypes.get(Wizard.class).length)];
        } else {
            weaponType = availableWeaponTypes.get(Warrior.class)[random.nextInt(availableWeaponTypes.get(Warrior.class).length)];
        }
        name.append(weaponType).append(" ");
        // Surname
        name.append(surnames[random.nextInt(surnames.length)]);

        // Effects based on player class.
        EffectType mainEffect;
        EffectType[] otherEffects;

        if (player.getClass() == Wizard.class) {
            mainEffect = EffectType.INT_BONUS;
            otherEffects = new EffectType[]{EffectType.HP_BONUS, EffectType.MP_BONUS};
        } else {
            mainEffect = EffectType.STR_BONUS;
            otherEffects = new EffectType[]{EffectType.HP_BONUS, EffectType.DEFENSE};
        }

        LinkedList<ItemEffect> effects = new LinkedList<>();

        int stat = random.nextInt(itemLevel) + 1;
        effects.add(new ItemEffect(mainEffect, stat));
        itemLevel = itemLevel - stat;
        EffectType secondStat = otherEffects[random.nextInt(otherEffects.length)];
        effects.add(new ItemEffect(secondStat, itemLevel));

        EquippableItem equippableItem = new EquippableItem.EquippableBuilder()
                .named(name.toString())
                .withSprite(new StringBuilder().append(weaponType.toLowerCase()).append("_")
                        .append(random.nextInt(numberOfSprites.get(weaponType))).append(".png").toString())
                .atPosition(position)
                .withItemEffects(effects)
                .atSlot(slots.get(weaponType))
                .build();
        return equippableItem;

    }
}
