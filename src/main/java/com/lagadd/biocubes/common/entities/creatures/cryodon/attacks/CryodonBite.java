package com.lagadd.biocubes.common.entities.creatures.cryodon.attacks;

import com.lagadd.biocubes.common.entities.creatures.cryodon.Cryodon;
import com.lagadd.biocubes.common.entities.creatures.cryodon.CryodonAttacks;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class CryodonBite extends Goal {

    private Cryodon cryodon;

    public CryodonBite(Cryodon cryodon) {
        this.cryodon = cryodon;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.cryodon.hasAnimationRunning() && this.cryodon.getAnimation() != CryodonAttacks.BITE.getId()) {
            return false;
        }
        return this.cryodon.getAnimation() == CryodonAttacks.BITE.getId();
    }

    @Override
    public boolean canContinueToUse() {
        return this.cryodon.getAnimationframe() < CryodonAttacks.BITE.getMaxframe();
    }

    @Override
    public void start() {
        System.out.println("Cryodon Bite");
    }

    @Override
    public void stop() {
        this.cryodon.getNavigation().stop();
        this.cryodon.setAnimation(0);
    }

    @Override
    public void tick() {
        System.out.println("Running Animation for Cryodon No: " + this.cryodon.getAnimation() + "with Ticks at " + this.cryodon.getAnimationframe());
        this.cryodon.runAnimTick();
        var target = this.cryodon.getTarget();
        if (target != null) {
            this.cryodon.getLookControl().setLookAt(target, 30F, 30F);
            if (this.cryodon.getAnimationframe() == CryodonAttacks.BITE.getDamageframe()) {
                this.cryodon.doHurtTarget(target);
            }
        }
    }
}
