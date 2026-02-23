package com.example.Items;

import com.example.Combat.Attack;

public class Weapon extends Item {
    private int attackPower;
    
    public Weapon(String name, int goldValue, int attackPower) {
        super(name, goldValue);

        this.attackPower = attackPower;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public Attack getAttack() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAttack'");
    }
}
