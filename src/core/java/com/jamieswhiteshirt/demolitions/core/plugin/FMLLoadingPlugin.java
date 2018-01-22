package com.jamieswhiteshirt.demolitions.core.plugin;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import javax.annotation.Nullable;
import java.util.Map;

@IFMLLoadingPlugin.Name("demolitions_core")
@IFMLLoadingPlugin.SortingIndex(1001)
@IFMLLoadingPlugin.TransformerExclusions("com.jamieswhiteshirt.demolitions.core.plugin")
public class FMLLoadingPlugin implements IFMLLoadingPlugin {
    @Override
    public String[] getASMTransformerClass() {
        return new String[] { "com.jamieswhiteshirt.demolitions.core.plugin.ClassTransformer" };
    }

    @Override
    public String getModContainerClass() {
        return "com.jamieswhiteshirt.demolitions.core.DemolitionsCore";
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    @Nullable
    public String getAccessTransformerClass() {
        return null;
    }
}
