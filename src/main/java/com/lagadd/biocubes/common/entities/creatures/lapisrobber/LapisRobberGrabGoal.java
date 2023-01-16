package com.lagadd.biocubes.common.entities.creatures.lapisrobber;


import com.lagadd.biocubes.common.entities.creatures.sixgill.Sixgill;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;

//TODO not yet ready
public class LapisRobberGrabGoal extends MeleeAttackGoal {

    private final LapisRobberEntity lapis;

    public LapisRobberGrabGoal(LapisRobberEntity lapis, double speedIn, boolean useLongMemory) {
        super(lapis, speedIn, useLongMemory);
        this.lapis = lapis;
    }

    @Override
    public boolean canUse() {
        LivingEntity attackTarget = this.lapis.getTarget();
        if (attackTarget != null && attackTarget.isPassenger()) {
            if (attackTarget.getVehicle() instanceof LapisRobberEntity) {
                return false;
            }
        }
        return super.canUse() && this.lapis.getPassengers().isEmpty();
    }

    @Override
    public void start() {
        this.lapis.setGrabbing(true);
    }

    @Override
    public void stop() {
        super.stop();
        this.lapis.setGrabbing(true);
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity attackTarget = this.lapis.getTarget();
        if (attackTarget != null && attackTarget.isPassenger()) {
            if (attackTarget.getVehicle() instanceof LapisRobberEntity) {
                return false;
            }
        }
        return super.canContinueToUse() && this.lapis.getPassengers().isEmpty();
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
        double attackReachSqr = this.getAttackReachSqr(enemy);
        if (distToEnemySqr <= attackReachSqr + 0.75F && this.getTicksUntilNextAttack() <= 0) {
            //
        }
        boolean isGrabBlocked = this.rayTrace(this.lapis, enemy.position().distanceTo(this.lapis.position()), 1.0F).getType() == HitResult.Type.BLOCK;

        if (distToEnemySqr <= attackReachSqr && this.getTicksUntilNextAttack() <= 0) {
            enemy.startRiding(this.lapis, true);
            this.lapis.setTarget(null);
        }
    }

    @Override
    protected double getAttackReachSqr(LivingEntity attackTarget) {
        return super.getAttackReachSqr(attackTarget) * 0.55F;
    }

    public static HitResult rayTrace(LapisRobberEntity entity, double distance, float delta) {
        return entity.level.clip(new ClipContext(
                entity.getEyePosition(delta),
                entity.getEyePosition(delta).add(entity.getViewVector(delta).scale(distance)),
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                entity
        ));
    }


}
