package com.lagadd.biocubes.client.render.entity;

import com.lagadd.biocubes.client.render.model.LapisRobberEntityModel;
import com.lagadd.biocubes.common.entites.LapisRobberEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

		public class LapisRobberEntityRenderer extends GeoEntityRenderer<LapisRobberEntity> {
			
			public LapisRobberEntityRenderer(EntityRendererProvider.Context renderManager) 
			{
				super(renderManager, new LapisRobberEntityModel());
				this.shadowRadius = 0.0F;
			}

			@Override
			public RenderType getRenderType(LapisRobberEntity animatable, float partialTicks, PoseStack stack,
					MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
					ResourceLocation textureLocation) {
				return RenderType.entityTranslucent(getTextureLocation(animatable));
				
				}
		}
