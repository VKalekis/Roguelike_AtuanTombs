package Game.Items;

public enum EffectType {
    NONE(false), INT_BONUS(false), STR_BONUS(false), MP_BONUS(false), HP_BONUS(false), HP_REPLENISH(true), MP_REPLENISH(true), DEFENSE(false);

    private final boolean useEffect;

    EffectType(boolean useEffect) {
        this.useEffect = useEffect;
    }

    public boolean isUseEffect() {
        return useEffect;
    }
}