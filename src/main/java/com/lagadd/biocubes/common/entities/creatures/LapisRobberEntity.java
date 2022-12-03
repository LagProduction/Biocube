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
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
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
    private int hidetick;
    private final int hide_count = 50;

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("flying", true));
            return PlayState.CONTINUE;
        } else if (this.isHiding()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("fly_dig"));
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
    }

    public void setHiding(boolean flag) {
        this.entityData.set(HIDING, flag);
    }

    public boolean isHiding() {
        return this.entityData.get(HIDING);
    }


    //Compound Tag requirements


    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putBoolean("HIDING", this.isHiding());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.setHiding(compoundTag.getBoolean("HIDING"));
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
        if (this.getTarget() != null && this.distanceTo(this.getTarget()) > 3.0F) {
            this.navigation.moveTo(this.getTarget(), 1.0D);
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

}
				