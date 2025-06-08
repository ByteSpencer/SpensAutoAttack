package com.slaiter.autoattack;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {
    public static final String KEY_CATEGORY_AUTOATTACK = "key.category.autoattack";
    public static final String KEY_TOGGLE_AUTO_ATTACK = "key.autoattack.toggle_auto_attack";
    public static final String KEY_TOGGLE_SHIELD_SWITCH = "key.autoattack.toggle_shield_switch";

    public static final KeyMapping AUTO_ATTACK_TOGGLE_KEY = new KeyMapping(
            KEY_TOGGLE_AUTO_ATTACK,
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            KEY_CATEGORY_AUTOATTACK
    );

    public static final KeyMapping SHIELD_SWITCH_TOGGLE_KEY = new KeyMapping(
            KEY_TOGGLE_SHIELD_SWITCH,
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            KEY_CATEGORY_AUTOATTACK
    );
}