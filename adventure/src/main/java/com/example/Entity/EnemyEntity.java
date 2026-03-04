package com.example.Entity;

import java.util.ArrayList;

import com.example.Combat.Attack;
import com.example.Combat.CombatContext;
import com.example.Combat.Spell;

public class EnemyEntity extends NonPlayerEntity {
    // TODO add loot mech
    
    public EnemyEntity(EntityCategory entityCategory, Role role, int level, String name, int maxHP, int speed, Attack singleTargetAttack, Attack aoeTargetAttack, ArrayList<Spell> spells) {
        super(entityCategory, RoleCategory.ENEMY, level, role, name, maxHP, speed, singleTargetAttack, aoeTargetAttack, spells);
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
