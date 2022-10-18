package com.lagadd.biocubes.common.entities.creatures.sixgill;


import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;

//TODO not yet ready
public class SixgillGrabGoal extends MeleeAttackGoal {

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
        return super.canUse() && this.sixgill.getPassengers().isEmpty();
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
        boolean isGrabBlocked = this.rayTrace(this.sixgill, enemy.position().distanceTo(this.sixgill.position()), 1.0F).getType() == HitResult.Type.BLOCK;

        if (distToEnemySqr <= attackReachSqr && this.getTicksUntilNextAttack() <= 0) {
            enemy.startRiding(this.sixgill, true);
            this.sixgill.setTarget(null);
        }
    }

    @Override
    protected double getAttackReachSqr(LivingEntity attackTarget) {
        return super.getAttackReachSqr(attackTarget) * 0.55F;
    }

    public static HitResult rayTrace(Sixgill entity, double distance, float delta) {
        return entity.level.clip(new ClipContext(
                entity.getEyePosition(delta),
                entity.getEyePosition(delta).add(entity.getViewVector(delta).scale(distance)),
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                entity
        ));
    }


}
