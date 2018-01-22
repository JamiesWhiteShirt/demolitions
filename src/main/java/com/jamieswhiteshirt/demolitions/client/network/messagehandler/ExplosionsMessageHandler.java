package com.jamieswhiteshirt.demolitions.client.network.messagehandler;

import com.jamieswhiteshirt.demolitions.Demolitions;
import com.jamieswhiteshirt.demolitions.api.IShakeManager;
import com.jamieswhiteshirt.demolitions.api.ShakeSource;
import com.jamieswhiteshirt.demolitions.common.network.message.ExplosionsMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
public class ExplosionsMessageHandler implements IMessageHandler<ExplosionsMessage, IMessage> {
    @Nullable
    @Override
    public IMessage onMessage(ExplosionsMessage message, MessageContext ctx) {
        Minecraft.getMinecraft().addScheduledTask(() -> {
            World world = Minecraft.getMinecraft().world;
            if (world != null) {
                IShakeManager manager = world.getCapability(Demolitions.SHAKE_MANAGER_CAPABILITY, null);
                if (manager != null) {
                    for (Tuple<BlockPos, Float> explosion : message.explosions) {
                        BlockPos pos = explosion.getFirst();
                        float intensity = explosion.getSecond();
                        manager.add(new ShakeSource(new Vec3d(pos).addVector(0.5, 0.5, 0.5), intensity));
                    }
                }
            }
        });
        return null;
    }
}
