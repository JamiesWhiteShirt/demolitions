package com.jamieswhiteshirt.demolitions.common.impl;

import com.jamieswhiteshirt.demolitions.Demolitions;
import com.jamieswhiteshirt.demolitions.api.IExplosionScheduler;
import com.jamieswhiteshirt.demolitions.common.network.message.ExplosionsMessage;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

import java.util.*;

public class ExplosionScheduler implements IExplosionScheduler {
    private final WorldServer world;
    private final Map<BlockPos, Float> scheduledExplosions = new LinkedHashMap<>();

    public ExplosionScheduler(WorldServer world) {
        this.world = world;
    }

    @Override
    public void scheduleExplosion(BlockPos pos, float energy) {
        scheduledExplosions.put(pos, energy);
    }

    final class Entry {
        final BlockPos pos;
        float absorbed, px, py, pz, nx, ny, nz;

        Entry(BlockPos pos) {
            this.pos = pos;
        }
    }

    @Override
    public void tick() {
        if (scheduledExplosions.size() > 0) {
            Map<BlockPos, Entry> entries = new HashMap<>(4000);
            Queue<Entry> queue = new LinkedList<>();
            for (Map.Entry<BlockPos, Float> entry : scheduledExplosions.entrySet()) {
                BlockPos pos = entry.getKey();
                Entry initialEntry = new Entry(pos);
                float energy = entry.getValue();
                initialEntry.px = energy * 1.0F / 6.0F;
                initialEntry.py = energy * 1.0F / 6.0F;
                initialEntry.pz = energy * 1.0F / 6.0F;
                initialEntry.nx = energy * 1.0F / 6.0F;
                initialEntry.ny = energy * 1.0F / 6.0F;
                initialEntry.nz = energy * 1.0F / 6.0F;
                entries.put(pos, initialEntry);
                queue.add(initialEntry);
            }

            while (!queue.isEmpty()) {
                final Entry entry = queue.remove();
                final BlockPos pos = entry.pos;
                final IBlockState state = world.getBlockState(pos);
                final float hardness = state.getBlockHardness(world, pos);
                final float a = 1.0F / (1.01F + hardness * hardness * hardness * hardness * 1.0F);
                //final float a = state.isFullCube() ? 0.00025F : 0.95F;
                final float ia = (1.0F - a) / 5.0F;

                entry.absorbed += (entry.px + entry.py + entry.pz + entry.nx + entry.ny + entry.nz) * a;
                float px = entry.px * ia;
                float py = entry.py * ia;
                float pz = entry.pz * ia;
                float nx = entry.nx * ia;
                float ny = entry.ny * ia;
                float nz = entry.nz * ia;

                if (entry.px > 0.001F) {
                    Entry otherEntry = entries.computeIfAbsent(pos.add(1, 0, 0), Entry::new);
                    otherEntry.px += px;
                    otherEntry.py += py;
                    otherEntry.pz += pz;
                    otherEntry.ny += ny;
                    otherEntry.nz += nz;
                    queue.add(otherEntry);
                    entry.px = 0.0F;
                }

                if (entry.py > 0.001F) {
                    Entry otherEntry = entries.computeIfAbsent(pos.add(0, 1, 0), Entry::new);
                    otherEntry.px += px;
                    otherEntry.py += py;
                    otherEntry.pz += pz;
                    otherEntry.nx += nx;
                    otherEntry.nz += nz;
                    queue.add(otherEntry);
                    entry.py = 0.0F;
                }

                if (entry.pz > 0.001F) {
                    Entry otherEntry = entries.computeIfAbsent(pos.add(0, 0, 1), Entry::new);
                    otherEntry.px += px;
                    otherEntry.py += py;
                    otherEntry.pz += pz;
                    otherEntry.nx += nx;
                    otherEntry.ny += ny;
                    queue.add(otherEntry);
                    entry.pz = 0.0F;
                }

                if (entry.nx > 0.001F) {
                    Entry otherEntry = entries.computeIfAbsent(pos.add(-1, 0, 0), Entry::new);
                    otherEntry.py += py;
                    otherEntry.pz += pz;
                    otherEntry.nx += nx;
                    otherEntry.ny += ny;
                    otherEntry.nz += nz;
                    queue.add(otherEntry);
                    entry.nx = 0.0F;
                }

                if (entry.ny > 0.001F) {
                    Entry otherEntry = entries.computeIfAbsent(pos.add(0, -1, 0), Entry::new);
                    otherEntry.px += px;
                    otherEntry.pz += pz;
                    otherEntry.nx += nx;
                    otherEntry.ny += ny;
                    otherEntry.nz += nz;
                    queue.add(otherEntry);
                    entry.ny = 0.0F;
                }

                if (entry.nz > 0.001F) {
                    Entry otherEntry = entries.computeIfAbsent(pos.add(0, 0, -1), Entry::new);
                    otherEntry.px += px;
                    otherEntry.py += py;
                    otherEntry.nx += nx;
                    otherEntry.ny += ny;
                    otherEntry.nz += nz;
                    queue.add(otherEntry);
                    entry.nz = 0.0F;
                }
            }

            for (Map.Entry<BlockPos, Entry> entry : entries.entrySet()) {
                BlockPos pos = entry.getKey();
                IBlockState state = world.getBlockState(pos);
                if (entry.getValue().absorbed > 0.005F * state.getBlockHardness(world, pos)) {
                    if (state.getMaterial() != Material.AIR)
                    {
                        //Block block = state.getBlock();
                        //block.dropBlockAsItemWithChance(this.world, pos, state, 1.0F, 0);
                        world.setBlockToAir(pos);
                    }
                }
            }

            Demolitions.instance.networkChannel.sendToDimension(new ExplosionsMessage(scheduledExplosions), world.provider.getDimension());
            scheduledExplosions.clear();
        }
    }
}
