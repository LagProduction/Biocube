package com.lagadd.biocubes.core.init;

import com.lagadd.biocubes.Biocube;
import com.lagadd.biocubes.core.itemgroup.BiocubeItemGroup;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
			Biocube.MOD_ID);

    public static final RegistryObject<Item> GOBLINJAWS = ITEMS.register( "goblinjaws_item",
            () -> new Item( new Item.Properties().tab( BiocubeItemGroup.BIOCUBE ) ) );
    
    public static final RegistryObject<Item> MISCELLANEOUSMEAT = ITEMS.register( "miscellaneousmeat_item",
            () -> new Item( new Item.Properties().tab( BiocubeItemGroup.BIOCUBE ) ) );
    
    public static final RegistryObject<Item> HIPPOTUSK = ITEMS.register( "hippotusk_item",
            () -> new Item( new Item.Properties().tab( BiocubeItemGroup.BIOCUBE ) ) );
    
    public static final RegistryObject<Item> OBSCURELENS = ITEMS.register( "obscurelens_item",
            () -> new Item( new Item.Properties().tab( BiocubeItemGroup.BIOCUBE ) ) );
    
    public static final RegistryObject<Item> LAPISCARAPACE = ITEMS.register( "lapiscarapace_item",
            () -> new Item( new Item.Properties().tab( BiocubeItemGroup.BIOCUBE ) ) );
    
    public static final RegistryObject<Item> EEL = ITEMS.register( "eel_item",
            () -> new Item( new Item.Properties().tab( BiocubeItemGroup.BIOCUBE ) ) );
    
    public static final RegistryObject<Item> CROCSCUTE = ITEMS.register( "crocscute_item",
            () -> new Item( new Item.Properties().tab( BiocubeItemGroup.BIOCUBE ) ) );
    
    public static final RegistryObject<Item> FROZENFLESH = ITEMS.register( "frozenflesh_item",
            () -> new Item( new Item.Properties().tab( BiocubeItemGroup.BIOCUBE ) ) );
    
    public static final RegistryObject<Item> SPINESPEAR = ITEMS.register( "spinespear_item",
            () -> new Item( new Item.Properties().tab( BiocubeItemGroup.BIOCUBE ) ) );
    
    public static final RegistryObject<Item> SHARKTOOTH = ITEMS.register( "sharktooth_item",
            () -> new Item( new Item.Properties().tab( BiocubeItemGroup.BIOCUBE ) ) );
    
    public static final RegistryObject<Item> RAWTUNA = ITEMS.register( "rawtuna_item",
            () -> new Item( new Item.Properties().tab( BiocubeItemGroup.BIOCUBE ) ) );
    
    public static final RegistryObject<Item> COOKEDTUNA = ITEMS.register( "cookedtuna_item",
            () -> new Item( new Item.Properties().tab( BiocubeItemGroup.BIOCUBE ) ) );
    
    public static final RegistryObject<Item> FLESHOFSLEEPER = ITEMS.register( "fleshofsleeper_item",
            () -> new Item( new Item.Properties().tab( BiocubeItemGroup.BIOCUBE ) ) );
    
    public static final RegistryObject<Item> FERMENTEDSLEEPER = ITEMS.register( "fermentedsleeper_item",
            () -> new Item( new Item.Properties().tab( BiocubeItemGroup.BIOCUBE ) ) );
}