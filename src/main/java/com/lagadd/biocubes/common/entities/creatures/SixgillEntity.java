package com.lagadd.biocubes.common.entities.creatures;

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
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.BreathAirGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
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
import software.bernie.shadowed.eliotlash.mclib.utils.MathHelper;

				public class SixgillEntity extends Animal implements IAnimatable  {
					private AnimationFactory factory = new AnimationFactory(this);
					   private static final EntityDataAccessor<Integer> MOISTNESS_LEVEL = SynchedEntityData.defineId(Dolphin.class, EntityDataSerializers.INT);
					   public static final int TOTAL_AIR_SUPPLY = 4800;
					   
					   
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
							event.getController().setAnimation(new AnimationBuilder().addAnimation("bite", true));
							return PlayState.CONTINUE;
						}
						return null;
					}
					
					public SixgillEntity(EntityType<? extends Animal> p_i48567_1_, Level p_i48567_2_) {
					      super(p_i48567_1_, p_i48567_2_);
					      this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.02F, 0.1F, true);
					      this.lookControl = new SmoothSwimmingLookControl(this, 10);
					      this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
					      this.maxUpStep = 1.0F;
					   }
					   public static AttributeSupplier.Builder createAttributes() {
						   return Mob.createMobAttributes()
							.add(Attributes.MAX_HEALTH, 24.0D)
							.add(Attributes.MOVEMENT_SPEED, 1.0F)
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
						this.goalSelector.addGoal(1, new GoblinSharkSwimGoal(this, 1.35D, 30) {

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
						  this.goalSelector.addGoal(5, new TemptGoal(this, 1.25D, Ingredient.of(Items.ROTTEN_FLESH), false));
					      this.goalSelector.addGoal(4, new MeleeAttackGoal(this, (double)1.2F, true));
					      this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Monster.class, true));
					      this.goalSelector.addGoal(0, new BreathAirGoal(this));
					      this.goalSelector.addGoal(2, new MoveTowardsTargetGoal(this, 0.9D, 32.0F));
					      this.targetSelector.addGoal(6, new HurtByTargetGoal(this));
						super.registerGoals();
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
				   protected boolean RandomSwim() {
				      return true;
				   }
				   public int getMaxAirSupply() {
					   return 4800;
				   }

				   protected int increaseAirSupply(int p_28389_) {
				       return this.getMaxAirSupply();
				   }
			       public float getWalkTargetValue(BlockPos p_149140_, LevelReader p_149141_) {
					   return 0.0F;
				   }
				   public boolean isPushedByFluid() {
				      return false;
				   }
				   protected float nextStep() {
				      return this.moveDist + 0.15F;
				   }
				   
				   //protected SoundEvent getAmbientSound() {
				    //  return SoundEvents.;
				   //}
				     public int getMoistnessLevel() {
					      return this.entityData.get(MOISTNESS_LEVEL);
					   }

					   public void setMoisntessLevel(int p_28344_) {
					      this.entityData.set(MOISTNESS_LEVEL, p_28344_);
					   }

					   protected void defineSynchedData() {
					      super.defineSynchedData();
					      this.entityData.define(MOISTNESS_LEVEL, 2400);
					   }

					   public void addAdditionalSaveData(CompoundTag compound) {
					      super.addAdditionalSaveData(compound);
					      compound.putInt("Moistness", this.getMoistnessLevel());
					   }

					   public void readAdditionalSaveData(CompoundTag compound) {
					      this.setMoisntessLevel(compound.getInt("Moistness"));
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
				         }
				      }
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
					static class MoveHelperController extends MoveControl {
						private final SixgillEntity sixgill;

						MoveHelperController(SixgillEntity sixgill) {
							super(sixgill);
							this.sixgill = sixgill;
						}

						public void tick() {
							if (this.sixgill.isEyeInFluid(FluidTags.WATER)) {
								this.sixgill.setDeltaMovement(this.sixgill.getDeltaMovement().add(0.0D, 0.005D, 0.0D));
							}

							if (this.operation == MoveControl.Operation.MOVE_TO && !this.sixgill.getNavigation().isDone()) {
								double d0 = this.wantedX - this.sixgill.getX();
								double d1 = this.wantedY - this.sixgill.getY();
								double d2 = this.wantedZ - this.sixgill.getZ();
								double d3 = MathHelper.wrapDegrees(d0 * d0 + d1 * d1 + d2 * d2);
								d1 = d1 / d3;
								float f = (float) (MathHelper.wrapDegrees(d2) * (double) (180F / (float) Math.PI)) - 90.0F;
								this.sixgill.yBodyRot = this.rotlerp(this.sixgill.yBodyRot, f, 90.0F);
								this.sixgill.yBodyRot = this.sixgill.yBodyRot;
								this.sixgill.setSpeed(MathHelper.wrapDegrees(0.125F));
								this.sixgill.setDeltaMovement(this.sixgill.getDeltaMovement().add(0.0D, (double) this.sixgill.getSpeed() * d1 * 0.03D, 0.0D));
							}
						}
					}
				      class GoblinSharkSwimGoal extends RandomSwimmingGoal {
				          public GoblinSharkSwimGoal(SixgillEntity sixgillEntity, double d, int i) {
				             super(sixgillEntity, 1.0D, 40);
				          }
				          
				      }
				}
				  