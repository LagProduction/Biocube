package com.lagadd.biocubes.core.init;

import com.lagadd.biocubes.Biocube;
import com.lagadd.biocubes.common.entities.creatures.*;
import com.lagadd.biocubes.common.entities.creatures.cryodon.Cryodon;
import com.lagadd.biocubes.common.entities.creatures.sixgill.Sixgill;
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
                    .sized(3.0f, 0.8f)
                    .build(new ResourceLocation(com.lagadd.biocubes.Biocube.MOD_ID, "giantsalamander").toString()));

    public static final RegistryObject<EntityType<GoblinSharkEntity>> GoblinShark = ENTITY_TYPES.register("goblinshark",
            () -> EntityType.Builder.<GoblinSharkEntity>of(GoblinSharkEntity::new, MobCategory.MISC)
                    .sized(3.0f, 1.0f)
                    .build(new ResourceLocation(com.lagadd.biocubes.Biocube.MOD_ID, "goblinshark").toString()));

    public static final RegistryObject<EntityType<Sixgill>> Sixgill = ENTITY_TYPES.register("sixgill",
            () -> EntityType.Builder.<Sixgill>of(Sixgill::new, MobCategory.WATER_CREATURE)
                    .sized(3.0f, 1.0f)
                    .build(new ResourceLocation(Biocube.MOD_ID, "sixgill").toString()));

    public static final RegistryObject<EntityType<Cryodon>> Cryodon = ENTITY_TYPES.register("cryodon",
            () -> EntityType.Builder.of(Cryodon::new, MobCategory.MISC)
                    .sized(3.0f, 3.0f)
                    .build(new ResourceLocation(com.lagadd.biocubes.Biocube.MOD_ID, "cryodon").toString()));

    public static final RegistryObject<EntityType<LapisRobberEntity>> LapisRobber = ENTITY_TYPES.register("lapisrobber",
            () -> EntityType.Builder.<LapisRobberEntity>of(LapisRobberEntity::new, MobCategory.MISC)
                    .sized(1.0f, 2.0f)
                    .build(new ResourceLocation(com.lagadd.biocubes.Biocube.MOD_ID, "lapisrobber").toString()));

    public static final RegistryObject<EntityType<IgneoglossaEntity>> Igneoglossa = ENTITY_TYPES.register("igneoglossa",
            () -> EntityType.Builder.<IgneoglossaEntity>of(IgneoglossaEntity::new, MobCategory.MISC)
                    .sized(4.0f, 1.0f)
                    .build(new ResourceLocation(com.lagadd.biocubes.Biocube.MOD_ID, "lapisrobber").toString()));

}