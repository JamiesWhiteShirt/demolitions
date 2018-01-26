package com.jamieswhiteshirt.demolitions.common;

import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder("demolitions")
public class DemolitionsItems {
    public static final ItemBlock BLASTING_MACHINE = Util.nonNullInjected();
}
