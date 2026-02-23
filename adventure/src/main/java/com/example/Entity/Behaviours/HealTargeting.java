package com.example.Entity.Behaviours;

import java.util.Map;

import com.example.Combat.CombatContext;
import com.example.Entity.Entity;
import com.example.Entity.NonPlayerEntity;

public abstract class HealTargeting extends Targeting {
    protected HealTargeting(NonPlayerEntity self) {
        super(self);
    }

    @Override
    protected Map<Long,Entity> getValidTargets() {
        CombatContext context = self.getCombatContext();

        return switch (self.getRoleCategory()) {
            case COMPANION -> context.getCompanionsAndPlayer();
            case ENEMY -> context.getEnemies();
            case WILDCARD -> Map.of(self.getId(), self);
            case PLAYER -> null;
        };
    }
}
