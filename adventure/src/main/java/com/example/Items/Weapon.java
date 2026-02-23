package com.example.Items;

public class Weapon extends Item {
    private int attackPower;
    
    public Weapon(String name, int goldValue, int attackPower) {
        super(name, goldValue);

        this.attackPower = attackPower;
    }

    public int getAttackPower() {
        return attackPower;
    }
}
