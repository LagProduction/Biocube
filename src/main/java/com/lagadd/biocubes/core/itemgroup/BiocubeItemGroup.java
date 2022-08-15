package com.lagadd.biocubes.core.itemgroup;

import com.lagadd.biocubes.core.init.ItemInit;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class BiocubeItemGroup extends CreativeModeTab {

	public static final BiocubeItemGroup BIOCUBE = new BiocubeItemGroup(CreativeModeTab.TABS.length,
			"biocube");

	public BiocubeItemGroup(int index, String label) {
		super(index, label);
	}
	@Override
	public ItemStack makeIcon() {
		return new ItemStack(ItemInit.GOBLINJAWS.get());
	}
}
