package com.example.Entity.Behaviours;

import java.util.List;

import com.example.Entity.Entity;
import com.example.Entity.NonPlayerEntity;

public class NoHealTargeting extends HealTargeting {
    public NoHealTargeting(NonPlayerEntity self) {
        super(self);
    }

    @Override
    public List<Entity> getRankedTargets() {
        return List.of();
    }
}
