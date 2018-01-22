package com.jamieswhiteshirt.demolitions.common.capability;

import com.jamieswhiteshirt.demolitions.api.IShakeManager;
import com.jamieswhiteshirt.demolitions.common.Util;
import com.jamieswhiteshirt.demolitions.common.impl.ShakeManager;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;

public class ShakeManagerProvider implements ICapabilityProvider {
    @CapabilityInject(IShakeManager.class)
    private static final Capability<IShakeManager> SHAKE_MANAGER_CAPABILITY = Util.nonNullInjected();

    private final ShakeManager instance = new ShakeManager();

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == SHAKE_MANAGER_CAPABILITY;
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == SHAKE_MANAGER_CAPABILITY ? SHAKE_MANAGER_CAPABILITY.cast(instance) : null;
    }
}
