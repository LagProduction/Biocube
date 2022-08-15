package com.lagadd.biocubes.client.render.model;

	import com.lagadd.biocubes.Biocube;
import com.lagadd.biocubes.common.entites.LapisRobberEntity;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

		public class LapisRobberEntityModel extends AnimatedGeoModel <LapisRobberEntity> {
			
		@Override
		public ResourceLocation getAnimationFileLocation(LapisRobberEntity entity) 
		{
			return new ResourceLocation(Biocube.MOD_ID, "animations/lapisrobberanimations/animation.json");
		}

		@Override
		public ResourceLocation getModelLocation(LapisRobberEntity entity)
		{
			return new ResourceLocation(Biocube.MOD_ID, "geo/lapisrobber.geo.json");
		}

		@Override
		public ResourceLocation getTextureLocation(LapisRobberEntity entity)
		{
			return new ResourceLocation(Biocube.MOD_ID, "textures/entity/lapisrobber/lapisrobber.png");
	}
}
