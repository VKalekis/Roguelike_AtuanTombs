package Game.Items;

import Game.PlayerSrc.SlotType;

import java.util.ArrayList;

public class EquippableItem implements Equippable {
    private String name;
    private String description;
    private ArrayList<ItemEffect> itemEffects;
    private SlotType slot;

    private EquippableItem() {
        itemEffects = new ArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public ArrayList<ItemEffect> getItemEffects() {
        return itemEffects;
    }

    public SlotType getSlot() {
        return slot;
    }

    @Override
    public String toString() {
        return "EquippableItem{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", itemEffects=" + itemEffects +
                ", slot=" + slot +
                '}';
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

        public EquippableBuilder withDescription(String description) {
            equippableItem.description = description;
            return this;
        }

        public EquippableBuilder withItemEffect(ItemEffect itemEffect) {
            if (!itemEffect.getEffectType().isUseEffect()) {
                equippableItem.itemEffects.add(itemEffect);
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