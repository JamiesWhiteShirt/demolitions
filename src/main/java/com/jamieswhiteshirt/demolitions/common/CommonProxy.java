package com.jamieswhiteshirt.demolitions.common;

import com.jamieswhiteshirt.demolitions.Demolitions;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public abstract class CommonProxy {
    public SimpleNetworkWrapper createNetworkChannel() {
        return NetworkRegistry.INSTANCE.newSimpleChannel(Demolitions.MODID);
    }
}
