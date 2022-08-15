package com.lagadd.biocubes.common.entites;

	import java.util.Map;

import com.mojang.math.Vector3f;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.BreathAirGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.TryFindWaterGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
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

				public class GiantSalamanderEntity extends Animal implements IAnimatable  {
					private AnimationFactory factory = new AnimationFactory(this);

					   private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
						if (this.isInWater() && this.isUnderWater() ) {
							event.getController().setAnimation(new AnimationBuilder().addAnimation("swimming normal", true));
							return PlayState.CONTINUE;			
						}						
						if (this.isOnGround()) {
							event.getController().setAnimation(new AnimationBuilder().addAnimation("walking normal", true));
							return PlayState.CONTINUE;
						}
						return null;
					  }
					   private <E extends IAnimatable> PlayState predicate1(AnimationEvent<E> event) {
						
						if (this.isAggressive() && !(this.dead || this.getHealth() < 0.01 || this.isDeadOrDying())) {
							event.getController().setAnimation(new AnimationBuilder().addAnimation("bite chonk", true));
							return PlayState.CONTINUE;
						}
						event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", true));
						return PlayState.CONTINUE;
					}
					
					public GiantSalamanderEntity(EntityType<? extends Animal> p_i48567_1_, Level p_i48567_2_) {
					      super(p_i48567_1_, p_i48567_2_);
					      this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.02F, 0.1F, true);
					      this.lookControl = new SmoothSwimmingLookControl(this, 10);
					      this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
					      this.maxUpStep = 1.0F;
					}
				   public static AttributeSupplier.Builder createAttributes() {
					   return Mob.createMobAttributes()
							.add(Attributes.MAX_HEALTH, 24.0D)
							.add(Attributes.MOVEMENT_SPEED, 0.07F)
							.add(Attributes.ATTACK_DAMAGE, 3.0D);
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
						this.goalSelector.addGoal(4, new RandomSwimmingGoal(this, 1.35D, 30) {

							@Override
							public boolean canUse() {
								if (this.mob.isVehicle()) {
									return false;
								} else {
									if (!this.forceTrigger) {
										if (this.mob.getNoActionTime() >= 100) {
											return false;
										}
										else {
											if (this.mob.getRandom().nextInt(30) != 0) {
												return false;
											}
										}
									}

									Vec3 vec3d = this.getPosition();
									if (vec3d == null) {
										return false;
									} else {
										this.wantedX = vec3d.x;
										this.wantedY = vec3d.y;
										this.wantedZ = vec3d.z;
										this.forceTrigger = false;
										return true;
									}
								}
							}

						});
						  this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
					      this.goalSelector.addGoal(0, new BreathAirGoal(this));
					      this.goalSelector.addGoal(1, new TryFindWaterGoal(this));

						super.registerGoals();
					}
					
					   public Map<String, Vector3f> getModelRotationValues() {
						   return this.getModelRotationValues();
					   }
					   protected PathNavigation createNavigation(Level p_27480_) {
					      return new WaterBoundPathNavigation(this, p_27480_);
					   }
					   protected boolean RandomSwim() {
					      return true;
					   }
				       public float getWalkTargetValue(BlockPos p_149140_, LevelReader p_149141_) {
						   return 0.0F;
					   }
					   public boolean canBreatheUnderwater() {
					      return true;
					   }
					   public boolean isPushedByFluid() {
					      return false;
					   }
					   protected float nextStep() {
					      return this.moveDist + 0.15F;
					   }
					   protected void handleAirSupply(int p_28326_) {
					   }
				   protected SoundEvent getAmbientSound() {
				     return SoundEvents.AXOLOTL_IDLE_WATER;
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
				   protected boolean closeToNextPos() {
					      BlockPos blockpos = this.getNavigation().getTargetPos();
					      return blockpos != null ? blockpos.closerToCenterThan(this.position(), 12.0D) : false;
					   }
					public void travel(Vec3 p_213352_1_) {
						if (this.isEffectiveAi() && this.isInWater()) {
							this.moveRelative(0.01F, p_213352_1_);
							this.move(MoverType.SELF, this.getDeltaMovement());
							this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
							if (this.getTarget() == null) {
								this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.005D, 0.0D));
							}
						} else {
							super.travel(p_213352_1_);
						}
					}
					   }
				      class GiantSalamanderSwimGoal extends RandomSwimmingGoal {
				          public GiantSalamanderSwimGoal(PathfinderMob GiantSalamanderEntity, double d, int i) {
				             super(GiantSalamanderEntity, 1.0D, 40);
				          }
				      }
				