package com.jamieswhiteshirt.demolitions.api;

import net.minecraft.util.math.Vec3d;

public class ShakeSource {
    private final Vec3d pos;
    private double intensity;

    public ShakeSource(Vec3d pos, double intensity) {
        this.pos = pos;
        this.intensity = intensity;
    }

    public Vec3d getPos() {
        return pos;
    }

    public double getIntensity() {
        return intensity;
    }

    public boolean tick() {
        intensity *= 0.90;
        return intensity < 0.01D;
    }
}
