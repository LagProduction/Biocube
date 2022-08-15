package com.lagadd.biocubes.common.entites;

	import java.util.Map;

import com.lagadd.biocubes.common.entites.ai.MeleGoal;
import com.mojang.math.Vector3f;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
	import software.bernie.geckolib3.core.PlayState;
	import software.bernie.geckolib3.core.builder.AnimationBuilder;
	import software.bernie.geckolib3.core.controller.AnimationController;
	import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
	import software.bernie.geckolib3.core.manager.AnimationData;
	import software.bernie.geckolib3.core.manager.AnimationFactory;

				public class CryodonEntity extends Animal implements IAnimatable  {
					private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(CryodonEntity.class, EntityDataSerializers.BOOLEAN);
				    private static final EntityDataAccessor<Boolean> SNIFFING = SynchedEntityData.defineId(CryodonEntity.class, EntityDataSerializers.BOOLEAN);
				    private AnimationFactory factory = new AnimationFactory(this);

				    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
				        if (this.isSitting()) {
				            event.getController().setAnimation(new AnimationBuilder().addAnimation("sit normal", true));
				            return PlayState.CONTINUE;
				        } else if (event.isMoving()) {
				            event.getController().setAnimation(new AnimationBuilder().addAnimation("walk normal", true));
				            return PlayState.CONTINUE;
				        } else if (!this.isSniffing()) {
				            event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", true));
				            return PlayState.CONTINUE;
				        }
				        event.getController().markNeedsReload();
				        return PlayState.STOP;
				    }

				    private <E extends IAnimatable> PlayState sniffPredicate(AnimationEvent<E> event) {
				        if (this.isSniffing()) {
				            event.getController().setAnimation(new AnimationBuilder().addAnimation("roar", true));
				            return PlayState.CONTINUE;
				        }
				        event.getController().markNeedsReload();
				        return PlayState.STOP;
				    }

				    private <E extends IAnimatable> PlayState swingPredicate(AnimationEvent<E> event) {
				        if (this.swinging && event.getController().getAnimationState().equals(AnimationState.Stopped)) {
				            event.getController().markNeedsReload();
				            event.getController().setAnimation(new AnimationBuilder().addAnimation("bite normal", false));
				            this.swinging = false;
				        
				        return PlayState.CONTINUE;
				    }
				        event.getController().markNeedsReload();
				        return PlayState.STOP;
				    }

				    @Override
				    public void registerControllers(AnimationData data) {
				        data.setResetSpeedInTicks(10);
				        data.addAnimationController(new AnimationController<>(this, "controller", 10, this::predicate));
				        data.addAnimationController(new AnimationController<>(this, "sniffController", 0, this::sniffPredicate));
				        data.addAnimationController(new AnimationController<>(this, "swingController", 0, this::swingPredicate));
				    }
					   public static AttributeSupplier.Builder createAttributes() {
						   return Mob.createMobAttributes()
						    .add(Attributes.FOLLOW_RANGE, 40.0D)
							.add(Attributes.MAX_HEALTH, 100.0D)
							.add(Attributes.MOVEMENT_SPEED, 0.5F)
							.add(Attributes.ATTACK_DAMAGE, 10.0D);
				}

					@Override
					public AnimationFactory getFactory() 
					{
						return this.factory;
					}

					@Override
					protected void registerGoals() {
				        this.goalSelector.addGoal(2, new CryodonMeleeAttackGoal(this, 1.25D, true));
						  this.goalSelector.addGoal(1, new RandomStrollGoal(this, 1.0D));
						  this.goalSelector.addGoal(2, new MeleGoal(this, 15, 1.25D));
					      this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Monster.class, true));
					      this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Player.class, true));
					      this.goalSelector.addGoal(2, new MoveTowardsTargetGoal(this, 0.9D, 32.0F));
					      this.targetSelector.addGoal(6, new HurtByTargetGoal(this));
						  super.registerGoals();
					}
				    @Override
				    protected void defineSynchedData() {
				        super.defineSynchedData();
				        this.entityData.define(SNIFFING, false);
				        this.entityData.define(SITTING, false);
				    }

				   public Map<String, Vector3f> getModelRotationValues() {
					   return this.getModelRotationValues();
				   }
				   public boolean canBreatheUnderwater() {
					      return false;
				   }
				   protected PathNavigation createNavigation(Level p_27480_) {
				      return new GroundPathNavigation(this, p_27480_);
				   }
			       public float getWalkTargetValue(BlockPos p_149140_, LevelReader p_149141_) {
					   return 0.0F;
				   }
				   public boolean isPushedByFluid() {
				      return false;
				   }
				   public boolean isSniffing() {
				        return this.entityData.get(SNIFFING);
				    }

				    public void setSniffing(boolean sniffing) {
				        this.entityData.set(SNIFFING, sniffing);
				    }
				    public boolean isSitting() {
				        return this.entityData.get(SITTING);
				    }

				    public void setSitting(boolean sitting) {
				        this.entityData.set(SITTING, sitting);
				    }
				    static class CryodonMeleeAttackGoal extends MeleeAttackGoal {

				        public CryodonMeleeAttackGoal(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
				            super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
				        }
				}
					protected CryodonEntity(EntityType<? extends Animal> p_27557_, Level p_27558_) {
						super(p_27557_, p_27558_);

					}@Override
					public AgeableMob getBreedOffspring(ServerLevel p_146743_, AgeableMob p_146744_) {
						return null;
					}
				}
					
				