package com.example.Entity.Behaviours;

import java.util.Comparator;
import java.util.List;
import com.example.Entity.Entity;
import com.example.Entity.NonPlayerEntity;

public class AssassinAttackTargeting extends AttackTargeting {
    public AssassinAttackTargeting(NonPlayerEntity self) {
        super(self);
    }

    @Override
    public List<Entity> getRankedTargets() {
        return validTargetsStream()
                // Assassin metric comparator
                .sorted(Comparator.comparingDouble(e -> e.getAssassinMetric(1.0, 1.5)))
                .toList();
    }
}