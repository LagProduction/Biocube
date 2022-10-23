package com.lagadd.biocubes.common.entities.creatures.sixgill;

import java.util.Map;

import com.mojang.math.Vector3f;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.shadowed.eliotlash.mclib.utils.MathHelper;

public class Sixgill extends Animal implements IAnimatable {
    private AnimationFactory factory = new AnimationFactory(this);
    private static final EntityDataAccessor<Integer> MOISTNESS_LEVEL = SynchedEntityData.defineId(Dolphin.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> MOVING = SynchedEntityData.defineId(Sixgill.class, EntityDataSerializers.BOOLEAN);
    public static final int TOTAL_AIR_SUPPLY = 4800;

    public Sixgill(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new SixgillMoveController(this);
        this.lookControl = new SmoothSwimmingLookControl(this, 10);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
    }

    @Override
    protected void registerGoals() {
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, Monster.class, true));
        this.goalSelector.addGoal(0, new BreathAirGoal(this));
        this.goalSelector.addGoal(0, new TryFindWaterGoal(this));
        this.goalSelector.addGoal(1, new SixgillCrushBiteGoal(this));
        this.goalSelector.addGoal(2, new SixgillGrabGoal(this, 2.5F, true));
        this.goalSelector.addGoal(3, new SixgillRandomSwimmingGoal(this, 1.1D, 15));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.25D, Ingredient.of(Items.ROTTEN_FLESH), false));
        this.targetSelector.addGoal(5, new HurtByTargetGoal(this));
        super.registerGoals();
    }


    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.isInWater() && this.isUnderWater()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("swim ", true));
            return PlayState.CONTINUE;
        }
        if (this.isOnGround() && !this.isUnderWater() && !this.isInWater()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("breached", true));
            return PlayState.CONTINUE;
        }
        return null;
    }

    private <E extends IAnimatable> PlayState predicate1(AnimationEvent<E> event) {

        if (this.isAggressive()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("bite"));
            return PlayState.CONTINUE;
        }
        return null;
    }

    @Override
    public boolean shouldRiderSit() {
        return false;
    }

    @Override
    public boolean canRiderInteract() {
        return true;
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

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
        data.addAnimationController(new AnimationController<>(this, "controller1", 0, this::predicate1));
    }


    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 24.0D)
                .add(Attributes.MOVEMENT_SPEED, 1.0F)
                .add(Attributes.ATTACK_DAMAGE, 3.0D);
    }


    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    public Map<String, Vector3f> getModelRotationValues() {
        return this.getModelRotationValues();
    }

    public boolean canBreatheUnderwater() {
        return false;
    }

    protected void handleAirSupply(int p_28326_) {
    }

    protected PathNavigation createNavigation(Level p_27480_) {
        return new WaterBoundPathNavigation(this, p_27480_);
    }

    @Override
    public boolean rideableUnderWater() {
        return true;
    }

    public int getMaxAirSupply() {
        return 4800;
    }

    protected int increaseAirSupply(int p_28389_) {
        return this.getMaxAirSupply();
    }

    public int getMoistnessLevel() {
        return this.entityData.get(MOISTNESS_LEVEL);
    }

    public void setMoisntessLevel(int p_28344_) {
        this.entityData.set(MOISTNESS_LEVEL, p_28344_);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(MOISTNESS_LEVEL, 2400);
        this.entityData.define(MOVING, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Moistness", this.getMoistnessLevel());
        compound.putBoolean("IsMoving", this.isMoving());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        this.setMoisntessLevel(compound.getInt("Moistness"));
        this.setMoving(compound.getBoolean("IsMoving"));
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.SLIME_DEATH_SMALL;
    }

    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.SLIME_HURT;
    }

    protected void playStepSound(BlockPos p_180429_1_, BlockState p_180429_2_) {
        this.playSound(SoundEvents.SLIME_BLOCK_STEP, 0.15F, 1.0F);
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel p_146743_, AgeableMob p_146744_) {
        return null;
    }

    public void tick() {
        super.tick();
        if (this.getTarget() != null && !this.getTarget().isAlive()) {
            this.setTarget(null);
        }


        if (this.isNoAi()) {
            this.setAirSupply(this.getMaxAirSupply());
        } else {
            if (this.isInWaterRainOrBubble()) {
                this.setMoisntessLevel(2400);
            } else {
                this.setMoisntessLevel(this.getMoistnessLevel() - 1);
                if (this.getMoistnessLevel() <= 0) {
                    this.hurt(DamageSource.DRY_OUT, 1.0F);
                }
                if (this.onGround) {
                    this.setDeltaMovement(this.getDeltaMovement().add((double) ((this.random.nextFloat() * 2.0F - 1.0F) * 0.2F), 0.5D, (double) ((this.random.nextFloat() * 2.0F - 1.0F) * 0.2F)));
                    this.setYRot(this.random.nextFloat() * 360.0F);
                    this.onGround = false;
                    this.hasImpulse = true;
                }
            }
        }
    }

    public boolean isMoving() {
        return this.entityData.get(MOVING);
    }

    public void setMoving(boolean moving) {
        this.entityData.set(MOVING, moving);
    }


    @Override
    public void travel(Vec3 p_213352_1_) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(0.01F, p_213352_1_);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
            if (!this.isMoving() && this.getTarget() == null) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.005D, 0.0D));
            }
        } else {
            super.travel(p_213352_1_);
        }
    }

}