package Game.Items;

import java.util.ArrayList;

public class UsableItem implements Usable {
    private String name;
    private String description;
    private ArrayList<ItemEffect> itemEffects;
    private int usesLeft;
    private int uses;
    private String sprite;

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
    public int getUses() {
        return uses;
    }

    @Override
    public String getSprite() {
        return sprite;
    }

    @Override
    public void decreaseUses() {
        usesLeft--;
    }

    @Override
    public String toString() {
        return name + "- Uses Left: " + usesLeft + "/" +uses;
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

        public UsableBuilder withSprite(String sprite) {
            usableItem.sprite = sprite;
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
            usableItem.uses = uses;
            return this;
        }

        public UsableItem build() {
            return usableItem;
        }

    }
}