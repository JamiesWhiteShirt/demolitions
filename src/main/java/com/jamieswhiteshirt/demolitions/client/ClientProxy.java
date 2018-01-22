package com.jamieswhiteshirt.demolitions.client;

import com.jamieswhiteshirt.demolitions.Demolitions;
import com.jamieswhiteshirt.demolitions.api.IShakeManager;
import com.jamieswhiteshirt.demolitions.api.ShakeSource;
import com.jamieswhiteshirt.demolitions.common.CommonProxy;
import com.jamieswhiteshirt.demolitions.common.capability.ShakeManagerProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

@SideOnly(Side.CLIENT)
public final class ClientProxy extends CommonProxy {
    @SubscribeEvent
    public void onCameraSetup(EntityViewRenderEvent.CameraSetup event) {
        WorldClient world = Minecraft.getMinecraft().world;
        IShakeManager manager = world.getCapability(Demolitions.SHAKE_MANAGER_CAPABILITY, null);
        if (manager != null) {
            Entity entity = Minecraft.getMinecraft().getRenderViewEntity();
            double partialTicks = event.getRenderPartialTicks();
            Vec3d pos = new Vec3d(
                entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks,
                entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks + entity.getEyeHeight(),
                entity.prevPosZ + (entity.posZ - entity.prevPosZ) * partialTicks
            );

            double accShake = manager.getAll().stream().mapToDouble(shakeSource -> shakeSource.getIntensity() / shakeSource.getPos().distanceTo(pos)).sum() * 0.01;

            if (accShake > 0.01D) {
                long seed = Double.doubleToLongBits(event.getRenderPartialTicks());
                seed ^= Minecraft.getMinecraft().world.getTotalWorldTime();
                Random random = new Random(seed);
                GlStateManager.translate(
                    (random.nextDouble() - random.nextDouble()) * accShake,
                    (random.nextDouble() - random.nextDouble()) * accShake,
                    (random.nextDouble() - random.nextDouble()) * accShake
                );
            }
        }
    }

    @SubscribeEvent
    public void attachWorldCapabilities(AttachCapabilitiesEvent<World> event) {
        World world = event.getObject();
        if (world.isRemote) {
            event.addCapability(new ResourceLocation(Demolitions.MODID, "shake_manager"), new ShakeManagerProvider());
        }
    }

    private final Random particleRandom = new Random();

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && !Minecraft.getMinecraft().isGamePaused()) {
            WorldClient world = Minecraft.getMinecraft().world;
            if (world != null) {
                EntityPlayerSP player = Minecraft.getMinecraft().player;
                IShakeManager manager = world.getCapability(Demolitions.SHAKE_MANAGER_CAPABILITY, null);
                if (manager != null) {
                    manager.tick();

                    AxisAlignedBB bb = player.getEntityBoundingBox().grow(10.0, 10.0, 10.0);
                    ParticleManager particleManager = Minecraft.getMinecraft().effectRenderer;
                    for (ShakeSource source : manager.getAll()) {
                        double intensity = source.getIntensity();
                        int numParticles = (int) Math.floor(intensity * intensity * 0.125);
                        for (int i = 0; i < numParticles; i++) {
                            Vec3d tryVec = source.getPos().add(new Vec3d(
                                particleRandom.nextDouble() - particleRandom.nextDouble(),
                                particleRandom.nextDouble() - particleRandom.nextDouble(),
                                particleRandom.nextDouble() - particleRandom.nextDouble()
                            ).scale(Math.sqrt(intensity)));
                            if (bb.contains(tryVec)) {
                                BlockPos pos = new BlockPos(tryVec);
                                EnumFacing facing = EnumFacing.values()[particleRandom.nextInt(EnumFacing.values().length)];
                                if (!world.isBlockFullCube(pos.offset(facing))) {
                                    particleManager.addBlockHitEffects(pos, facing);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
