package com.example.Entity.Behaviours;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.example.Combat.CombatContext;
import com.example.Entity.Entity;
import com.example.Entity.NonPlayerEntity;

public abstract class AttackTargeting extends Targeting {
    protected AttackTargeting(NonPlayerEntity self) {
        super(self);
    }

    protected List<Entity> rankRandomly() {
        List<Entity> entities = new ArrayList<>(getValidTargets().values());
        Collections.shuffle(entities);
        return entities;
    }

    @Override
    protected Map<Long,Entity> getValidTargets() {
        CombatContext context = self.getCombatContext();

        return switch (self.getRoleCategory()) {
            case COMPANION -> context.getEnemies();
            case ENEMY -> context.getCompanionsAndPlayer();
            case WILDCARD -> context.getEntitiesExcludingOne(self);
            case PLAYER -> null;
        };
    }
}
