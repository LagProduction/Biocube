package com.lagadd.biocubes.common.entities.creatures.sixgill;

import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;

import java.util.EnumSet;

public class SixgillCrushBiteGoal extends Goal {
    public Sixgill Sixgill;
    private float originalYaw;
    private float thrashedTicks;

    public SixgillCrushBiteGoal(Sixgill Sixgill) {
        this.Sixgill = Sixgill;
        this.setFlags(EnumSet.of(Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        Entity passenger = !Sixgill.getPassengers().isEmpty() ? this.Sixgill.getPassengers().get(0) : null;
        if (passenger instanceof Player) {
            if (((Player) passenger).isCreative() || passenger.isSpectator()) {
                return false;
            }
        }
        return passenger != null && this.Sixgill.getRandom().nextFloat() < 0.1F;
    }

    @Override
    public boolean canContinueToUse() {
        Entity passenger = !Sixgill.getPassengers().isEmpty() ? this.Sixgill.getPassengers().get(0) : null;
        if (passenger instanceof Player) {
            if (((Player) passenger).isCreative() || passenger.isSpectator()) {
                return false;
            }
        }
        return this.thrashedTicks <= 55 && passenger != null;
    }

    @Override
    public void start() {
        this.originalYaw = this.Sixgill.getYRot();
    }

    @Override
    public void stop() {
        this.originalYaw = 0;
        this.thrashedTicks = 0;
    }

    @Override
    public void tick() {
        this.thrashedTicks++;

        this.Sixgill.getNavigation().stop();

        this.Sixgill.yRotO = this.Sixgill.getYRot();

        this.Sixgill.yBodyRot = (this.originalYaw) + 75 * Mth.cos(this.Sixgill.tickCount * 0.5F) * 1F;
        this.Sixgill.setYRot((this.originalYaw) + 75 * Mth.cos(this.Sixgill.tickCount * 0.5F) * 1F);

        Entity entity = this.Sixgill.getPassengers().get(0);

        if (entity instanceof Player) {
            this.disablePlayersShield((Player) entity);
        }
        entity.setShiftKeyDown(false);

        if (this.thrashedTicks % 5 == 0 && this.thrashedTicks > 0) {
            entity.hurt(DamageSource.mobAttack(this.Sixgill), (float) this.Sixgill.getAttribute(Attributes.ATTACK_DAMAGE).getValue());
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
