package com.lagadd.biocubes.common.entities.creatures.sixgill;

import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;

import java.util.EnumSet;

public class SixgillCrushBiteGoal extends Goal {
    public Sixgill sixgill;
    private float originalYaw;
    private float biteTicks;

    public SixgillCrushBiteGoal(Sixgill Sixgill) {
        this.sixgill = Sixgill;
        this.setFlags(EnumSet.of(Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        Entity passenger = !sixgill.getPassengers().isEmpty() ? this.sixgill.getPassengers().get(0) : null;
        if (passenger instanceof Player) {
            if (((Player) passenger).isCreative() || passenger.isSpectator()) {
                return false;
            }
        }
        return passenger != null && this.sixgill.getRandom().nextFloat() < 0.1F;
    }

    @Override
    public boolean canContinueToUse() {
        Entity passenger = this.sixgill.getFirstPassenger();
        if (passenger instanceof Player) {
            if (((Player) passenger).isCreative() || passenger.isSpectator()) {
                return false;
            }
        }
        return this.biteTicks <= 55 && passenger != null;
    }

    @Override
    public void start() {
        this.originalYaw = this.sixgill.getYRot();
    }

    @Override
    public void stop() {
        this.originalYaw = 0;
        this.biteTicks = 0;
    }

    @Override
    public void tick() {
        this.biteTicks++;

        this.sixgill.getNavigation().stop();

        this.sixgill.yRotO = this.sixgill.getYRot();

        this.sixgill.yBodyRot = (this.originalYaw) + 75 * Mth.cos(this.sixgill.tickCount * 0.5F) * 1F;
        this.sixgill.setYRot((this.originalYaw) + 75 * Mth.cos(this.sixgill.tickCount * 0.5F) * 1F);

        Entity entity = this.sixgill.getFirstPassenger();
        if (entity != null) {
            if (entity instanceof Player) {
                this.disablePlayersShield((Player) entity);
            }
            if (entity instanceof Mob) {
                ((Mob) entity).setTarget(null);
            }
            entity.setShiftKeyDown(false);

            if (this.biteTicks % 5 == 0 && this.biteTicks > 0) {
                entity.hurt(DamageSource.mobAttack(this.sixgill), (float) this.sixgill.getAttribute(Attributes.ATTACK_DAMAGE).getValue());
            }
        }
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    private void disablePlayersShield(Player player) {
        player.getCooldowns().addCooldown(Items.SHIELD, 30);
    }
}
