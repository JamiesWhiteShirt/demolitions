package com.jamieswhiteshirt.demolitions.common;

import com.jamieswhiteshirt.demolitions.Demolitions;
import com.jamieswhiteshirt.demolitions.common.block.BlockBlastingMachine;
import com.jamieswhiteshirt.demolitions.common.block.BlockCoolExplosive;
import net.minecraftforge.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(Demolitions.MODID)
public class DemolitionsBlocks {
    public static final BlockCoolExplosive COOL_EXPLOSIVE = Util.nonNullInjected();
    public static final BlockBlastingMachine BLASTING_MACHINE = Util.nonNullInjected();
}
