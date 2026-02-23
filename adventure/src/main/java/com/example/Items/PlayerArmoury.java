package com.example.Items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.Entity.Entity.EntityCategory;

public class PlayerArmoury {
    private Weapon equippedWeapon = null;
    private ArrayList<Weapon> storedWeapons = new ArrayList<>();

    //private Armour equippedHeadArmour = null;
    //private Armour equippedChestArmour = null;
    //private Armour equippedLegArmour = null;
    private HashMap<Armour.Type, Armour> equippedArmour;
    private ArrayList<Armour> storedArmour = new ArrayList<>();

    public void equipWeapon(Weapon weapon) {
        if (equippedWeapon != null) {
            storedWeapons.add(weapon);
        }
        equippedWeapon = weapon;
    }

    public Weapon getEquippedWeapon() {
        return equippedWeapon;
    }

    public void equipArmour(Armour newArmour)
    {
        Armour previouslyEquipped = equippedArmour.put(newArmour.getType(), newArmour);
        if (previouslyEquipped != null) {
            storedArmour.add(previouslyEquipped);
        }
    }

    public Armour getEquippedArmour(Armour.Type type) {
        return equippedArmour.get(type);
    }

    public Armour[] getEquippedArmour() {
        return equippedArmour.values().toArray(Armour[]::new);
    }
}