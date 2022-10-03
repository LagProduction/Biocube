package com.lagadd.biocubes.common.entities.ai;

import com.lagadd.biocubes.common.entities.BiocubeCreature;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Path;

import java.util.EnumSet;

public class AnimatedAttackGoal extends Goal {

    private BiocubeCreature creature;
    private double speedfactor;
    private double limitdistance;
    private long lastcanusecheck;
    private Path path;
    private int pathingcooldown;

    public AnimatedAttackGoal(BiocubeCreature creature, double speedfactor, double limitdistance) {
        this.creature = creature;
        this.speedfactor = speedfactor;
        this.limitdistance = limitdistance;
        //In this Goal we want to make the mob to keep looking to its target and moving at the same time (for test)
        this.setFlags(EnumSet.of(Flag.TARGET, Flag.LOOK, Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        var wyverntime = this.creature.level.getGameTime();
        var gametime = wyverntime - this.lastcanusecheck;
        if (gametime < 20L) {
            return false;
        } else {
            this.lastcanusecheck = wyverntime;
            var target = this.creature.getTarget();
            if (target == null) {
                return false;
            } else if (!target.isAlive() || target.getHealth() <= 0) {
                return false;
            } else {
                if (--this.pathingcooldown <= 0) {
                    this.path = this.creature.getNavigation().createPath(target, 0);
                    this.pathingcooldown = 4 + this.creature.getRandom().nextInt(7);
                    return this.path != null;
                } else {
                    this.path = this.creature.getNavigation().createPath(target, 0);
                    if (this.path != null) {
                        return true;
                    }
                    return this.creature.getReachRange(target) >= this.creature.distanceToSqr(target.getX(), target.getY(), target.getZ());
                }
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        var target = this.creature.getTarget();
        if (target == null) {
            return false;
        } else if (!target.isAlive()) {
            return false;
        } else if (!this.creature.isWithinRestriction(target.blockPosition())) {
            return false;
        } else {
            return !(target instanceof Player) || !target.isSpectator() && !((Player) target).isCreative();
        }

    }

    @Override
    public void start() {
        this.creature.getNavigation().moveTo(this.path, this.speedfactor);
        this.creature.setAggressive(true);
        this.pathingcooldown = 0;
        System.out.println("AI is functioning");
    }

    @Override
    public void stop() {
        var target = this.creature.getTarget();
        if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(target)) {
            this.creature.setTarget((LivingEntity) null);
        }
        this.creature.setAggressive(false);
        this.creature.getNavigation().stop();
    }

    @Override
    public void tick() {
        var target = this.creature.getTarget();
        if (target != null) {
            var dist = this.creature.distanceToSqr(target.getX(), target.getY(), target.getZ());
            this.creature.getLookControl().setLookAt(target, 30.0F, 30.0F);
            this.pathingcooldown = Math.max(this.pathingcooldown - 1, 0);
            if (this.pathingcooldown <= 0) {
                this.pathingcooldown = 4 + this.creature.getRandom().nextInt(7);
                if (!this.creature.getNavigation().moveTo(target, this.speedfactor)) {
                    this.pathingcooldown += 15;
                }
            }
            this.pathingcooldown = this.adjustedTickDelay(this.pathingcooldown);
            this.attackCheck(target, dist);
        }
    }

    protected void attackCheck(LivingEntity entity, double reach) {
        var dis = this.creature.getReachRange(entity);
        if (reach <= dis) {
            this.creature.doAnimatedAttack();
        }
    }

}
