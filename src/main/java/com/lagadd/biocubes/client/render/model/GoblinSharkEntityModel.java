package com.lagadd.biocubes.client.render.model;

	import com.lagadd.biocubes.Biocube;
import com.lagadd.biocubes.common.entites.GoblinSharkEntity;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

		public class GoblinSharkEntityModel extends AnimatedGeoModel <GoblinSharkEntity> {
			
		@Override
		public ResourceLocation getAnimationFileLocation(GoblinSharkEntity entity) 
		{
			return new ResourceLocation(Biocube.MOD_ID, "animations/goblinshark/animation.json");
		}

		@Override
		public ResourceLocation getModelLocation(GoblinSharkEntity entity)
		{
			return new ResourceLocation(Biocube.MOD_ID, "geo/gshark.geo.json");
		}

		@Override
		public ResourceLocation getTextureLocation(GoblinSharkEntity entity)
		{
			return new ResourceLocation(Biocube.MOD_ID, "textures/entity/goblinshark/gshark.png");
	}
}
