package com.jamieswhiteshirt.demolitions.common.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExplosionsMessage implements IMessage {
    public List<Tuple<BlockPos, Float>> explosions;

    public ExplosionsMessage(Map<BlockPos, Float> explosions) {
        this.explosions = explosions.entrySet().stream().map(entry -> new Tuple<>(entry.getKey(), entry.getValue())).collect(Collectors.toList());
    }

    public ExplosionsMessage() { }

    @Override
    public void fromBytes(ByteBuf buf) {
        int numExplosions = buf.readUnsignedByte();
        explosions = new ArrayList<>(numExplosions);
        for (int i = 0; i < numExplosions; i++) {
            explosions.add(new Tuple<>(BlockPos.fromLong(buf.readLong()), buf.readFloat()));
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(explosions.size());
        for (Tuple<BlockPos, Float> entry : explosions) {
            buf.writeLong(entry.getFirst().toLong());
            buf.writeFloat(entry.getSecond());
        }
    }
}
