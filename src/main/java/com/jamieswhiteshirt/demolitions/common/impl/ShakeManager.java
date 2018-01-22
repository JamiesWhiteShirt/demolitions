package com.jamieswhiteshirt.demolitions.common.impl;

import com.jamieswhiteshirt.demolitions.api.IShakeManager;
import com.jamieswhiteshirt.demolitions.api.ShakeSource;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ShakeManager implements IShakeManager {
    private final List<ShakeSource> shakeSources = new LinkedList<>();

    @Override
    public void add(ShakeSource source) {
        shakeSources.add(source);
    }

    @Override
    public Collection<ShakeSource> getAll() {
        return shakeSources;
    }

    @Override
    public void tick() {
        shakeSources.removeIf(ShakeSource::tick);
    }
}
