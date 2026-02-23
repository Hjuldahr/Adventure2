package com.example.Entity;

import com.example.Utility.Clamped;
import com.example.Utility.IdGenerator;
import com.example.Utility.Randomizer;

import java.util.ArrayList;

import com.example.Combat.Attack;
import com.example.Combat.CombatContext;
import com.example.Combat.Spell;

public abstract class Entity {
    public enum RoleCategory { PLAYER, COMPANION, ENEMY, WILDCARD }
    public enum ProtectionStance { NONE, DEFENDING, EVADING }
    public enum EntityCategory { HUMAN, ANIMAL, UNDEAD, GOLEM, ANGEL, FIEND }

    protected EntityCategory entityCategory;
    protected RoleCategory roleCategory;
    protected ProtectionStance defenceStance = ProtectionStance.NONE;

    protected final float DEFEND_STANCE_MOD = 1.10f;
    protected final float EVADE_STANCE_MOD = 1.25f;
    protected final float EVADE_STANCE_PENALTY_MOD = 0.9f;

    protected String name;

    protected Clamped hitPoints;

    protected Clamped magicPoints = new Clamped(0, 100);
    protected int magicRegenerationRate = 1;

    protected int level;
    protected int attackPower;
    protected int defencePower;
    protected float dodgeChance;
    protected int magicPower;
    protected int speed;

    protected float accuracy;
    protected float evasion;

    protected ArrayList<Spell> spells = new ArrayList<>();

    protected CombatContext currentCombatContext = null;
    protected long totalDamageDealt = 0l;
    protected long totalHealingDealt = 0l;

    protected final long id = IdGenerator.next(IdGenerator.IdTypes.ENTITY);
    
    protected Entity(EntityCategory entityCategory, RoleCategory roleCategory, int level, String name, int maxHP, int speed) {
        this.entityCategory = entityCategory;
        this.roleCategory = roleCategory;
        this.level = level;
        this.hitPoints = new Clamped(maxHP, maxHP);
        this.name = name;
        this.speed = speed;
    }

    public int getLevel() { return level; }
    public Long getId() { return id; }
    public String getName() { return name; }
    public Clamped getHP() { return hitPoints; }
    public int getAttackPower() { return attackPower; }
    public int getDefencePower() { return defencePower; }
    public int getMagicPower() { return magicPower; }
    public int getSpeed() { return speed; }
    public float getEvasion() { return evasion; }
    public ProtectionStance getDefenceStance() { return defenceStance; }
    public long getTotalDamageDealt() { return totalDamageDealt; }
    public long getTotalHealingDealt() { return totalHealingDealt; }
    public double getAssassinMetric(double damageWeight, double healingWeight) { 
        return totalDamageDealt * damageWeight + totalHealingDealt * healingWeight;
    }

    public boolean isDead() { return hitPoints.atMinimum(); }

    public void onCombatStart(CombatContext context) { 
        currentCombatContext = context; 
        magicPoints.minimize();
    }

    public void onCombatTurn() {
        magicPoints.increment(magicRegenerationRate);
    }

    public void onCombatEnd() {
        currentCombatContext = null;
        totalDamageDealt = 0l;
        totalHealingDealt = 0l;
    }

    public boolean performAttack(Entity target, Attack attack) {
        ProtectionStance targetStance = target.getDefenceStance();
        float evasionModifier = (targetStance == ProtectionStance.EVADING) ? EVADE_STANCE_MOD : 1f;
        float dodgeSuccess = Randomizer.getFloat(target.getEvasion() * evasionModifier);
        float hitThreshold = accuracy;

        if (dodgeSuccess <= hitThreshold) {
            float defenceModifier = switch(targetStance) {
                case DEFENDING -> DEFEND_STANCE_MOD;
                case EVADING -> EVADE_STANCE_PENALTY_MOD;
                case NONE -> 1f;
            };
            // deal more damage the closer you are to max accuracy
            float normalized = 1f - (dodgeSuccess / accuracy);
            float power = 0.5f + 0.5f * normalized * normalized; // scales 0.5 -> 1
            
            int damage = Math.round(Math.max(1f, (attackPower + attack.getAttackPower() - defencePower * defenceModifier) * power));

            if (normalized >= 0.99f) {
                // doesnt change anything, just indicates goodluck
                System.out.print("PERFECT ");
            }
            System.out.printf("HIT! %d DMG Dealt%n", damage);

            target.getHP().decrement(damage);
            return true;

        } else {
            System.out.println("MISS! 0 DMG Dealt");
            return false;
        }
    }

    //no defence mitigation, reliable hits once honed in, costs MP
    public boolean performSpellAttack(Entity target, Spell spell) {
        if (magicPoints.decrement(spell.getCost())) {
            
            ProtectionStance targetStance = target.getDefenceStance();

            float defenceModifier = switch(targetStance) {
                case DEFENDING -> DEFEND_STANCE_MOD;
                case EVADING -> 1f;
                case NONE -> 1f;
            };
            int damage = Math.round(Math.max(1f, (magicPower + spell.getPotency() - defencePower * defenceModifier)));

            System.out.printf("%d DMG Dealt%n", damage);

            target.getHP().decrement(damage);
            return true;
        }
        return false;
    }

    public boolean performHeal(Entity target, Spell spell) {
        if (magicPoints.decrement(spell.getCost())) {
            int healing = magicPower + spell.getPotency();

            System.out.printf("%d DMG Dealt%n", healing);

            return target.getHP().increment(healing);
        }
        return false;
    }

    public RoleCategory getRoleCategory() {
        return roleCategory;
    }

    public EntityCategory getEntityCategory() {
        return entityCategory;
    }

    public CombatContext getCombatContext() {
        return currentCombatContext;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Entity e) {
            return this.id == e.id;
        }
        return false;
    }
}