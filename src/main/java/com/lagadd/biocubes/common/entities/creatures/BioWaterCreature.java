package com.lagadd.biocubes.common.entities.creatures;

import com.lagadd.biocubes.common.entities.BiocubeCreature;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

import java.util.Random;

public abstract class BioWaterCreature extends BiocubeCreature {

    private static final EntityDataAccessor<Integer> airsupply = SynchedEntityData.defineId(BioWaterCreature.class, EntityDataSerializers.INT);

    public BioWaterCreature(EntityType<? extends BioWaterCreature> entityType, Level level) {
        super(entityType, level);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
    }

    public int maxAirSupply() {
        return 300;
    }

    public int getAirSupply() {
        return this.entityData.get(airsupply);
    }

    public void setAirSupply(int var) {
        this.entityData.set(airsupply, var);
    }

    protected void handleAirSupply(int p_30344_) {
        if (this.isAlive() && !this.isInWaterOrBubble()) {
            this.setAirSupply(p_30344_ - 1);
            if (this.getAirSupply() == -20) {
                this.setAirSupply(0);
                this.hurt(DamageSource.DROWN, 2.0F);
            }
        } else {
            this.setAirSupply(300);
        }
    }

    @Override
    public void baseTick() {
        super.baseTick();
        this.handleAirSupply(this.getAirSupply());
    }

    public static boolean checkSurfaceWaterAnimalSpawnRules(EntityType<? extends WaterAnimal> p_186238_, LevelAccessor p_186239_, MobSpawnType p_186240_, BlockPos p_186241_, Random p_186242_) {
        int i = p_186239_.getSeaLevel();
        int j = i - 13;
        return p_186241_.getY() >= j && p_186241_.getY() <= i && p_186239_.getFluidState(p_186241_.below()).is(FluidTags.WATER) && p_186239_.getBlockState(p_186241_.above()).is(Blocks.WATER);
    }

    @Override
    public MobType getMobType() {
        return MobType.WATER;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }
}