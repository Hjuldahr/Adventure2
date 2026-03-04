package com.example.Entity;

import java.util.ArrayList;

import com.example.Combat.Attack;
import com.example.Combat.CombatContext;
import com.example.Combat.Spell;
import com.example.Items.Inventory;

public class CompanionEntity extends NonPlayerEntity {
    // TODO add recruitment mech
    private Inventory inventory = new Inventory(10);
    
    public CompanionEntity(EntityCategory entityCategory, Role role, int level, String name, int maxHP, int speed, Attack singleTargetAttack, Attack aoeTargetAttack, ArrayList<Spell> spells) {
        super(entityCategory, RoleCategory.COMPANION, level, role, name, maxHP, speed, singleTargetAttack, aoeTargetAttack, spells);
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