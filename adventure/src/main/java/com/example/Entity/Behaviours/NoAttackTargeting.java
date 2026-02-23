package com.example.Entity.Behaviours;

import java.util.List;

import com.example.Entity.Entity;
import com.example.Entity.NonPlayerEntity;

public class NoAttackTargeting extends AttackTargeting
{
    public NoAttackTargeting(NonPlayerEntity self) {
        super(self);
    }

    @Override
    public List<Entity> getRankedTargets() {
        return List.of();
    }
}
