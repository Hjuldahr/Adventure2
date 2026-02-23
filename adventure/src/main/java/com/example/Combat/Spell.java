package com.example.Combat;

import java.util.List;

import com.example.Entity.Entity;
import com.example.Entity.NonPlayerEntity;
import com.example.Entity.Entity.RoleCategory;

public class Spell {
    public enum SpellType { CREATION, DESTRUCTION, PROTECTION, RESTORATION, ALTERATION } //player facing
    public enum SpellNPCUsage { ATTACK, AOE_ATTACK, HEAL_SELF, HEAL_TARGET, HEAL_OTHER, AOE_HEAL, FORTIFY_SELF, FORTIFY_TARGET, WEAKEN_TARGET, RESURRECT } //ai behaviour
    
    private SpellType type;
    private SpellNPCUsage NPCUsage;

    private String name;
    private int MPCost;
    private int potency;

    public Spell(String name, int MPCost, int potency, SpellType spellType) {
        this(name, MPCost, potency, spellType, null);
    }

    public Spell(String name, int MPCost, int potency, SpellNPCUsage NPCUsage) {
        this(name, MPCost, potency, null, NPCUsage);
    }

    private Spell(String name, int MPCost, int potency, SpellType type, SpellNPCUsage NPCUsage) {
        this.name = name;
        this.MPCost = MPCost;
        this.potency = potency;
        this.type = type;
        this.NPCUsage = NPCUsage;
    }

    public SpellType getType() {
        return type;
    }

    public SpellNPCUsage getNPCUsage() {
        return NPCUsage;
    }

    public String getName() {
        return name;
    }

    public int getCost() {
        return MPCost;
    }

    public int getPotency() {
        return potency;
    }

    public boolean executeMany(List<Entity> targets) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'execute'");
    }

    public boolean execute(Entity first) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'executeMany'");
    }

    public boolean canHeal(NonPlayerEntity caster, Entity target) {
        RoleCategory casterRole = caster.getRoleCategory();
        RoleCategory targetRole = target.getRoleCategory();

        if (casterRole != targetRole || !(casterRole == RoleCategory.COMPANION && targetRole == RoleCategory.PLAYER)) {
            return false;
        }
        if (casterRole == RoleCategory.WILDCARD && !caster.equals(target)) {
            return false;
        }
        
        switch (NPCUsage) {
            case HEAL_SELF: return caster.equals(target);
            case HEAL_OTHER: return !caster.equals(target);
            case HEAL_TARGET: return true;
            case AOE_HEAL: return true;
            default: return false;
        }
    }

    public boolean canAttack(NonPlayerEntity caster, Entity target) {
        RoleCategory casterRole = caster.getRoleCategory();
        RoleCategory targetRole = target.getRoleCategory();

        if (casterRole == targetRole || (casterRole == RoleCategory.COMPANION && targetRole == RoleCategory.PLAYER)) {
            return false;
        }
        if (caster.equals(target)) {
            return false;
        }
        return true;
    }

    public Attack getAttack() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAttack'");
    }
}