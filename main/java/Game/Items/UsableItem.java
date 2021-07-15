package Game.Items;

import java.util.ArrayList;

public class UsableItem implements Usable {
    private String name;
    private String description;
    private ArrayList<ItemEffect> itemEffects;
    private int usesLeft;

    private UsableItem() {
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

    @Override
    public int getUsesLeft() {
        return usesLeft;
    }

    @Override
    public void decreaseUses() {
        usesLeft--;
    }

    @Override
    public String toString() {
        return "UsableItem{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", itemEffects=" + itemEffects +
                ", usesLeft=" + usesLeft +
                '}';
    }

    public static class UsableBuilder {
        private UsableItem usableItem;

        public UsableBuilder() {
            usableItem = new UsableItem();
        }

        public UsableBuilder named(String name) {
            usableItem.name = name;
            return this;
        }

        public UsableBuilder withDescription(String description) {
            usableItem.description = description;
            return this;
        }

        public UsableBuilder withItemEffect(ItemEffect itemEffect) {
            if (itemEffect.getEffectType().isUseEffect()) {
                usableItem.itemEffects.add(itemEffect);
            }
            return this;
        }

        public UsableBuilder withUses(int uses) {
            usableItem.usesLeft = uses;
            return this;
        }

        public UsableItem build() {
            return usableItem;
        }

    }
}