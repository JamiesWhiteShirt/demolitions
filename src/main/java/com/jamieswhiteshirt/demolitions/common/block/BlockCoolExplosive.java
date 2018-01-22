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

public class BlockCoolExplosive extends Block {
    public BlockCoolExplosive(Material materialIn) {
        super(materialIn);
    }

    private void explosionThingy(World world, BlockPos pos) {
        if (!world.isRemote) {
            IExplosionScheduler explosionScheduler = world.getCapability(Demolitions.EXPLOSION_SCHEDULER_CAPABILITY, null);
            if (explosionScheduler != null) {
                explosionScheduler.scheduleExplosion(pos, 5.0F);
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
