package com.example.Entity.Behaviours;

import java.util.Comparator;
import java.util.List;
import com.example.Entity.Entity;
import com.example.Entity.NonPlayerEntity;

public class SlayerAttackTargeting extends AttackTargeting {
    public SlayerAttackTargeting(NonPlayerEntity self) {
        super(self);
    }

    @Override
    public List<Entity> getRankedTargets() {
        return validTargetsStream()
                // lowest current HP -> lowest max HP -> lowest level
                .sorted(Comparator.comparingInt((Entity e) -> e.getHP().getValue())
                        .thenComparingInt(e -> e.getHP().getMaxValue())
                        .thenComparingInt(Entity::getLevel))
                .toList();
    }
}
