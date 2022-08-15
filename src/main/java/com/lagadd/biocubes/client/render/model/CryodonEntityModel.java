package com.lagadd.biocubes.client.render.model;

	import com.lagadd.biocubes.Biocube;
import com.lagadd.biocubes.common.entites.CryodonEntity;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

		public class CryodonEntityModel extends AnimatedGeoModel <CryodonEntity> {
			
		@Override
		public ResourceLocation getAnimationFileLocation(CryodonEntity entity) 
		{
			return new ResourceLocation(Biocube.MOD_ID, "animations/cryodonanimations/cryodon.animation.json");
		}

		@Override
		public ResourceLocation getModelLocation(CryodonEntity entity)
		{
			return new ResourceLocation(Biocube.MOD_ID, "geo/stormswimmer.geo.json");
		}

		@Override
		public ResourceLocation getTextureLocation(CryodonEntity entity)
		{
			return new ResourceLocation(Biocube.MOD_ID, "textures/entity/cryodon/stormswimmer.png");
	}
}
