package com.lagadd.biocubes.client.render.model;

import com.lagadd.biocubes.Biocube;
import com.lagadd.biocubes.common.entities.creatures.cryodon.Cryodon;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class CryodonEntityModel extends AnimatedGeoModel<Cryodon> {

    @Override
    public ResourceLocation getAnimationFileLocation(Cryodon entity) {
        if (entity.getAreaStun()) {
            return new ResourceLocation(Biocube.MOD_ID, "animations/cryodonanimations/cryodon_charged.animation.json");
        }
        return new ResourceLocation(Biocube.MOD_ID, "animations/cryodonanimations/cryodon.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(Cryodon entity) {
        if (entity.getAreaStun()) {
            return new ResourceLocation(Biocube.MOD_ID, "geo/stormswimmer_charged.geo.json");
        } else {
            return new ResourceLocation(Biocube.MOD_ID, "geo/stormswimmer_normal.geo.json");
        }
    }

    @Override
    public ResourceLocation getTextureLocation(Cryodon entity) {
        return new ResourceLocation(Biocube.MOD_ID, "textures/entity/cryodon/stormswimmer.png");
    }
}
