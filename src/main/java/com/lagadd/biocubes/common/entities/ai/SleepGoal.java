package com.lagadd.biocubes.common.entities.ai;

import com.lagadd.biocubes.common.entities.BiocubeCreature;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class SleepGoal extends Goal {

    private BiocubeCreature creature;
    private float heal;

    public SleepGoal(BiocubeCreature creature, float heal) {
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
        this.creature = creature;
        this.heal = heal;
    }

    @Override
    public boolean canUse() {
        var onPlace = this.creature.xxa == 0.0F && this.creature.yya == 0.0F && this.creature.zza == 0.0F;
        if (onPlace) {
            return this.creature.canSleep();
        } else {
            return false;
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.creature.canSleep();
    }

    public void start() {
        this.creature.setJumping(false);
        this.creature.setSleeping(true);
        this.creature.getNavigation().stop();
        this.creature.getMoveControl().setWantedPosition(this.creature.getX(), this.creature.getY(), this.creature.getZ(), 0.00D);
        System.out.println("Test Sleep");
    }

    @Override
    public void stop() {
        this.creature.setSleeping(false);
    }

    @Override
    public void tick() {
        if (this.creature.getHealth() < this.creature.getMaxHealth()) {
            this.creature.heal(heal);
        }
    }
}


