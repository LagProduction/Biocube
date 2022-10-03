package com.lagadd.biocubes;

import com.lagadd.biocubes.common.entities.creatures.*;
import com.lagadd.biocubes.common.entities.creatures.cryodon.Cryodon;
import com.lagadd.biocubes.core.init.BlockInit;
import com.lagadd.biocubes.core.init.EntityTypesInit;
import com.lagadd.biocubes.core.init.ItemInit;
import com.lagadd.biocubes.core.itemgroup.BiocubeItemGroup;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.example.GeckoLibMod;
import software.bernie.geckolib3.GeckoLib;

	  @Mod("biocube")
	  @Mod.EventBusSubscriber(modid = Biocube.MOD_ID, bus = Bus.MOD)
	   	public class Biocube {

	    	public static final Logger LOGGER = LogManager.getLogger();
	    	public static final String MOD_ID = "biocube";

	    	public Biocube() {
	    		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
	          bus.addListener(this::entityAttributesRegistration);
	          
	          GeckoLibMod.DISABLE_IN_DEV = true;
	          GeckoLib.initialize();

		  	 EntityTypesInit.ENTITY_TYPES.register(bus);
		   	 ItemInit.ITEMS.register(bus);
	    		
	    		MinecraftForge.EVENT_BUS.register(this);
	    	}
	    	@SubscribeEvent
	    	public static void onRegisterItems(final RegistryEvent.Register<Item> event) {
	    		BlockInit.BLOCKS.getEntries().stream().map(RegistryObject::get).forEach(block -> {
	    			event.getRegistry().register(new BlockItem(block, new Item.Properties().tab(BiocubeItemGroup.BIOCUBE))
	    					.setRegistryName(block.getRegistryName()));
	    				});
	   }
	  	  	private void entityAttributesRegistration(EntityAttributeCreationEvent event) {
	          event.put(EntityTypesInit.GiantSalamander.get(), GiantSalamanderEntity.createAttributes().build());
	          event.put(EntityTypesInit.GoblinShark.get(), GoblinSharkEntity.createAttributes().build());
	          event.put(EntityTypesInit.Sixgill.get(), SixgillEntity.createAttributes().build());
	          event.put(EntityTypesInit.Cryodon.get(), Cryodon.createAttributes().build());
	          event.put(EntityTypesInit.LapisRobber.get(), LapisRobberEntity.createAttributes().build());
	          event.put(EntityTypesInit.Igneoglossa.get(), IgneoglossaEntity.createAttributes().build());
	  	  	}
	  }
