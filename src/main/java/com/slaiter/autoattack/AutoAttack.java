package com.slaiter.autoattack;

import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(AutoAttack.MODID)
public class AutoAttack {
    public static final String MODID = "autoattack";

    public AutoAttack() {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new ClientEvents());

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.SPEC, "auto-attack-client.toml");

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::registerKeyMappings);
    }

    private void registerKeyMappings(final RegisterKeyMappingsEvent event) {
        event.register(KeyBindings.AUTO_ATTACK_TOGGLE_KEY);
        event.register(KeyBindings.SHIELD_SWITCH_TOGGLE_KEY);
    }
}