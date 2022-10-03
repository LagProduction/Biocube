package com.lagadd.biocubes.client.render.entity;

import com.lagadd.biocubes.client.render.model.IgneoglossaEntityModel;
import com.lagadd.biocubes.common.entities.creatures.IgneoglossaEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

		public class IgneoglossaEntityRenderer extends GeoEntityRenderer<IgneoglossaEntity> {
			
			public IgneoglossaEntityRenderer(EntityRendererProvider.Context renderManager) 
			{
				super(renderManager, new IgneoglossaEntityModel());
				this.shadowRadius = 0.0F;
			}

			@Override
			public RenderType getRenderType(IgneoglossaEntity animatable, float partialTicks, PoseStack stack,
					MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
					ResourceLocation textureLocation) {
				return RenderType.entityTranslucent(getTextureLocation(animatable));
				
				
		    }
		}
