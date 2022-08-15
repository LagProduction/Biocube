package com.lagadd.biocubes.common.entites;

	import java.util.Map;

import com.lagadd.biocubes.common.entites.ai.MeleGoal;
import com.mojang.math.Vector3f;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsTargetGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import software.bernie.geckolib3.core.IAnimatable;
	import software.bernie.geckolib3.core.PlayState;
	import software.bernie.geckolib3.core.builder.AnimationBuilder;
	import software.bernie.geckolib3.core.controller.AnimationController;
	import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
	import software.bernie.geckolib3.core.manager.AnimationData;
	import software.bernie.geckolib3.core.manager.AnimationFactory;

				public class IgneoglossaEntity extends Animal implements IAnimatable  {
					private AnimationFactory factory = new AnimationFactory(this);
					
					   private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {					
							if (event.isMoving()) {
								event.getController().setAnimation(new AnimationBuilder().addAnimation("walk normal", true));
								return PlayState.CONTINUE;
							}			
								if (this.isSprinting()) {
									event.getController().setAnimation(new AnimationBuilder().addAnimation("walk normal", true));
									return PlayState.CONTINUE;
								}
						event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", true));
						return PlayState.CONTINUE;
					}
					   private <E extends IAnimatable> PlayState predicate1(AnimationEvent<E> event) {
						
							if (this.isAggressive()) {
								event.getController().setAnimation(new AnimationBuilder().addAnimation("inflated attack", true));
								return PlayState.CONTINUE;	
							}
							if (this.isAggressive()) {
								event.getController().setAnimation(new AnimationBuilder().addAnimation("inflated jump attack", true));
								return PlayState.CONTINUE;
							}
						return null;
					}
					
					public IgneoglossaEntity(EntityType<? extends Animal> p_i48567_1_, Level p_i48567_2_) {
					      super(p_i48567_1_, p_i48567_2_);
					   }
					   public static AttributeSupplier.Builder createAttributes() {
						   return Mob.createMobAttributes()
						    .add(Attributes.FOLLOW_RANGE, 40.0D)
							.add(Attributes.MAX_HEALTH, 20.0D)
							.add(Attributes.MOVEMENT_SPEED, 0.5F)
							.add(Attributes.ATTACK_DAMAGE, 10.0D);
				}
					@Override
					public void registerControllers(AnimationData data) 
					{
						data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
						data.addAnimationController(new AnimationController<>(this, "controller1", 0, this::predicate1));
					}

					@Override
					public AnimationFactory getFactory() 
					{
						return this.factory;
					}

					@Override
					protected void registerGoals() {
						this.goalSelector.addGoal(1, new PanicGoal(this, 2.2D));
						this.goalSelector.addGoal(4, new AvoidEntityGoal<>(this, Player.class, 8.0F, 2.2D, 2.2D));
					      this.goalSelector.addGoal(4, new AvoidEntityGoal<>(this, Monster.class, 4.0F, 2.2D, 2.2D));
					      this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 0.6D));
						  this.goalSelector.addGoal(1, new RandomStrollGoal(this, 1.0D));
						  this.goalSelector.addGoal(2, new MeleGoal(this, 15, 1.25D));
					      this.goalSelector.addGoal(3, new MoveTowardsTargetGoal(this, 0.9D, 32.0F));
					      this.targetSelector.addGoal(6, new HurtByTargetGoal(this));
						  super.registerGoals();
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
				   
				@Override
				public AgeableMob getBreedOffspring(ServerLevel p_146743_, AgeableMob p_146744_) {
					return null;
				
					   }
					}
				