package com.example;

import java.util.ArrayList;
import java.util.List;

import com.example.Combat.CombatEncounter;
import com.example.Entity.CompanionEntity;
import com.example.Entity.EnemyEntity;
import com.example.Entity.Entity;
import com.example.Entity.Entity.EntityCategory;
import com.example.Entity.NonPlayerEntity.Role;
import com.example.Entity.PlayerEntity;
import com.example.Utility.DisplayBar;

/**
 * Hello dark world!
 */
public class App 
{
    public static void main( String[] args )
    {
        PlayerEntity player = new PlayerEntity();

        CompanionEntity ally = new CompanionEntity(
            EntityCategory.HUMAN,
            Role.BATTLE_HEALER, 
            2,
            "Zephyra", 
            100, 
            10);
        //celestial smite, balming light, incensed flailing (incense burner swung as a flail)

        EnemyEntity enemy1 = new EnemyEntity(
            EntityCategory.HUMAN,
            Role.HUNTER,
            1,
            "Cutthroat", 
            25, 
            15);
        //stabby stab, chris's crossbow (stolen)

        EnemyEntity enemy2 = new EnemyEntity(
            EntityCategory.UNDEAD,
            Role.BERZERKER, 
            1,
            "Bruiser", 
            50, 
            5);
        //clobberin club, big boot, his friend the fist (just a punch really)
        
        ArrayList<Entity> entities = new ArrayList<>(List.of(player, ally, enemy1, enemy2));

        CombatEncounter encounter = new CombatEncounter(entities);
        encounter.startCombat();
        
        System.out.println(DisplayBar.generateBar(75, 0, 100, 15));
    }
}
