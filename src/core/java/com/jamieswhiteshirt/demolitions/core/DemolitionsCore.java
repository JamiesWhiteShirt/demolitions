package com.jamieswhiteshirt.demolitions.core;

import com.google.common.eventbus.EventBus;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;

import java.util.Collections;

public class DemolitionsCore extends DummyModContainer {
    public static final String MODID = "demolitions_core";
    public static final String VERSION = "1.12-0.0.0.0";

    public DemolitionsCore() {
        super(new ModMetadata());
        ModMetadata metadata = getMetadata();
        metadata.modId = MODID;
        metadata.name = "Demolitions Core";
        metadata.version = VERSION;
        metadata.authorList = Collections.singletonList("JamiesWhiteShirt");
        metadata.description =
                "This is one of those evil core mods, known for burning down your cat and killing your house!\n" +
                "You'll find that Demolitions Core actually only adds some necessities to make Demolitions work.\n" +
                "This actually doesn't do anything yet, but it probably will in the near future!";
        metadata.screenshots = new String[0];
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
        return true;
    }
}
