package com.lagadd.biocubes.common.entities.creatures.lapisrobber;

import com.lagadd.biocubes.common.entities.creatures.sixgill.Sixgill;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;

import java.util.EnumSet;

public class LapisRobberDragGoal extends Goal {
    public LapisRobberEntity lapis;
    private float originalYaw;
    private float biteTicks;

    public LapisRobberDragGoal(LapisRobberEntity lapis) {
        this.lapis = lapis;
        this.setFlags(EnumSet.of(Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        Entity passenger = !lapis.getPassengers().isEmpty() ? this.lapis.getPassengers().get(0) : null;
        if (passenger instanceof Player) {
            if (((Player) passenger).isCreative() || passenger.isSpectator()) {
                return false;
            }
        }
        return passenger != null && this.lapis.getRandom().nextFloat() < 0.1F;
    }

    @Override
    public boolean canContinueToUse() {
        Entity passenger = this.lapis.getFirstPassenger();
        if (passenger instanceof Player) {
            if (((Player) passenger).isCreative() || passenger.isSpectator()) {
                return false;
            }
        }
        return this.biteTicks <= 55 && passenger != null;
    }

    @Override
    public void start() {
        this.originalYaw = this.lapis.getYRot();
    }

    @Override
    public void stop() {
        this.originalYaw = 0;
        this.biteTicks = 0;
    }

    @Override
    public void tick() {
        this.biteTicks++;

        this.lapis.getNavigation().stop();

        this.lapis.yRotO = this.lapis.getYRot();

        this.lapis.yBodyRot = (this.originalYaw) + 75 * Mth.cos(this.lapis.tickCount * 0.5F) * 1F;
        this.lapis.setYRot((this.originalYaw) + 75 * Mth.cos(this.lapis.tickCount * 0.5F) * 1F);

        Entity entity = this.lapis.getFirstPassenger();
        if (entity != null) {
            if (entity instanceof Player) {
                this.disablePlayersShield((Player) entity);
            }
            if (entity instanceof Mob) {
                ((Mob) entity).setTarget(null);
            }
            entity.setShiftKeyDown(false);

            if (this.biteTicks % 5 == 0 && this.biteTicks > 0) {
                entity.hurt(DamageSource.mobAttack(this.lapis), (float) this.lapis.getAttribute(Attributes.ATTACK_DAMAGE).getValue());
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
