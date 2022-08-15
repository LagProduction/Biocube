package com.lagadd.biocubes.client.render.model;

	import com.lagadd.biocubes.Biocube;
import com.lagadd.biocubes.common.entites.IgneoglossaEntity;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

		public class IgneoglossaEntityModel extends AnimatedGeoModel <IgneoglossaEntity> {
			
		@Override
		public ResourceLocation getAnimationFileLocation(IgneoglossaEntity entity) 
		{
			return new ResourceLocation(Biocube.MOD_ID, "animations/igneogloaanimations/igneoglossa.animation.json");
		}

		@Override
		public ResourceLocation getModelLocation(IgneoglossaEntity entity)
		{
			return new ResourceLocation(Biocube.MOD_ID, "geo/igneoglossa.geo.json");
		}

		@Override
		public ResourceLocation getTextureLocation(IgneoglossaEntity entity)
		{
			return new ResourceLocation(Biocube.MOD_ID, "textures/entity/igneoglossa/igneoglossa.png");
	}
}
