package com.lagadd.biocubes.common.entities.creatures.cryodon;

import com.lagadd.biocubes.common.entities.BiocubeCreature;
import com.lagadd.biocubes.common.entities.ai.AnimatedAttackGoal;
import com.lagadd.biocubes.common.entities.ai.SleepGoal;
import com.lagadd.biocubes.common.entities.creatures.cryodon.attacks.CryodonBite;
import com.lagadd.biocubes.common.entities.creatures.cryodon.attacks.CryodonRoar;
import com.lagadd.biocubes.common.entities.creatures.cryodon.attacks.CryodonStun;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

public class Cryodon extends BiocubeCreature {

    private final static EntityDataAccessor<Boolean> AREA_STUN = SynchedEntityData.defineId(Cryodon.class, EntityDataSerializers.BOOLEAN);

    private int stun_tick = 0;
    private int roar_tick = 0;
    private int bite_tick = 0;

    private int sleepTick = 0;
    private int sleep_cooldown = 120; // about 6s

    public Cryodon(EntityType<? extends Cryodon> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new CryodonStun(this, 16.0f, 5.0f));
        this.goalSelector.addGoal(0, new CryodonRoar(this));
        this.goalSelector.addGoal(0, new CryodonBite(this));
        this.goalSelector.addGoal(0, new SleepGoal(this, 1.5F));
        this.goalSelector.addGoal(1, new AnimatedAttackGoal(this, 1.0D, 16.0D));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Monster.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Sheep.class, true));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(AREA_STUN, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag p_21484_) {
        super.addAdditionalSaveData(p_21484_);
        p_21484_.putBoolean("areaStun", this.getAreaStun());
        p_21484_.putInt("sleepTick", this.sleepTick);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag p_21450_) {
        super.readAdditionalSaveData(p_21450_);
        this.setAreaStun(p_21450_.getBoolean("areaStun"));
        this.sleepTick = p_21450_.getInt("sleepTick");
    }

    public boolean getAreaStun() {
        return this.entityData.get(AREA_STUN);
    }

    public void setAreaStun(boolean flag) {
        this.entityData.set(AREA_STUN, flag);
    }

    @Override
    public void doAnimatedAttack() {
        if (!this.level.isClientSide) {
            var target = this.getTarget();
            if (target != null && !this.hasAnimationRunning()) {
                if (this.stun_tick == 0) {
                    System.out.println("Stun Ready");
                    this.setAnimation(CryodonAttacks.STUN.getId());
                    this.stun_tick = CryodonAttacks.STUN.getCooldown();
                }
                if (this.roar_tick == 0) {
                    System.out.println("Roar Ready");
                    this.setAnimation(CryodonAttacks.ROAR.getId());
                    this.roar_tick = CryodonAttacks.ROAR.getCooldown();
                }
                if (this.bite_tick == 0) {
                    System.out.println("Bite  Ready");
                    this.setAnimation(CryodonAttacks.BITE.getId());
                    this.bite_tick = CryodonAttacks.BITE.getCooldown();
                }
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isInWater() && this.isAlive()) {
            this.setSwimming(true);
        }
        if (this.stun_tick > 0) {
            this.stun_tick--;
            this.setAreaStun(false);
            System.out.println("Cryodon Stun Cooldown remaining at: " + this.stun_tick);
        }
        if (this.roar_tick > 0) {
            this.roar_tick--;
            System.out.println("Cryodon Roar  Cooldown remaining at: " + this.roar_tick);
        }
        if (this.bite_tick > 0) {
            this.bite_tick--;
            System.out.println("Cryodon Bite  Cooldown remaining at: " + this.bite_tick);
        }
        if (!this.level.isClientSide) {
            if (this.getTarget() == null) {
                if (!this.isSleeping()) {
                    this.sleepTick++;
                    System.out.println("Sleeping in : " + this.sleepTick);
                }
            }
            if (this.sleepTick == this.sleep_cooldown) {
                if (!this.isInWater() && this.onGround) {
                    this.setSleeping(true);
                }
                System.out.println("Sleeping Ready");
                this.sleepTick = 0;
            }

            if (this.stun_tick == 0) {
                this.setAreaStun(true);
            }
        }
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (event.isMoving()) {
            if (this.getAreaStun()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("walk charged ", true));
                return PlayState.CONTINUE;
            } else if (this.isSwimming()) {
                if (this.getAreaStun()) {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation("slide charged", true));
                    return PlayState.CONTINUE;
                } else {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", true));
                    return PlayState.CONTINUE;
                }
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("walk normal", true));
                return PlayState.CONTINUE;
            }

        } else if (this.getAnimation() == CryodonAttacks.STUN.getId()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(CryodonAttacks.STUN.getName()));
            return PlayState.CONTINUE;
        } else if (this.getAnimation() == CryodonAttacks.ROAR.getId()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(CryodonAttacks.ROAR.getName()));
            return PlayState.CONTINUE;
        } else if (this.getAnimation() == CryodonAttacks.ROAR.getId()) {
            if (this.getAreaStun()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("bite charged"));
                return PlayState.CONTINUE;
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("bite normal"));
                return PlayState.CONTINUE;
            }
        } else if (this.isSleeping()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("sit normal", true));
            return PlayState.CONTINUE;
        } else if (this.isSwimming()) {
            if (this.getAreaStun()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("slide charged", true));
                return PlayState.CONTINUE;
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("slide normal", true));
                return PlayState.CONTINUE;
            }
        }

        if (this.getAreaStun()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("idle charged", true));
            return PlayState.CONTINUE;
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", true));
            return PlayState.CONTINUE;
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.FOLLOW_RANGE, 40.0D)
                .add(Attributes.MAX_HEALTH, 100.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.4F)
                .add(Attributes.ATTACK_DAMAGE, 10.0D);
    }
}