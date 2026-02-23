package com.example.Combat;

import java.util.ArrayList;
import java.util.List;

import com.example.Entity.*;
import com.example.Utility.Input;

public class CombatEncounter 
{
    private CombatContext context;

    public CombatEncounter(ArrayList<Entity> entities) {
        context = new CombatContext(entities);
    }

    public void startCombat()
    {
        //TODO replace with check for if all enemies, or player is dead
        context.getActors().values().forEach(a -> a.onCombatStart(context));

        while (true) {
            context.printCombatActors();
            System.out.println("------------------");

            Entity actor = context.nextActor();
            actor.onCombatTurn();

            Input.getAny("Press [Enter] to continue ");

            
            
            break; //Temp
        }

        context.getActors().values().forEach(Entity::onCombatEnd);
    }

    public boolean areAlive(List<Entity> entities) {
        for (Entity entity : entities) {
            if (!entity.getHP().atMinimum()) {
                return true;
            }
        }
        return false;
    } 
}
