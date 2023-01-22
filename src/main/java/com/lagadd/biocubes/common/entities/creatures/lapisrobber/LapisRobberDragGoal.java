package com.lagadd.biocubes.common.entities.creatures.lapisrobber;

import com.lagadd.biocubes.common.entities.creatures.sixgill.Sixgill;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

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
        return passenger != null && this.lapis.getRandom().nextFloat() < 0.1F && this.lapis.isGrabbing();
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
        if (!this.lapis.getPassengers().isEmpty() && this.biteTicks > 0) {
            Vec3 flightDest = new Vec3(this.lapis.getX(), this.lapis.getY() + 0.5D, this.lapis.getZ());
            this.lapis.getNavigation().moveTo(flightDest.x, flightDest.y, flightDest.z, 0.8D);

        }

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
                entity.hurt(DamageSource.mobAttack(this.lapis), 2F);
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
