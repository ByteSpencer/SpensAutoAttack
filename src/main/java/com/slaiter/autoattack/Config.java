package com.slaiter.autoattack;

import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.ForgeConfigSpec;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Config {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    // --- GENERAL TOGGLES ---
    public static final ForgeConfigSpec.BooleanValue AUTO_ATTACK_ENABLED;
    public static final ForgeConfigSpec.BooleanValue SHIELD_SWITCH_ENABLED;

    // --- AUTO ATTACK CONFIG ---
    public enum AttackMode { ALL, CUSTOM }
    public static final ForgeConfigSpec.EnumValue<AttackMode> ATTACK_MODE;
    private static final List<String> ATTACK_TARGETS = List.of(
            "minecraft:zombie", "minecraft:husk", "minecraft:drowned", "minecraft:zombie_villager",
            "minecraft:skeleton", "minecraft:stray", "minecraft:wither_skeleton",
            "minecraft:creeper", "minecraft:spider", "minecraft:cave_spider", "minecraft:phantom",
            "minecraft:slime", "minecraft:magma_cube", "minecraft:enderman", "minecraft:silverfish",
            "minecraft:endermite", "minecraft:vex", "minecraft:vindicator", "minecraft:evoker",
            "minecraft:ravager", "minecraft:hoglin", "minecraft:zoglin",
            "minecraft:piglin_brute", "minecraft:guardian", "minecraft:elder_guardian"
            // Note: Neutral mobs like Piglin and Zombified Piglin are handled by the auto attack logic directlyyy!
    );
    public static final Map<String, ForgeConfigSpec.BooleanValue> CUSTOM_ATTACK_MOBS;

    // --- SHIELD SWITCH CONFIG ---
    public enum ShieldMode { ALL, CUSTOM }
    public static final ForgeConfigSpec.EnumValue<ShieldMode> SHIELD_MODE;
    private static final List<String> SHIELD_TRIGGERS = List.of(
            "minecraft:skeleton", "minecraft:stray", "minecraft:pillager",
            "minecraft:ghast", "minecraft:blaze", "minecraft:shulker"
    );
    public static final Map<String, ForgeConfigSpec.BooleanValue> CUSTOM_SHIELD_MOBS;

    static {
        BUILDER.push("Auto Attack Mod - General Toggles");

        AUTO_ATTACK_ENABLED = BUILDER.comment("The main ON/OFF switch for the Auto Attack feature.").define("auto_attack_enabled", true);
        SHIELD_SWITCH_ENABLED = BUILDER.comment("The main ON/OFF switch for the Offhand Shield Switch feature.").define("shield_switch_enabled", true);

        BUILDER.pop();

        // --- AUTO ATTACK CONFIG SECTION ---
        BUILDER.push("Auto Attack - Entity Selection");

        ATTACK_MODE = BUILDER
                .comment("'ALL' will attack all supported hostile mobs. 'CUSTOM' lets you choose from the list below.")
                .defineEnum("attack_mode", AttackMode.ALL);

        BUILDER.push("Custom Attack Mob List");
        CUSTOM_ATTACK_MOBS = ATTACK_TARGETS.stream()
                .collect(Collectors.toMap(Function.identity(), id -> BUILDER.define(id.replace("minecraft:", ""), true)));
        BUILDER.pop();

        BUILDER.pop();

        // --- SHIELD SWITCH CONFIG SECTION ---
        BUILDER.push("Offhand Shield Switch - Entity Selection");

        SHIELD_MODE = BUILDER
                .comment("'ALL' will equip the shield for all supported projectile mobs. 'CUSTOM' lets you choose from the list below.")
                .defineEnum("shield_mode", ShieldMode.ALL);

        BUILDER.push("Custom Shield Mob List");
        CUSTOM_SHIELD_MOBS = SHIELD_TRIGGERS.stream()
                .collect(Collectors.toMap(Function.identity(), id -> BUILDER.define(id.replace("minecraft:", ""), true)));
        BUILDER.pop();

        BUILDER.pop();

        SPEC = BUILDER.build();
    }
}