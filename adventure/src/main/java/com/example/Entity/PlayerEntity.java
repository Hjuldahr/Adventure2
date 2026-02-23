package com.example.Entity;

import com.example.Combat.CombatContext;
import com.example.Combat.Spell;
import com.example.Items.Armour;
import com.example.Items.Consumable;
import com.example.Items.PlayerArmoury;
import com.example.Utility.Clamped;
import com.example.Utility.Input;
import com.example.Utility.Timer;

public class PlayerEntity extends Entity {
    private int goldPieces = 0;

    private PlayerArmoury armoury = new PlayerArmoury();

    private Timer potionSicknessTimer = new Timer();

    private final int POTION_TOLERANCE = 3;
    private final int NAV_HP_REGEN = 5;
    
    public PlayerEntity() {
        super(EntityCategory.HUMAN, RoleCategory.PLAYER, 1, "Sylvie", 100, 10);
    }

    public Clamped getMP() {
        return magicPoints;
    }

    public void equipArmour(Armour newArmour)
    {
        armoury.equipArmour(newArmour);
        updateBaseStats();
    }

    private void updateBaseStats() {
        //replace 0 with base constant if intrinsic defence is needed (i.e. if ability score derivation get added)
        defencePower = 0; 

        for (Armour armour : armoury.getEquippedArmour()) {
            defencePower += armour.getDefencePower();
            //add other aggregates as needed (weight, evasion mods, hp mods, mp mods, etc)
        }
    }

    public void consumePotion(Consumable potion) {
        if (potionSicknessTimer.get() < POTION_TOLERANCE) {
            hitPoints.increment(potion.getHitPointChange());
            magicPoints.increment(potion.getManaPointChange());
            potionSicknessTimer.increment();
        }
    }

    @Override
    public void onCombatStart(CombatContext context) {
        super.onCombatStart(context);

        magicPoints.minimize();
        potionSicknessTimer.reset();
    }

    @Override
    public void onCombatTurn() {
        super.onCombatTurn();

        System.out.println("OPTIONS: [1] ATTACK [2] DEFEND [3] EVADE [4] CAST [5] USE");
        int option = Input.getInt("> ", 1, 4);
        switch(option) {
            case 1 -> useAttack();
            case 2 -> useDefend();
            case 3 -> useEvade();
            case 4 -> useSpell();
            case 5 -> useItem();
        }
    }

    protected void useAttack() {
        Entity target = null;
        
        while (target == null) {
            System.out.printf("%s using [ATTACK]\n", name);
            int targetNumber = Input.getInt("TARGET NUMBER > ", 1, 100);

            target = currentCombatContext.getActorByTargetNumber(targetNumber);

            if (target == null) {
                System.out.printf("No Combatant has TARGET NUMBER %d\n", targetNumber);
            }
        }
        
        performAttack(target, armoury.getEquippedWeapon().getAttack());
    }

    protected void useDefend() {
        System.out.printf("%s used [DEFEND]\nDefence Raised +%f%%\n", name, (DEFEND_STANCE_MOD - 1f) * 100f);

        defenceStance = ProtectionStance.DEFENDING;
    }

    protected void useEvade() {
        System.out.printf("%s used [EVADE]\nDodge Chance Raised +%f%%\n", name, (EVADE_STANCE_MOD - 1f) * 100f);

        defenceStance = ProtectionStance.EVADING;
    }

    protected void useSpell() {

    }

    protected void useItem() {
        for (int spellNumber = 1; spellNumber < spells.size(); spellNumber++) {
            Spell spell = spells.get(spellNumber - 1);
            System.out.printf("%d %S %s %d MP\n", spellNumber, spell.getType(), spell.getName(), spell.getCost());
        }
    }

    @Override
    public void onCombatEnd() {
        super.onCombatEnd();
    }

    public int getGold() {
        return goldPieces;
    }

    //next room
    public void onMove() {
        hitPoints.increment(NAV_HP_REGEN);
    }

    
}