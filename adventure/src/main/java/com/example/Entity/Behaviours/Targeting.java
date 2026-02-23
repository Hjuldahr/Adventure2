package com.example.Entity.Behaviours;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.example.Entity.Entity;
import com.example.Entity.NonPlayerEntity;

public abstract class Targeting {
    protected NonPlayerEntity self;

    protected Targeting(NonPlayerEntity self) {
        this.self = self;
    }

    public abstract List<Entity> getRankedTargets();

    protected Stream<Entity> validTargetsStream() {
        return getValidTargets().values().stream();
    }

    abstract protected Map<Long,Entity> getValidTargets();
}
