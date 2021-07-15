package Game.Items;

public class ItemEffect {
    private final EffectType effectType;
    private final int amount;

    public ItemEffect(EffectType effectType, int amount) {
        this.effectType = effectType;
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public EffectType getEffectType() {
        return effectType;
    }

    @Override
    public String toString() {
        return "ItemEffect{" +
                "effectType=" + effectType +
                ", amount=" + amount +
                '}';
    }
}