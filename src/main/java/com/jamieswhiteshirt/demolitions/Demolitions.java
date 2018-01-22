package com.jamieswhiteshirt.demolitions;

import com.jamieswhiteshirt.demolitions.api.IShakeManager;
import com.jamieswhiteshirt.demolitions.common.Util;
import com.jamieswhiteshirt.demolitions.common.capability.DummyFactory;
import com.jamieswhiteshirt.demolitions.common.capability.DummyStorage;
import com.jamieswhiteshirt.demolitions.common.CommonProxy;
import com.jamieswhiteshirt.demolitions.common.DemolitionsBlocks;
import com.jamieswhiteshirt.demolitions.common.block.BlockCoolExplosive;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(
    modid = Demolitions.MODID,
    version = Demolitions.VERSION,
    name = "Demolitions",
    dependencies = "required-after:demolitions_core"
)
public class Demolitions {
    public static final String MODID = "demolitions";
    public static final String VERSION = "1.12-0.0.0.0";

    @Mod.Instance
    public static Demolitions instance;
    @SidedProxy(
        clientSide = "com.jamieswhiteshirt.demolitions.client.ClientProxy",
        serverSide = "com.jamieswhiteshirt.demolitions.client.ServerProxy",
        modId = MODID
    )
    public static CommonProxy proxy;

    @CapabilityInject(IShakeManager.class)
    public static final Capability<IShakeManager> SHAKE_MANAGER_CAPABILITY = Util.nonNullInjected();

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(proxy);
        MinecraftForge.EVENT_BUS.register(this);
        CapabilityManager.INSTANCE.register(IShakeManager.class, new DummyStorage<>(), new DummyFactory<>());
    }

    private static <T extends Block> T withBoth(T entry, String name) {
        entry.setRegistryName(MODID, name);
        entry.setUnlocalizedName(MODID + "." + name);
        return entry;
    }

    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(
            withBoth(new BlockCoolExplosive(Material.TNT), "cool_explosive").setBlockUnbreakable().setResistance(6000000.0F)
        );
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
            new ItemBlock(DemolitionsBlocks.COOL_EXPLOSIVE).setRegistryName(MODID, "cool_explosive")
        );
    }
}
