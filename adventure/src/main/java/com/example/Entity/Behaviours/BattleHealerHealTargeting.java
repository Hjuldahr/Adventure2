package com.example.Entity.Behaviours;

import java.util.Comparator;
import java.util.List;

import com.example.Entity.Entity;
import com.example.Entity.NonPlayerEntity;

public class BattleHealerHealTargeting extends HealTargeting {
    public BattleHealerHealTargeting(NonPlayerEntity self) {
        super(self);
    }

    @Override
    public List<Entity> getRankedTargets() {
        if (self.getHP().getRatio() < 0.5f) {
            return List.of(self);
        }
        // Prioritize most wounded allies
        return validTargetsStream()
                .filter(e -> e.getHP().getRatio() < 0.5f)
                .sorted(Comparator.comparingDouble(e -> e.getHP().getRatio()))
                .toList();
    }
}