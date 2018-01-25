package com.jamieswhiteshirt.demolitions.common.block;

import com.jamieswhiteshirt.demolitions.Demolitions;
import com.jamieswhiteshirt.demolitions.api.IExplosionScheduler;
import com.jamieswhiteshirt.demolitions.api.IShakeManager;
import com.jamieswhiteshirt.demolitions.api.ShakeSource;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class BlockCoolExplosive extends Block {
    public BlockCoolExplosive(Material materialIn) {
        super(materialIn);
    }

    private void explosionThingy(World world, BlockPos fromPos) {
        if (!world.isRemote) {
            IExplosionScheduler explosionScheduler = world.getCapability(Demolitions.EXPLOSION_SCHEDULER_CAPABILITY, null);
            if (explosionScheduler != null) {
                Set<BlockPos> visited = new HashSet<>();
                Queue<BlockPos> frontier = new LinkedList<>();
                visited.add(fromPos);
                frontier.add(fromPos);

                while (!frontier.isEmpty()) {
                    BlockPos pos = frontier.remove();
                    if (world.getBlockState(pos).getBlock() == this) {
                        explosionScheduler.scheduleExplosion(pos, 250.0F);
                        for (EnumFacing facing : EnumFacing.values()) {
                            BlockPos candidatePos = pos.offset(facing);
                            if (visited.add(candidatePos)) {
                                visited.add(candidatePos);
                                frontier.add(candidatePos);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        explosionThingy(world, pos);
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
        if (world.isBlockPowered(pos)) {
            explosionThingy(world, pos);
        }
    }
}
