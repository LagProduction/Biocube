package com.lagadd.biocubes.common.entites.ai;

import com.lagadd.biocubes.common.entites.CryodonEntity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.InteractionHand;

public class PickUpAttackGoal extends MeleeAttackGoal {

	    private CryodonEntity cryo;

	    public PickUpAttackGoal(CryodonEntity cryo, double speedIn, boolean useLongMemory) {
	        super(cryo, speedIn, useLongMemory);
	        this.cryo = cryo;
	    }

	    public boolean canUse() {

	        return super.canUse() && cryo.getPassengers().isEmpty();
	    }

	    public boolean canContinueToUse() {
	        return super.canContinueToUse() && cryo.getPassengers().isEmpty();
	    }

	    protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
	        double d0 = this.getAttackReachSqr(enemy);
	        if (distToEnemySqr <= d0) {
	            this.resetAttackCooldown();
	            this.mob.swing(InteractionHand.MAIN_HAND);
	            this.mob.doHurtTarget(enemy);
	        }
	    }
}