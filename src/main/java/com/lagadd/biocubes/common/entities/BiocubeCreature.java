package com.lagadd.biocubes.common.entities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.level.Level;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.List;

public abstract class BiocubeCreature extends PathfinderMob implements IAnimatable, IEntityAdditionalSpawnData {

    //Sleep State - Generalizing into whole mobs
    private static final EntityDataAccessor<Boolean> SLEEPING = SynchedEntityData.defineId(BiocubeCreature.class, EntityDataSerializers.BOOLEAN);

    //Compound NBT variables
    protected int animation;
    protected int animationframe;

    protected float targetdistance;

    public AnimationFactory factory = new AnimationFactory(this);

    public BiocubeCreature(EntityType<? extends BiocubeCreature> entityType, Level level) {
        super(entityType, level);
        this.noCulling = true;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
        //Test
        this.goalSelector.addGoal(1, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(1, new FloatGoal(this));
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeVarInt(this.animation);
        buffer.writeVarInt(this.animationframe);
    }


    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        this.animation = additionalData.readVarInt();
        this.animationframe = additionalData.readVarInt();
    }

    @Override
    public void aiStep() {
        super.aiStep();
        var attackTarget = this.getTarget();
        if (attackTarget != null) {
            //Just an assurance that the inheritted mob should look at target entity each ai tick.
            if (attackTarget.isDeadOrDying() || attackTarget.getHealth() <= 0) {
                this.setTarget(null);
            }
            this.lookControl.setLookAt(attackTarget, 30F, 30F);
            this.targetdistance = this.distance(this, attackTarget);
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.runAnimTick();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SLEEPING, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag p_21484_) {
        super.addAdditionalSaveData(p_21484_);
        p_21484_.putBoolean("sleeping", this.isSleeping());
        p_21484_.putInt("animationframe", this.animationframe);
        p_21484_.putInt("animation", this.animation);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag p_21450_) {
        super.readAdditionalSaveData(p_21450_);
        this.setSleeping(p_21450_.getBoolean("sleeping"));
        this.setAnimation(p_21450_.getInt("animation"));
        this.animationframe = p_21450_.getInt("animationframe");
    }

    @Override
    public void handleEntityEvent(byte pak) {
        if (pak <= 0) {
            this.animation = Math.abs(pak);
            this.animationframe = 0;
        } else {
            super.handleEntityEvent(pak);
        }
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    public boolean hasAnimationRunning() {
        return this.animation != 0;
    }

    public int getAnimation() {
        return this.animation;
    }

    public int getAnimationframe() {
        return this.animationframe;
    }

    public void setAnimation(int id) {
        this.animation = id;
        this.animationframe = -1;
        this.level.broadcastEntityEvent(this, (byte) -id);
    }

    public double getReachRange(LivingEntity target) {
        return (double) (this.getBbWidth() * 1.5F * this.getBbWidth() + target.getBbWidth());
    }

    public abstract void doAnimatedAttack();

    public void runAnimTick() {
        if (hasAnimationRunning()) {
            this.animationframe++;
        }
    }

    //Sleeping State
    public void setSleeping(boolean sleeping) {
        this.entityData.set(SLEEPING, sleeping);
    }

    public boolean isSleeping() {
        return this.entityData.get(SLEEPING);
    }

    public boolean canSleep() {
        if (this.level.isWaterAt(this.blockPosition())) {
            return false;
        } else if (this.getTarget() != null) {
            return false;
        } else {
            return this.isSleeping();
        }
    }

    public <T extends Entity> List<T> getEntitiesNearby(Class<T> entityClass, double dX, double dY, double dZ, double r) {
        return this.level.getEntitiesOfClass(entityClass, this.getBoundingBox().inflate(dX, dY, dZ), e -> e != this && distanceTo(e) <= r + e.getBbWidth() / 2F && e.getY() <= getY() + dY);
    }

    public void hitEntities(float range, float height) {
        List<LivingEntity> entitiesHit = this.getEntitiesNearby(LivingEntity.class, range, height, range, range);
        for (LivingEntity entityHit : entitiesHit) {
            float entityHitAngle = (float) ((Math.atan2(entityHit.getZ() - this.getZ(), entityHit.getX() - this.getX()) * (180 / Math.PI) - 90) % 360);
            float entityAttackingAngle = this.yBodyRot % 360;
            if (entityHitAngle < 0) {
                entityHitAngle += 360;
            }
            if (entityAttackingAngle < 0) {
                entityAttackingAngle += 360;
            }
            float entityRelativeAngle = entityHitAngle - entityAttackingAngle;
            float entityHitDistance = (float) Math.sqrt((entityHit.getZ() - this.getZ()) * (entityHit.getZ() - this.getZ()) + (entityHit.getX() - this.getX()) * (entityHit.getX() - this.getX())) - entityHit.getBbWidth() / 2f;
            if (entityHitDistance <= range && (entityRelativeAngle <= 180 / 2 && entityRelativeAngle >= -180 / 2) || (entityRelativeAngle >= 360 - 180 / 2 || entityRelativeAngle <= -360 + 180 / 2)) {
                this.hurt(DamageSource.mobAttack(this), 30F);
            }
        }
    }

    public float distance(@Nullable LivingEntity living, @Nullable LivingEntity target) {
        var dist = living.distanceTo(target);
        var targetcenter = target.getBbWidth() / 2F;
        var precise = dist - targetcenter;
        return precise;
    }


}
