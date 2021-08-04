package Game.PlayerSrc;

import Game.Drawable;
import Game.Entity;
import Game.Items.*;
import Game.Map.Position;

import java.util.*;

public abstract class AbstractPlayer implements Entity, Drawable {
    protected Position position;
    protected String sprite;
    protected int maxHitPoints, hitPoints;
    protected int maxManaPoints, manaPoints;
    protected int strength;
    protected int intelligence;
    protected int experiencePoints, level;
    protected int defense;
    protected int visibility;

    protected Map<Integer, Integer> xpLevelsProgression;
    protected Map<Integer, int[]> statsProgression;

    protected Map<SlotType, Equippable> slotsAndEquippables;
    protected List<Item> inventory;
    protected int inventoryCursor;

    protected abstract void setXPProgression();
    protected abstract void setStatsProgression();
    public abstract int dealDamage();

    public AbstractPlayer(String sprite, List<SlotType> availableSlots, int visibility) {

        this.sprite = sprite;
        this.experiencePoints = 0;
        this.level = 1;

        this.slotsAndEquippables = new HashMap<>();
        for (SlotType slot : availableSlots) {
            this.slotsAndEquippables.put(slot, null);
        }

        this.inventory = new ArrayList<>();
        this.inventoryCursor = 0;

        this.visibility = visibility;
    }

    public void pickUp(Item item) {
        // Max Items - 10
        if (inventory.size() < 10) {
            inventory.add(item);
        }
    }

    public boolean dropItem() {
        try {
            inventory.remove(inventoryCursor);
            if (inventoryCursor >= inventory.size()) {
                inventoryCursor = inventory.size() - 1;
            }
            return true;
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean drop(Item item) {
        if (inventory.contains(item)) {
            inventory.remove(item);
            return true;
        }
        return false;
    }

    public boolean isSlotEmpty(SlotType slot) {
        return slotsAndEquippables.get(slot) == null;
    }

    private void decodeEquippableEffect(EffectType effectType, int amount) {
        switch (effectType) {
            case DEFENSE:
                defense += amount;
                break;
            case HP_BONUS:
                maxHitPoints += amount;
                break;
            case MP_BONUS:
                maxManaPoints += amount;
                break;
            case INT_BONUS:
                intelligence += amount;
                break;
            case STR_BONUS:
                strength += amount;
                break;
            case NONE:
                System.out.println("No effects");
                break;
            default:
                System.out.println("Error");

        }
    }

    private void decodeUsableEffect(EffectType effectType, int amount) {
        switch (effectType) {
            case HP_REPLENISH:
                hitPoints += amount;
                if (hitPoints > maxHitPoints) {
                    hitPoints = maxHitPoints;
                }
                break;
            case MP_REPLENISH:
                if (maxManaPoints != 0) {
                    manaPoints += amount;
                    if (manaPoints > maxManaPoints) {
                        manaPoints = maxManaPoints;
                    }
                }
                break;
            default:
                System.out.println("Error");
                break;
        }
    }


    public boolean equip(SlotType slot, Equippable equippable) {
        if ((slotsAndEquippables.containsKey(slot)) && (isSlotEmpty(slot)) && (equippable.getSlot() == slot)) {
            slotsAndEquippables.put(slot, equippable);
            for (ItemEffect itemEffect : equippable.getItemEffects()) {
                decodeEquippableEffect(itemEffect.getEffectType(), itemEffect.getAmount());
            }
            return true;
        }
        return false;
    }

    public Equippable remove(SlotType slot) {
        if (!isSlotEmpty(slot)) {
            Equippable equippable = slotsAndEquippables.get(slot);
            for (ItemEffect itemEffect : equippable.getItemEffects()) {
                decodeEquippableEffect(itemEffect.getEffectType(), -itemEffect.getAmount());
            }
            slotsAndEquippables.put(slot, null);
            return equippable;
        }
        return null;
    }

    public void moveInventoryCursor(int offset) {

        this.inventoryCursor += offset;
        if (inventoryCursor < 0) {
            inventoryCursor = inventory.size();
        }
        if (inventoryCursor > inventory.size() - 1) {
            inventoryCursor = 0;
        }
        System.out.println(inventoryCursor);
    }

    public void use() {
        try {
            Usable usable = (Usable) inventory.get(inventoryCursor);
            if (usable.getUsesLeft() > 0) {
                for (ItemEffect itemEffect : usable.getItemEffects()) {
                    decodeUsableEffect(itemEffect.getEffectType(), itemEffect.getAmount());
                }
                usable.decreaseUses();
                if (usable.getUsesLeft() == 0) {
                    drop(usable);
                }
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

    }

    public void addXP(int xp) {
        experiencePoints += xp;

        // Don't like it, alla den mporesa na skeftw kati kalutero wste na gnwrizoume ka8e stigmh to level kai ta stats tou.
        // Tsekarei sunexeia an anebhke level akoma kai an phre 1XP.

        int oldlevel = level;
        level = calculateLevel();

        if (oldlevel != level) {
            int[] stats = statsProgression.get(level);

            //HP - MP - Str - Int
            maxHitPoints = stats[0];
            hitPoints = stats[0];

            maxManaPoints = stats[1];
            manaPoints = stats[1];

            strength = stats[2];

            intelligence = stats[3];
        }
    }

    public void removeXP(int xp) {
        experiencePoints -= xp;

        int oldlevel = level;
        level = calculateLevel();

        if (oldlevel != level) {
            int[] stats = statsProgression.get(level);

            //HP - MP - Str - Int
            maxHitPoints = stats[0];
            if (hitPoints > maxHitPoints) {
                hitPoints = maxHitPoints;
            }

            maxManaPoints = stats[1];
            if (manaPoints > maxManaPoints) {
                manaPoints = maxManaPoints;
            }

            strength = stats[2];

            intelligence = stats[3];
        }
    }

    private int calculateLevel() {
        int baseLevelXP = xpLevelsProgression.keySet().stream().filter((x) -> x <= experiencePoints).max(Integer::compareTo).get();
        return xpLevelsProgression.get(baseLevelXP);
    }

    public void rest() {
        hitPoints += 5;
        if (hitPoints > maxHitPoints) {
            hitPoints = maxHitPoints;
        }
        if (maxManaPoints != 0) {
            manaPoints += 5;
            if (manaPoints > maxManaPoints) {
                manaPoints = maxManaPoints;
            }
        }
    }

    public int getExperiencePoints() {
        return experiencePoints;
    }

    public int getLevel() {
        return level;
    }

    public int getMaxHitPoints() {
        return maxHitPoints;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public int getMaxManaPoints() {
        return maxManaPoints;
    }

    public int getManaPoints() {
        return manaPoints;
    }

    public int getStrength() {
        return strength;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public int getDefense() {
        return defense;
    }

    public int getVisibility() {
        return visibility;
    }

    public String getStats() {
        return new StringBuilder().append("Hitpoints:").append(hitPoints)
                .append(" MaxHitPoints:").append(maxHitPoints)
                .append(" Manapoints:").append(manaPoints)
                .append(" MaxManaPoints:").append(maxManaPoints)
                .append(" Strength:").append(strength)
                .append(" Intelligence:").append(intelligence)
                .append(" Defense:").append(defense)
                .append(" Experience:").append(experiencePoints)
                .append(" Level:").append(level).toString();
    }

    public String getInventoryHTML() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < inventory.size(); i++) {
            if (i == inventoryCursor) {
                sb.append(" &#8594; ");
            } else {
                sb.append("   ");
            }
            sb.append(inventory.get(i)).append("<br>");
        }
        return sb.toString();
    }

    private String decodeEffectHTML(EffectType effectType, int amount) {
        StringBuilder sb = new StringBuilder().append("+").append(amount).append(" ");
        switch (effectType) {
            case DEFENSE:
                return sb.append("DEF   ").toString();
            case HP_BONUS:
                return sb.append("HP   ").toString();
            case MP_BONUS:
                return sb.append("MP   ").toString();
            case INT_BONUS:
                return sb.append("INT   ").toString();
            case STR_BONUS:
                return sb.append("STR   ").toString();
            case NONE:
            default:
                return "";

        }
    }

    public String getSlotsHTML() {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<SlotType, Equippable> entry : slotsAndEquippables.entrySet()) {
            if (entry.getValue() != null) {
                sb.append(entry.getKey()).append(" : ").append(entry.getValue().toStringHTML()).append("<br>");
                for (ItemEffect itemEffect : entry.getValue().getItemEffects()) {
                    sb.append("&nbsp;&nbsp;&nbsp;&nbsp;").append(decodeEffectHTML(itemEffect.getEffectType(), itemEffect.getAmount()));
                }
                sb.append("<br>");

            } else {
                sb.append(entry.getKey()).append(" : <br>");
            }
        }
        return sb.toString();

    }

    public String getStatsHTML() {
        return new StringBuilder("<html>").append("<u>Player stats:</u>").append("<br>")
                .append("Hitpoints: ").append(hitPoints)
                .append("/").append(maxHitPoints).append("<br>")
                .append("Manapoints: ").append(manaPoints)
                .append("/").append(maxManaPoints).append("<br>")
                .append("Strength: ").append(strength).append("<br>")
                .append("Intelligence: ").append(intelligence).append("<br>")
                .append("Defense: ").append(defense).append("<br>")
                .append("Experience: ").append(experiencePoints).append("<br>")
                .append("Level: ").append(level).append("<br>")
                .append("Slots: ").append("<br>").append(getSlotsHTML())
                .append("Inventory: ").append("<br>").append(getInventoryHTML())
                .append("</html>").toString();
    }

    public Position getPosition() {
        return position;
    }

    public void movePlayer(String dir) {
        position.shiftDir(dir);
    }

    public void setStartingPosition(Position position) {
        this.position = position;
    }

    public void takeDamage(int dmg) {
        hitPoints -= dmg;
    }

    @Override
    public List<String> getSprites() {
        return Arrays.asList(this.sprite);
    }

    @Override
    public Position getDrawablePosition() {
        return new Position(position.getI(), position.getJ() - 1);
    }
}
