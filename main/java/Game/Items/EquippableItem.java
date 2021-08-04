package Game.Items;

import Game.Map.Position;
import Game.PlayerSrc.SlotType;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EquippableItem implements Equippable {
    private String name;
    //private String description;
    private Position position;
    private ArrayList<ItemEffect> itemEffects;
    private SlotType slot;
    private String sprite;

    private EquippableItem() {
        itemEffects = new ArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }

//    @Override
//    public String getDescription() {
//        return description;
//    }

    @Override
    public ArrayList<ItemEffect> getItemEffects() {
        return itemEffects;
    }

    public SlotType getSlot() {
        return slot;
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
    public Position getPosition() {
        return position;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (ItemEffect itemEffect : itemEffects) {
            sb.append("    ").append(itemEffect).append("\n");

        }
        return new StringBuilder().append(name).append(" - Item effects:\n")
                .append(sb).toString();
    }

    public String toStringHTML() {

        return new StringBuilder().append(name).toString();
    }

    public static class EquippableBuilder {
        private EquippableItem equippableItem;

        public EquippableBuilder() {
            equippableItem = new EquippableItem();
        }

        public EquippableBuilder named(String name) {
            equippableItem.name = name;
            return this;
        }

//        public EquippableBuilder withDescription(String description) {
//            equippableItem.description = description;
//            return this;
//        }

        public EquippableBuilder withSprite(String sprite) {
            equippableItem.sprite = sprite;
            return this;
        }

        public EquippableBuilder atPosition(Position position) {
            equippableItem.position = position;
            return this;
        }

        public EquippableBuilder withItemEffect(ItemEffect itemEffect) {
            if (!itemEffect.getEffectType().isUseEffect()) {
                equippableItem.itemEffects.add(itemEffect);
            }
            return this;
        }

        public EquippableBuilder withItemEffects(List<ItemEffect> itemEffects) {
            for (ItemEffect itemEffect : itemEffects) {
                if (!itemEffect.getEffectType().isUseEffect()) {
                    equippableItem.itemEffects.add(itemEffect);
                }
            }
            return this;
        }

        public EquippableBuilder atSlot(SlotType slot) {
            equippableItem.slot = slot;
            return this;
        }

        public EquippableItem build() {
            return equippableItem;
        }

    }
}