package com.jamieswhiteshirt.demolitions.common.capability;

import com.jamieswhiteshirt.demolitions.api.IExplosionScheduler;
import com.jamieswhiteshirt.demolitions.common.Util;
import com.jamieswhiteshirt.demolitions.common.impl.ExplosionScheduler;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;

public class ExplosionSchedulerProvider implements ICapabilityProvider {
    @CapabilityInject(IExplosionScheduler.class)
    private static final Capability<IExplosionScheduler> EXPLOSION_SCHEDULER_CAPABILITY = Util.nonNullInjected();

    private final ExplosionScheduler instance;

    public ExplosionSchedulerProvider(WorldServer world) {
        instance = new ExplosionScheduler(world);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == EXPLOSION_SCHEDULER_CAPABILITY;
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == EXPLOSION_SCHEDULER_CAPABILITY ? EXPLOSION_SCHEDULER_CAPABILITY.cast(instance) : null;
    }
}
