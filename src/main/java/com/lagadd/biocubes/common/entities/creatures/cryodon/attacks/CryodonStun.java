package com.lagadd.biocubes.common.entities.creatures.cryodon.attacks;

import com.lagadd.biocubes.common.entities.creatures.cryodon.Cryodon;
import com.lagadd.biocubes.common.entities.creatures.cryodon.CryodonAttacks;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;
import java.util.List;

public class CryodonStun extends Goal {
    private Cryodon cryodon;
    private float range;
    private float height;

    public CryodonStun(Cryodon cryodon, float range, float height) {
        this.cryodon = cryodon;
        this.range = range;
        this.height = height;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.cryodon.hasAnimationRunning() && this.cryodon.getAnimation() != CryodonAttacks.STUN.getId()) {
            return false;
        }
        return this.cryodon.getAnimation() == CryodonAttacks.STUN.getId();
    }

    @Override
    public boolean canContinueToUse() {
        return this.cryodon.getAnimationframe() < CryodonAttacks.STUN.getMaxframe();
    }

    @Override
    public void start() {
        System.out.println("Cryodon Stun");
    }

    @Override
    public void stop() {
        this.cryodon.getNavigation().stop();
        this.cryodon.setAnimation(0);
    }

    @Override
    public void tick() {
        this.cryodon.runAnimTick();
        System.out.println("Running Animation for Cryodon No: " + this.cryodon.getAnimation() + " with Ticks at " + this.cryodon.getAnimationframe());
        if (this.cryodon.getTarget() != null) {
            if (this.cryodon.getAnimationframe() == CryodonAttacks.STUN.getDamageframe()) {
                this.cryodon.hitEntities(range, height);
                List<LivingEntity> slowList = this.cryodon.getEntitiesNearby(LivingEntity.class, range, height, range, range);
                for (LivingEntity entity : slowList) {
                    if (entity instanceof Cryodon) continue;
                    entity.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 80, 3), this.cryodon);
                    entity.push(1.0D, 1.0D, 1.0D);
                }
            }
        }
    }
}
