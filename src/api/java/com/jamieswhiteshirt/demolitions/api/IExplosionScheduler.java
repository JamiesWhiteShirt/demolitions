package com.jamieswhiteshirt.demolitions.api;

import net.minecraft.util.math.BlockPos;

public interface IExplosionScheduler {
    void scheduleExplosion(BlockPos pos, float energy);

    void tick();
}
