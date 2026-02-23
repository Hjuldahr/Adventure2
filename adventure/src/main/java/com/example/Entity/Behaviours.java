package com.example.Entity;

import com.example.Combat.CombatContext;

interface TargetingBehavior { Entity[] selectTargets(Entity self, CombatContext context); }
interface HealingBehavior { Entity[] selectHealingTargets(Entity self, CombatContext context); }
