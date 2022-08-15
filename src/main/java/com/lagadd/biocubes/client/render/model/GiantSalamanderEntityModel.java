package com.lagadd.biocubes.client.render.model;

	import com.lagadd.biocubes.Biocube;
import com.lagadd.biocubes.common.entites.GiantSalamanderEntity;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

		public class GiantSalamanderEntityModel extends AnimatedGeoModel <GiantSalamanderEntity> {
			
		@Override
		public ResourceLocation getAnimationFileLocation(GiantSalamanderEntity entity) 
		{
			return new ResourceLocation(Biocube.MOD_ID, "animations/giantsalamanderanimations/giantsalamander.animation.json");
		}

		@Override
		public ResourceLocation getModelLocation(GiantSalamanderEntity entity)
		{
			return new ResourceLocation(Biocube.MOD_ID, "geo/giantsalamander.geo.json");
		}

		@Override
		public ResourceLocation getTextureLocation(GiantSalamanderEntity entity)
		{
			return new ResourceLocation(Biocube.MOD_ID, "textures/entity/giantsalamander/giantsalamander.png");
	}
}
