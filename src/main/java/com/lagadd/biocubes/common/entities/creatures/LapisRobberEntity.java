package com.lagadd.biocubes.common.entities.creatures;

import java.util.Map;


import com.mojang.math.Vector3f;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class LapisRobberEntity extends Animal implements IAnimatable {
    private AnimationFactory factory = new AnimationFactory(this);

    //Checker State if this mob is about to hide to ground.
    protected static final EntityDataAccessor<Boolean> HIDING = SynchedEntityData.defineId(LapisRobberEntity.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> GRABBING = SynchedEntityData.defineId(LapisRobberEntity.class, EntityDataSerializers.BOOLEAN);

    protected int grabbing_tick;
    protected final int grabbing_cooldown = 100;

    protected int grab_duration;
    protected final int grabbing_max_duration = 50;


    private int hidetick;
    private final int hide_count = 50;

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("flying", true));
            return PlayState.CONTINUE;
        } else if (this.isHiding()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("fly_dig"));
            return PlayState.CONTINUE;
        } else if (this.isGrabbing()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("grab"));
            return PlayState.CONTINUE;
        }
        event.getController().setAnimation(new AnimationBuilder().addAnimation("flying idle", true));
        return PlayState.CONTINUE;
    }

    //Defining EntityDataAccessor
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(HIDING, false);
        this.entityData.define(GRABBING, false);
    }

    public void setHiding(boolean flag) {
        this.entityData.set(HIDING, flag);
    }

    public boolean isHiding() {
        return this.entityData.get(HIDING);
    }

    public void setGrabbing(boolean flag) {
        this.entityData.set(GRABBING, flag);
    }

    public boolean isGrabbing() {
        return this.entityData.get(GRABBING);
    }


    //Compound Tag requirements

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putBoolean("HIDING", this.isHiding());
        compoundTag.putBoolean("GRABBING", this.isGrabbing());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.setHiding(compoundTag.getBoolean("HIDING"));
        this.setGrabbing(compoundTag.getBoolean("GRABBING"));
    }

    public LapisRobberEntity(EntityType<? extends Animal> p_i48567_1_, Level p_i48567_2_) {
        super(p_i48567_1_, p_i48567_2_);
        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER_BORDER, 16.0F);
        this.setPathfindingMalus(BlockPathTypes.COCOA, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.FENCE, -1.0F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.FOLLOW_RANGE, 40.0D)
                .add(Attributes.MAX_HEALTH, 100.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.0F)
                .add(Attributes.FLYING_SPEED, 1.0F)
                .add(Attributes.ATTACK_DAMAGE, 10.0D);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(9, new FloatGoal(this));
        this.goalSelector.addGoal(1, new RandomStrollGoal(this, 1.0D));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Monster.class, true));
        this.targetSelector.addGoal(7, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(7, new NearestAttackableTargetGoal<>(this, Sheep.class, true));
        super.registerGoals();
    }

    public Map<String, Vector3f> getModelRotationValues() {
        return this.getModelRotationValues();
    }

    public boolean canBreatheUnderwater() {
        return false;
    }

    protected PathNavigation createNavigation(Level p_27815_) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, p_27815_) {
            public boolean isStableDestination(BlockPos p_27947_) {
                return !this.level.getBlockState(p_27947_.below()).isAir();
            }
        };
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.setCanFloat(false);
        flyingpathnavigation.setCanPassDoors(true);
        return flyingpathnavigation;
    }

    public float getWalkTargetValue(BlockPos p_27788_, LevelReader p_27789_) {
        return p_27789_.getBlockState(p_27788_).isAir() ? 10.0F : 0.0F;
    }

    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getTarget() != null && this.hasLineOfSight(this.getTarget())) {
            this.grabbing_tick++;
            if (this.distanceTo(this.getTarget()) > 3.0F) {
                this.navigation.moveTo(this.getTarget(), 1.0D);
            } else if (this.grabbing_tick == this.grabbing_cooldown && this.distanceTo(this.getTarget()) < 3.0F) {
                this.navigation.stop();
                this.setGrabbing(true);
                this.getTarget().startRiding(this, true);
            }
        }

        if (this.isGrabbing() && this.getTarget() != null) {
            this.grab_duration++;
            Vec3 vec3 = new Vec3(this.getX(), this.getY(), this.getZ());

            if (this.grab_duration > 0 && this.grab_duration <= this.grabbing_max_duration) {
                this.getTarget().hurt(DamageSource.mobAttack(this), (float) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue());
                this.setDeltaMovement(vec3.x, vec3.y * 0.05D, vec3.z);
            }
            if (this.grab_duration >= this.grabbing_max_duration) {
                this.removePassenger(this.getTarget());
                this.setGrabbing(false);
                this.grab_duration = 0;
                this.grabbing_tick = 0;
            }
        }


        if (this.shouldHideUnderneath()) {
            this.setHiding(true);
        }

        if (this.isHiding()) {
            System.out.println("Hiding");
            this.hidetick++;
            this.navigation.stop();
            System.out.println(this.hidetick);
            if (hidetick == this.hide_count) {
                this.hidetick = 0;
                this.remove(RemovalReason.DISCARDED);
            }
        }
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel p_146743_, AgeableMob p_146744_) {
        // TODO Auto-generated method stub
        return null;
    }

    protected boolean shouldHideUnderneath() {
        if (this.level.isClientSide) {
            return false;
        }
        return !this.level.isNight() && this.getTarget() == null && this.isAlive() && !this.isInWater() && !this.isInLava();
    }

    @Override
    public boolean shouldRiderSit() {
        return false;
    }


    @Override
    public boolean shouldRiderFaceForward(Player player) {
        return true;
    }

    public float getMountDistance() {
        return 1.2F;
    }

    @Override
    public double getPassengersRidingOffset() {
        return 0.5F;
    }

    @Override
    protected void addPassenger(Entity p_20349_) {
        super.addPassenger(p_20349_);
    }

    @Override
    protected void removePassenger(Entity p_20352_) {
        super.removePassenger(p_20352_);
    }

    @Override
    public void positionRider(Entity passenger) {
        if (passenger instanceof LivingEntity) {
            float distance = this.getMountDistance();

            double dx = Math.cos((this.getYRot() + 90) * Math.PI / 180.0D) * distance;
            double dy = -Math.sin(this.getXRot() * (Math.PI / 180.0D));
            double dz = Math.sin((this.getYRot() + 90) * Math.PI / 180.0D) * distance;

            Vec3 riderPos = new Vec3(this.getX() + dx, this.getY(), this.getZ() + dz);

            double offset = passenger instanceof Player ? this.getPassengersRidingOffset() - 0.2D : this.getPassengersRidingOffset() - 0.5F;

            passenger.setPos(riderPos.x, this.getY() + dy + offset, riderPos.z);
        } else {
            super.positionRider(passenger);
        }
    }

}
				