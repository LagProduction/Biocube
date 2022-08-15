package com.lagadd.biocubes.client.util;

import com.lagadd.biocubes.Biocube;
import com.lagadd.biocubes.client.render.entity.CryodonEntityRenderer;
import com.lagadd.biocubes.client.render.entity.GiantSalamanderEntityRenderer;
import com.lagadd.biocubes.client.render.entity.GoblinSharkEntityRenderer;
import com.lagadd.biocubes.client.render.entity.IgneoglossaEntityRenderer;
import com.lagadd.biocubes.client.render.entity.LapisRobberEntityRenderer;
import com.lagadd.biocubes.client.render.entity.SixgillEntityRenderer;
import com.lagadd.biocubes.core.init.EntityTypesInit;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = Biocube.MOD_ID, bus = Bus.MOD, value = Dist.CLIENT)
public class ClientEventBusSubscriber {

	public static void registerRenderers(final FMLClientSetupEvent event) { }
	@SubscribeEvent
	public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(EntityTypesInit.GiantSalamander.get(), GiantSalamanderEntityRenderer::new);
		event.registerEntityRenderer(EntityTypesInit.GoblinShark.get(), GoblinSharkEntityRenderer::new);
		event.registerEntityRenderer(EntityTypesInit.Sixgill.get(), SixgillEntityRenderer::new);
		event.registerEntityRenderer(EntityTypesInit.Cryodon.get(), CryodonEntityRenderer::new);
		event.registerEntityRenderer(EntityTypesInit.LapisRobber.get(), LapisRobberEntityRenderer::new);
		event.registerEntityRenderer(EntityTypesInit.Igneoglossa.get(), IgneoglossaEntityRenderer::new);



	}
}