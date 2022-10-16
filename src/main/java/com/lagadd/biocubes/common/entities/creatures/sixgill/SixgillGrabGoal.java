package com.lagadd.biocubes.common.entities.creatures.sixgill;


import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

//TODO not yet ready
public class SixgillGrabGoal  extends MeleeAttackGoal {

    private final Sixgill sixgill;

    public SixgillGrabGoal(Sixgill sixgill, double speedIn, boolean useLongMemory) {
        super(sixgill, speedIn, useLongMemory);
        this.sixgill = sixgill;
    }

    @Override
    public boolean canUse() {
        LivingEntity attackTarget = this.sixgill.getTarget();
        if (attackTarget != null && attackTarget.isPassenger()) {
            if (attackTarget.getVehicle() instanceof Sixgill) {
                return false;
            }
        }
        return  super.canUse() && this.sixgill.getPassengers().isEmpty();
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity attackTarget = this.sixgill.getTarget();
        if (attackTarget != null && attackTarget.isPassenger()) {
            if (attackTarget.getVehicle() instanceof Sixgill) {
                return false;
            }
        }
        return super.canContinueToUse() && this.sixgill.getPassengers().isEmpty();
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
        double attackReachSqr = this.getAttackReachSqr(enemy);
        if (distToEnemySqr <= attackReachSqr + 0.75F && this.getTicksUntilNextAttack() <= 0) {
            //
        }


        if (distToEnemySqr <= attackReachSqr &&  this.getTicksUntilNextAttack() <= 0) {
            enemy.startRiding(this.sixgill, true);
            this.sixgill.setTarget(null);
        }
    }

    @Override
    protected double getAttackReachSqr(LivingEntity attackTarget) {
        return super.getAttackReachSqr(attackTarget) * 0.55F;
    }


}
