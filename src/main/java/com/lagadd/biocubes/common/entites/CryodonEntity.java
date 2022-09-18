package com.lagadd.biocubes.common.entites;

import com.lagadd.biocubes.common.entites.ai.MeleGoal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.EnumSet;
import java.util.List;

public class CryodonEntity extends Animal implements IAnimatable {
    //SLEEP STATE
    private static final EntityDataAccessor<Boolean> sleep_state = SynchedEntityData.defineId(CryodonEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> area_attack = SynchedEntityData.defineId(CryodonEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> roar_attack = SynchedEntityData.defineId(CryodonEntity.class, EntityDataSerializers.BOOLEAN);

    private int areaTick = 0;
    private int roarTick = 0;

    public CryodonEntity(EntityType<? extends Animal> p_27557_, Level p_27558_) {
        super(p_27557_, p_27558_);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SleepGoal(this));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Monster.class, true));
        this.goalSelector.addGoal(1, new RoarGoal(this));
        this.goalSelector.addGoal(1, new AreaAttackGoal(this,16.0D, 5.0D));
        this.goalSelector.addGoal(1, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(2, new MeleGoal(this, 15, 1.25D));
    }

    private AnimationFactory factory = new AnimationFactory(this);

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.getSleepState()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("sit", true));
            return PlayState.CONTINUE;
        } else if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("walk normal", true));
            return PlayState.CONTINUE;
        } else if (this.areaAttack()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("hit stun", true));
            return PlayState.CONTINUE;
        }
        else if (this.roarAttack()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("roar", true));
            return PlayState.CONTINUE;
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", true));
            return PlayState.CONTINUE;
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.getSleepState() || this.isImmobile()) {
            this.jumping = false;
            this.xxa = 0.0F;
            this.zza = 0.0F;
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getTarget() != null) {
            if (!this.roarAttack()) {
                this.roarTick++;
            }
            if (this.getHealth() < this.getMaxHealth() / 2) {
                if (!this.areaAttack()) {
                    this.areaTick++;
                }
            }
        }

        if (roarTick == 100) {
            this.setRoarAttack(true);
            this.roarTick = 0;
        }
        if (areaTick == 100) {
            this.setAreaAttack(true);
            this.areaTick = 0;
        }

    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.FOLLOW_RANGE, 40.0D)
                .add(Attributes.MAX_HEALTH, 100.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.5F)
                .add(Attributes.ATTACK_DAMAGE, 10.0D);
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(sleep_state, false);
        this.entityData.define(area_attack, false);
        this.entityData.define(roar_attack, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putBoolean("sleep_state", this.getSleepState());
        compoundTag.putBoolean("area_attack", this.areaAttack());
        compoundTag.putBoolean("roar_attack", this.roarAttack());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.setSleep_state(compoundTag.getBoolean("sleep_state"));
        this.setRoarAttack(compoundTag.getBoolean("roar_attack"));
        this.setAreaAttack(compoundTag.getBoolean("area_attack"));
    }

    public boolean canSleep() {
        var day = this.level.getDayTime();
        var midnight = day > 18000 && day < 23000;
        if (this.level.isWaterAt(this.blockPosition())) {
            return false;
        } else if (midnight) {
            return false;
        } else {
            return day > 12000 && day < 28000;
        }
    }

    public boolean areaAttack() {
        return this.entityData.get(area_attack);
    }

    public void setAreaAttack(boolean flag) {
        this.entityData.set(area_attack, flag);
    }

    public boolean roarAttack() {
        return this.entityData.get(roar_attack);
    }

    public void setRoarAttack(boolean flag) {
        this.entityData.set(roar_attack, flag);
    }

    public boolean getSleepState() {
        return this.entityData.get(sleep_state);
    }

    public void setSleep_state(boolean flag) {
        this.entityData.set(sleep_state, flag);
    }


    @Override
    public AgeableMob getBreedOffspring(ServerLevel p_146743_, AgeableMob p_146744_) {
        return null;
    }


    public <T extends Entity> List<T> getEntitiesNearby(Class<T> entityClass, double dX, double dY, double dZ, double r) {
        return this.level.getEntitiesOfClass(entityClass, this.getBoundingBox().inflate(dX, dY, dZ), e -> e != this && distanceTo(e) <= r + e.getBbWidth() / 2F && e.getY() <= getY() + dY);
    }

    private static class RoarGoal extends Goal {
        private CryodonEntity entity;

        public RoarGoal(CryodonEntity entity) {
            this.setFlags(EnumSet.of(Flag.LOOK));
            this.entity = entity;
        }

        @Override
        public boolean canUse() {
            return this.entity.roarAttack();
        }
    }

    private static class AreaAttackGoal extends Goal {
        private CryodonEntity entity;
        private double range;
        private double height;

        public AreaAttackGoal(CryodonEntity entity, double range, double height) {
            this.entity = entity;
            this.range = range;
            this.height = height;
            this.setFlags(EnumSet.of(Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return this.entity.areaAttack();
        }

        @Override
        public void start() {
            super.start();
            this.entity.getTarget();
        }

        @Override
        public void stop() {
            this.entity.setAreaAttack(false);
        }

        @Override
        public void tick() {
            super.tick();
            if (this.entity.getTarget() != null && this.entity.distanceTo(entity.getTarget()) < 3.0D) {
                hitEntities();
            }
        }

        private void hitEntities() {
            List<LivingEntity> entitiesHit = this.entity.getEntitiesNearby(LivingEntity.class, range, height, range, range);
            for (LivingEntity entityHit : entitiesHit) {
                float entityHitAngle = (float) ((Math.atan2(entityHit.getZ() - entity.getZ(), entityHit.getX() - entity.getX()) * (180 / Math.PI) - 90) % 360);
                float entityAttackingAngle = entity.yBodyRot % 360;
                if (entityHitAngle < 0) {
                    entityHitAngle += 360;
                }
                if (entityAttackingAngle < 0) {
                    entityAttackingAngle += 360;
                }
                float entityRelativeAngle = entityHitAngle - entityAttackingAngle;
                float entityHitDistance = (float) Math.sqrt((entityHit.getZ() - entity.getZ()) * (entityHit.getZ() - entity.getZ()) + (entityHit.getX() - entity.getX()) * (entityHit.getX() - entity.getX())) - entityHit.getBbWidth() / 2f;
                if (entityHitDistance <= range && (entityRelativeAngle <= 180 / 2 && entityRelativeAngle >= -180 / 2) || (entityRelativeAngle >= 360 - 180 / 2 || entityRelativeAngle <= -360 + 180 / 2)) {
                    entity.doHurtTarget(entityHit);
                }
            }
        }

    }

    private static class SleepGoal extends Goal {
        private CryodonEntity entity;

        public SleepGoal(CryodonEntity entity) {
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
            this.entity = entity;
        }

        @Override
        public boolean canUse() {
            if (entity.xxa == 0.0F && entity.yya == 0.0F && entity.zza == 0.0f) {
                return entity.canSleep() || entity.getSleepState();
            } else {
                return false;
            }
        }

        @Override
        public boolean canContinueToUse() {
            return entity.canSleep();
        }

        @Override
        public void start() {
            entity.setJumping(false);
            entity.setSleep_state(true);
            entity.getNavigation().stop();
            entity.getMoveControl().setWantedPosition(entity.getX(), entity.getY(), entity.getZ(), 0.00D);
            System.out.println("Test Sleep");
        }

        @Override
        public void stop() {
            entity.setSleep_state(false);
        }
    }
}