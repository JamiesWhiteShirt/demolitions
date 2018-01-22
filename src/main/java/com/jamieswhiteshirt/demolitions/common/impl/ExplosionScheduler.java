package com.jamieswhiteshirt.demolitions.common.impl;

import com.jamieswhiteshirt.demolitions.Demolitions;
import com.jamieswhiteshirt.demolitions.api.IExplosionScheduler;
import com.jamieswhiteshirt.demolitions.common.network.message.ExplosionsMessage;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

import java.util.LinkedHashMap;
import java.util.Map;

public class ExplosionScheduler implements IExplosionScheduler {
    private final WorldServer world;
    private final Map<BlockPos, Float> scheduledExplosions = new LinkedHashMap<>();

    public ExplosionScheduler(WorldServer world) {
        this.world = world;
    }

    @Override
    public void scheduleExplosion(BlockPos pos, float intensity) {
        scheduledExplosions.put(pos, intensity);
    }

    @Override
    public void tick() {
        for (Map.Entry<BlockPos, Float> entry : scheduledExplosions.entrySet()) {
            BlockPos pos = entry.getKey();
            float intensity = entry.getValue();
            world.createExplosion(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, intensity, false);
        }
        Demolitions.instance.networkChannel.sendToDimension(new ExplosionsMessage(scheduledExplosions), world.provider.getDimension());
        scheduledExplosions.clear();
    }
}
