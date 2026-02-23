package com.example.Items;

public class Consumable extends Item {
    private int hitPointChange = 0;
    private int manaPointChange = 0;
    
    public Consumable(String name, int goldValue, int hitPointChange, int manaPointChange) {
        super(name, goldValue);

        this.hitPointChange = hitPointChange;
        this.manaPointChange = manaPointChange;
    }

    public int getHitPointChange() {
        return hitPointChange;
    }

    public int getManaPointChange() {
        return manaPointChange;
    }
}
