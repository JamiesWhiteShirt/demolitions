package com.jamieswhiteshirt.demolitions.server;

import com.jamieswhiteshirt.demolitions.common.CommonProxy;
import com.jamieswhiteshirt.demolitions.common.network.message.ExplosionsMessage;
import com.jamieswhiteshirt.demolitions.common.network.messagehandler.DummyMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.SERVER)
public final class ServerProxy extends CommonProxy {
    @Override
    public SimpleNetworkWrapper createNetworkChannel() {
        SimpleNetworkWrapper networkChannel = super.createNetworkChannel();
        //The system requires a handler, even on a physical side that does not host the logical side that will handle it.
        //To avoid referencing side specific things, I use a dummy message handler.
        networkChannel.registerMessage(new DummyMessageHandler(), ExplosionsMessage.class, 0, Side.CLIENT);
        return networkChannel;
    }
}
