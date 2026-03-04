package com.example.Entity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.example.Combat.Attack;
import com.example.Combat.CombatContext;
import com.example.Combat.Spell;
import com.example.Combat.Spell.SpellNPCUsage;
import com.example.Entity.Behaviours.*;

public abstract class NonPlayerEntity extends Entity {
    /*
     * The goal is to keep it simple and self contained
     * Both enemies and allies
     * Slayer (Attack): lowest current hp -> lowest max hp -> lowest level
     * Hunter (Attack) : highest max hp -> lowest current hp -> highest level
     * Berzerker (Attack) highest damage *taken* per enemy (uses decaying aggro map) => random
     * Assassin (Attack) priotizes by (total damage output + total healing output * 1.5) *dealt* per enemy (uses non-decaying aggro map - because it "pursues" targets) => lowest current HP -> highest level 
     * Battle_Healer
     *   1. if self has % HP < 50%, perform highest HP heal spell on self not on cooldown
     *   2. If > 1 ally has % HP < 50%, perform highest HP Group heal spell not on cooldown 
     *   3. If all group heal spells are on cooldown perform highest HP heal spell not on cooldown on member with lowest HP %.  
     *   4. If exactly 1 ally has % HP < 50%, perform highest HP heal spell on target not on cooldown
     *   5. Fallback to Berzerker logic.
     * Support_Healer
     *   1. if self has % HP < 10%, perform highest HP heal spell on self not on cooldown
     *   2. If > 1 ally has % HP < 90%, perform highest HP Group heal spell not on cooldown 
     *   3. If > 1 ally has % HP < 90% & all group heal spells are on cooldown perform highest HP heal spell not on cooldown on member with lowest HP %.  
     *   4. If exactly 1 ally has % HP < 90%, perform highest HP heal spell on target not on cooldown
     *   5. If no heals or healable individuals are available, skip turn.
     */
    public enum Role { SLAYER, HUNTER, BERZERKER, ASSASSIN, BATTLE_HEALER, SUPPORT_HEALER }
    protected Role role;

    protected HashMap<Long,Float> aggroMap = new HashMap<>();

    protected Attack singleTargetAttack;
    protected Attack aoeTargetAttack;

    private AttackTargeting attackTargeting;
    private HealTargeting healTargeting;
    
    protected NonPlayerEntity(EntityCategory entityCategory, RoleCategory roleCategory, int level, Role role, String name, int maxHP, int speed, Attack singleTargetAttack, Attack aoeTargetAttack, ArrayList<Spell> spells) {
        super(entityCategory, roleCategory, level, name, maxHP, speed, spells);

        this.role = role;
        this.singleTargetAttack = singleTargetAttack;
        this.aoeTargetAttack = aoeTargetAttack;

        // Simplifies Composition by common associations (can be manually replaced by unique targeting if needed)
        attackTargeting = switch(role) {
            case SLAYER -> new SlayerAttackTargeting(this);
            case HUNTER -> new HunterAttackTargeting(this);
            case ASSASSIN -> new AssassinAttackTargeting(this);
            case SUPPORT_HEALER -> new NoAttackTargeting(this); 
            default -> new BerzerkerAttackTargeting(this); // berzeker & battle_healer
        };
        healTargeting = switch(role) {
            case BATTLE_HEALER -> new BattleHealerHealTargeting(this);
            case SUPPORT_HEALER -> new SupportHealerHealTargeting(this);
            default -> new NoHealTargeting(this);
        };
    }

    @Override
    public void onCombatStart(CombatContext context) {
        super.onCombatStart(context);

        context.getActors().values().forEach(
            e -> aggroMap.put(e.getId(), 0f)
        );
    }

    @Override
    public void onCombatTurn() {
        super.onCombatTurn();

        defenceStance = ProtectionStance.NONE;

        // Attempt to heal
        List<Entity> entities = healTargeting.getRankedTargets();
        if (!entities.isEmpty()) {
            boolean hasHealed = attemptHealSpell(entities);

            if (hasHealed) { //if successfully healed, end turn
                return;
            }
        }

        // if they cannot heal (no target, not enough MP, wrong role)
        entities = attackTargeting.getRankedTargets();
        if (!entities.isEmpty()) {
            boolean hasAttacked = attemptAttackSpell(entities);
            if (hasAttacked) { //if successfully spell attacked, end turn
                return;
            }

            hasAttacked = attemptAutoAttack(entities);
            if (hasAttacked) { //if successfully auto attacked, end turn
                return;
            }
        }

        // fallback, (needs extra logic to be tactically relevant)
        // evading has a chance of failure but is more protective when it works
        // defending doesnt have a chance of failure but is only a marginal reduction
        // health based to mirror exhaustion dynamics, as evading would be more tiring than blocking or bracing for a hit
        defenceStance = hitPoints.getRatio() > 0.25 ? ProtectionStance.EVADING : ProtectionStance.DEFENDING;
    }

    private boolean attemptAttackSpell(List<Entity> targets) {
        if (targets.isEmpty() || spells.isEmpty()) return false;

        // If multiple targets, try AoE first
        if (targets.size() > 1) {
            Optional<Spell> aoeSpell = spells.stream()
                .filter(s -> s.getNPCUsage() == SpellNPCUsage.AOE_ATTACK && s.getCost() <= magicPoints.getValue())
                // Choose the AoE that overshoots least for the most HP (sum of overkill penalties)
                .min(Comparator.comparingInt(s ->
                    targets.stream()
                        .mapToInt(t -> Math.max(0, s.getPotency() - t.getHP().getValue()))
                        .sum()
                ));

            if (aoeSpell.isPresent()) {
                int results = 0;
                for (Entity target : targets) {
                    results += performSpellAttack(target, aoeSpell.get()) ? 1 : 0;
                }
                return results > 0;
            }
        }

        // Single-target attack
        Entity target = targets.get(0);
        Optional<Spell> singleSpell = spells.stream()
            .filter(s -> s.canAttack(this, target) && s.getCost() <= magicPoints.getValue())
            // Pick spell that overshoots least; if all underkill, maximize damage
            .min(Comparator.comparingInt(s -> {
                int diff = s.getPotency() - target.getHP().getValue();
                return diff >= 0 ? diff : -diff; // overkill positive, underkill negative
            }));

        return singleSpell.map(spell -> performSpellAttack(target, spell)).orElse(false);
    }

    private boolean attemptAutoAttack(List<Entity> targets) {
        if (targets.isEmpty()) return false;

        // If multiple targets, try AoE first
        if (targets.size() > 1 && aoeTargetAttack != null) {
            int results = 0;
            for (Entity target : targets) {
                results += performAttack(target, aoeTargetAttack) ? 1 : 0;
            }
            return results > 0;
        }

        // Single-target attack (first in sorted list)
        Entity target = targets.getFirst();
        return performAttack(target, singleTargetAttack);
    }

    private boolean attemptHealSpell(List<Entity> targets) {
        if (targets.isEmpty() || spells.isEmpty()) return false;

        // If multiple targets, try AoE first
        if (targets.size() > 1) {
            Optional<Spell> aoeSpell = spells.stream()
                .filter(s -> s.getNPCUsage() == SpellNPCUsage.AOE_HEAL && s.getCost() <= magicPoints.getValue())
                // Prefer spell that minimizes overheal across all targets
                .min(Comparator.comparingInt(s ->
                    targets.stream()
                        .mapToInt(t -> Math.max(0, s.getPotency() - t.getHP().getMaxValue() + t.getHP().getValue()))
                        .sum()
                ));

            if (aoeSpell.isPresent()) {
                //return aoeSpell.get().executeMany(targets);
                int results = 0;
                for (Entity target : targets) {
                    results += performHeal(target, aoeSpell.get()) ? 1 : 0;
                }
                return results > 0;
            }
        }

        // Single-target heal
        Entity target = targets.get(0);
        Optional<Spell> singleSpell = spells.stream()
            .filter(s -> s.canHeal(this, target) && s.getCost() <= magicPoints.getValue())
            // minimize overheal; if no overheal possible, heal as much as possible
            .min(Comparator.comparingInt(s -> {
                int missingHP = target.getHP().getMaxValue() - target.getHP().getValue();
                int diff = s.getPotency() - missingHP;
                return diff >= 0 ? diff : -diff;
            }));

        return singleSpell.map(spell -> performHeal(target, spell)).orElse(false);
    }

    public Map<Long,Float> getAggroMap() {
        return aggroMap;
    }
}
