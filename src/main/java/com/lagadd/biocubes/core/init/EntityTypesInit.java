package com.lagadd.biocubes.core.init;

import com.lagadd.biocubes.common.entites.CryodonEntity;
import com.lagadd.biocubes.common.entites.GiantSalamanderEntity;
import com.lagadd.biocubes.common.entites.GoblinSharkEntity;
import com.lagadd.biocubes.common.entites.IgneoglossaEntity;
import com.lagadd.biocubes.common.entites.LapisRobberEntity;
import com.lagadd.biocubes.common.entites.SixgillEntity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityTypesInit {

	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES,
			com.lagadd.biocubes.Biocube.MOD_ID);
	
	public static final RegistryObject<EntityType<GiantSalamanderEntity>> GiantSalamander = ENTITY_TYPES.register("giantsalamander",
			() -> EntityType.Builder.<GiantSalamanderEntity>of(GiantSalamanderEntity::new, MobCategory.MISC)
		    .sized(3.0f,0.8f)
		    .build(new ResourceLocation(com.lagadd.biocubes.Biocube.MOD_ID, "giantsalamander").toString()));
	
	public static final RegistryObject<EntityType<GoblinSharkEntity>> GoblinShark = ENTITY_TYPES.register("goblinshark",
			() -> EntityType.Builder.<GoblinSharkEntity>of(GoblinSharkEntity::new, MobCategory.MISC)
		    .sized(3.0f,1.0f)
		    .build(new ResourceLocation(com.lagadd.biocubes.Biocube.MOD_ID, "goblinshark").toString()));
	
	public static final RegistryObject<EntityType<SixgillEntity>> Sixgill = ENTITY_TYPES.register("sixgill",
			() -> EntityType.Builder.<SixgillEntity>of(SixgillEntity::new, MobCategory.MISC)
		    .sized(3.0f,1.0f)
		    .build(new ResourceLocation(com.lagadd.biocubes.Biocube.MOD_ID, "sixgill").toString()));
	
	public static final RegistryObject<EntityType<CryodonEntity>> Cryodon = ENTITY_TYPES.register("cryodon",
			() -> EntityType.Builder.<CryodonEntity>of(CryodonEntity::new, MobCategory.MISC)
		    .sized(3.0f,3.0f)
		    .build(new ResourceLocation(com.lagadd.biocubes.Biocube.MOD_ID, "cryodon").toString()));
	
	public static final RegistryObject<EntityType<LapisRobberEntity>> LapisRobber = ENTITY_TYPES.register("lapisrobber",
			() -> EntityType.Builder.<LapisRobberEntity>of(LapisRobberEntity::new, MobCategory.MISC)
		    .sized(1.0f,2.0f)
		    .build(new ResourceLocation(com.lagadd.biocubes.Biocube.MOD_ID, "lapisrobber").toString()));
	
	public static final RegistryObject<EntityType<IgneoglossaEntity>> Igneoglossa = ENTITY_TYPES.register("igneoglossa",
			() -> EntityType.Builder.<IgneoglossaEntity>of(IgneoglossaEntity::new, MobCategory.MISC)
		    .sized(4.0f,1.0f)
		    .build(new ResourceLocation(com.lagadd.biocubes.Biocube.MOD_ID, "lapisrobber").toString()));
	
}