package com.jamieswhiteshirt.demolitions.api;

import java.util.Collection;

public interface IShakeManager {
    void add(ShakeSource source);

    Collection<ShakeSource> getAll();

    void tick();
}
