package com.lagadd.biocubes.client.render.model;

	import com.lagadd.biocubes.Biocube;
import com.lagadd.biocubes.common.entites.SixgillEntity;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

		public class SixgillEntityModel extends AnimatedGeoModel <SixgillEntity> {
			
		@Override
		public ResourceLocation getAnimationFileLocation(SixgillEntity entity) 
		{
			return new ResourceLocation(Biocube.MOD_ID, "animations/sixgillanimation/animation.json");
		}

		@Override
		public ResourceLocation getModelLocation(SixgillEntity entity)
		{
			return new ResourceLocation(Biocube.MOD_ID, "geo/sixgill.geo.json");
		}

		@Override
		public ResourceLocation getTextureLocation(SixgillEntity entity)
		{
			return new ResourceLocation(Biocube.MOD_ID, "textures/entity/sixgill/sixgill.png");
	}
}
