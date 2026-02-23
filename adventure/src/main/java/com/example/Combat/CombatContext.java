package com.example.Combat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;

import com.example.Entity.*;
import com.example.Utility.Clamped;

public class CombatContext {
    //integer keys used so references are maintained even if elements are added and removed
    private long playerId;
    private ArrayList<Long> companionIds = new ArrayList<>();
    private ArrayList<Long> enemyIds = new ArrayList<>();
    private ArrayList<Long> wildcardIds = new ArrayList<>();
    
    private HashMap<Long,Integer> targetNumbers = new HashMap<>();
    private HashMap<Integer,Long> targetNumbersReverse = new HashMap<>();

    private HashMap<Long,Entity> actors = new HashMap<>();
    private LinkedList<Long> turnOrder = new LinkedList<>();

    public CombatContext(ArrayList<Entity> entities) {
        entities.sort(
            Comparator.<Entity> comparingInt(Entity::getSpeed).reversed() // fastest to slowest speed
            .thenComparingLong(Entity::getId) // tie breaker: lowest id
        );

        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);
            Long id = entity.getId();

            switch (entity.getRoleCategory()) {
                case PLAYER -> { playerId = id; }
                case COMPANION -> { companionIds.add(id); }
                case WILDCARD -> { wildcardIds.add(id); }
                case ENEMY -> { enemyIds.add(id); }   
            }

            int targetNumber = i + 1;
            targetNumbers.put(id, targetNumber);
            targetNumbersReverse.put(targetNumber, id);

            actors.put(id, entity);
            turnOrder.add(id);
        }
    }

    public void printCombatActors()
    {
        for (Long id : turnOrder) {
            Entity entity = actors.get(id);
            int targetNumber = targetNumbers.get(id);

            Clamped hitPoints = entity.getHP();

            if (entity instanceof EnemyEntity) {
                System.out.printf("%d E \"%s\" %.0f%% HP%n", 
                targetNumber, 
                entity.getName(),
                hitPoints.getRatio() * 100f);

            } else if (entity instanceof CompanionEntity) {
                System.out.printf("%d C \"%s\" %d/%d HP%n", 
                targetNumber, 
                entity.getName(), 
                hitPoints.getValue(),
                hitPoints.getMaxValue());

            } else {
                Clamped manaPoints = ((PlayerEntity) entity).getMP();
                
                System.out.printf("%d P \"%s\" %d/%d HP %d/%d MP%n", 
                targetNumber, 
                entity.getName(), 
                hitPoints.getValue(),
                hitPoints.getMaxValue(), 
                manaPoints.getValue(),
                manaPoints.getMaxValue());
            }
        }
    }

    public Entity nextActor() {
        // if actor is dead skip to next
        while (!turnOrder.isEmpty()) {
            long id = turnOrder.poll();
            Entity actor = actors.get(id);

            // drops dead actors out of turn order
            if (actor != null && !actor.isDead()) {
                turnOrder.add(id);
                return actor;
            }
        }
        return null;
    }

    public HashMap<Long,Entity> getActors() {
        return new HashMap<>(actors);
    }

    public Entity getActorByTargetNumber(int targetNumber) {
        // convert from transient id to persistent id 
        long id = targetNumbersReverse.get(targetNumber);
        return actors.get(id);
    }

    public PlayerEntity getPlayer() {
        return (PlayerEntity) actors.get(playerId);
    }

    public HashMap<Long,Entity> getCompanions() {
        HashMap<Long,Entity> companions = new HashMap<>();

        companionIds.forEach(id -> companions.put(
            id, actors.get(id)
        ));

        return companions;
    }

    public HashMap<Long,Entity> getCompanionsAndPlayer() {
        HashMap<Long,Entity> entities = new HashMap<>();

        companionIds.forEach(id -> entities.put(
            id, actors.get(id)
        ));
        entities.put(
            playerId, actors.get(playerId)
        );

        return entities;
    }

    public HashMap<Long,Entity> getEnemies() {
        HashMap<Long,Entity> enemies = new HashMap<>();

        enemyIds.forEach(id -> enemies.put(
            id, actors.get(id)
        ));

        return enemies;
    }

    public HashMap<Long,Entity> getEntitiesExcludingOne(Entity excluded) {
        HashMap<Long,Entity> entities = new HashMap<>(actors);
        entities.remove(excluded.getId());
        return entities;
    }
}
