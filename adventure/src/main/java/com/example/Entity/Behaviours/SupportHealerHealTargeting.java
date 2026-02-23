package com.example.Entity.Behaviours;

import java.util.Comparator;
import java.util.List;

import com.example.Entity.Entity;
import com.example.Entity.NonPlayerEntity;

public class SupportHealerHealTargeting extends HealTargeting {
    public SupportHealerHealTargeting(NonPlayerEntity self) {
        super(self);
    }

    @Override
    public List<Entity> getRankedTargets() {
        if (self.getHP().getRatio() < 0.15f) {
                return List.of(self);
            }
            return validTargetsStream()
                    .filter(e -> !e.getHP().atMaximum())
                    .sorted(Comparator.comparingDouble(e -> e.getHP().getRatio()))
                    .toList();
    }
}
