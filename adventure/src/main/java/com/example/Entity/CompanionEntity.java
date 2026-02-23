package com.example.Entity;

import com.example.Combat.CombatContext;

public class CompanionEntity extends NonPlayerEntity {
    public CompanionEntity(EntityCategory entityCategory, Role role, int level, String name, int maxHP, int speed) {
        super(entityCategory, RoleCategory.COMPANION, level, role, name, maxHP, speed);
    }

    @Override
    public void onCombatStart(CombatContext context) {
        super.onCombatStart(context);
    }

    @Override
    public void onCombatTurn() {
        super.onCombatTurn();
    }

    @Override
    public void onCombatEnd() {
        super.onCombatEnd();
    }
}
