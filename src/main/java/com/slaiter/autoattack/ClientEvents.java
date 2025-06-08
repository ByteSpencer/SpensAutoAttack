package com.slaiter.autoattack;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Optional;

@Mod.EventBusSubscriber(modid = AutoAttack.MODID)
public class ClientEvents {

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Minecraft mc = Minecraft.getInstance();
            Player player = mc.player;
            if (player == null) return;

            handleKeyToggles(player);

            if (Config.AUTO_ATTACK_ENABLED.get()) {
                handleAutoAttack(player, mc);
            }
            if (Config.SHIELD_SWITCH_ENABLED.get()) {
                handleAutoShield(player, mc);
            }
        }
    }

    private static void handleKeyToggles(Player player) {
        if (KeyBindings.AUTO_ATTACK_TOGGLE_KEY.consumeClick()) {
            boolean currentState = Config.AUTO_ATTACK_ENABLED.get();
            Config.AUTO_ATTACK_ENABLED.set(!currentState);
            player.sendSystemMessage(Component.literal("Auto Attack: " + (!currentState ? "ON" : "OFF")).withStyle(!currentState ? ChatFormatting.GREEN : ChatFormatting.RED));
        }

        if (KeyBindings.SHIELD_SWITCH_TOGGLE_KEY.consumeClick()) {
            boolean currentState = Config.SHIELD_SWITCH_ENABLED.get();
            Config.SHIELD_SWITCH_ENABLED.set(!currentState);
            player.sendSystemMessage(Component.literal("Offhand Shield Switch: " + (!currentState ? "ON" : "OFF")).withStyle(!currentState ? ChatFormatting.GREEN : ChatFormatting.RED));
        }
    }

    private static void handleAutoAttack(Player player, Minecraft mc) {
        HitResult hitResult = mc.hitResult;
        if (hitResult == null || hitResult.getType() != HitResult.Type.ENTITY) return;

        Entity target = ((EntityHitResult) hitResult).getEntity();
        if (!(target instanceof Monster)) return;

        boolean canAttackThisMob = false;
        ResourceLocation targetId = EntityType.getKey(target.getType());

        if (Config.ATTACK_MODE.get() == Config.AttackMode.ALL) {
            canAttackThisMob = true;
        } else {
            if (Config.CUSTOM_ATTACK_MOBS.containsKey(targetId.toString()) &&
                    Config.CUSTOM_ATTACK_MOBS.get(targetId.toString()).get()) {
                canAttackThisMob = true;
            }
        }

        if (canAttackThisMob) {
            boolean shouldAttack = false;
            if (target instanceof EnderMan enderman) {
                if (enderman.getRemainingPersistentAngerTime() > 0) shouldAttack = true;
            } else if (target instanceof ZombifiedPiglin zombifiedPiglin) {
                if (zombifiedPiglin.isAngryAt(player)) shouldAttack = true;
            } else if (target instanceof Piglin piglin) {
                Optional<LivingEntity> memory = piglin.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET);
                if (memory.isPresent() && memory.get().equals(player)) shouldAttack = true;
            } else {
                shouldAttack = true;
            }

            if (shouldAttack && player.distanceToSqr(target) <= 9.0D) {
                if (mc.gameMode != null) mc.gameMode.attack(player, target);
                player.swing(InteractionHand.MAIN_HAND);
            }
        }
    }

    private static void handleAutoShield(Player player, Minecraft mc) {
        if (!player.getOffhandItem().isEmpty()) return;

        boolean threatDetected = false;

        boolean checkAll = Config.SHIELD_MODE.get() == Config.ShieldMode.ALL;

        if (checkAll || (Config.CUSTOM_SHIELD_MOBS.get("minecraft:skeleton").get())) {
            if (!player.level().getEntitiesOfClass(Skeleton.class, player.getBoundingBox().inflate(16.0D)).isEmpty()) threatDetected = true;
        }
        if (!threatDetected && (checkAll || (Config.CUSTOM_SHIELD_MOBS.get("minecraft:pillager").get()))) {
            if (!player.level().getEntitiesOfClass(Pillager.class, player.getBoundingBox().inflate(8.0D)).isEmpty()) threatDetected = true;
        }
        if (!threatDetected && (checkAll || (Config.CUSTOM_SHIELD_MOBS.get("minecraft:ghast").get()))) {
            if (!player.level().getEntitiesOfClass(Ghast.class, player.getBoundingBox().inflate(64.0D)).isEmpty()) threatDetected = true;
        }
        if (!threatDetected && (checkAll || (Config.CUSTOM_SHIELD_MOBS.get("minecraft:blaze").get()))) {
            if (!player.level().getEntitiesOfClass(Blaze.class, player.getBoundingBox().inflate(48.0D)).isEmpty()) threatDetected = true;
        }
        if (!threatDetected && (checkAll || (Config.CUSTOM_SHIELD_MOBS.get("minecraft:shulker").get()))) {
            if (!player.level().getEntitiesOfClass(Shulker.class, player.getBoundingBox().inflate(16.0D)).isEmpty()) threatDetected = true;
        }

        if (threatDetected) {
            for (int i = 0; i < 9; ++i) {
                ItemStack hotbarStack = player.getInventory().getItem(i);
                if (hotbarStack.is(Items.SHIELD)) {
                    int hotbarSlotId = 36 + i;
                    if (mc.gameMode != null) mc.gameMode.handleInventoryMouseClick(player.inventoryMenu.containerId, hotbarSlotId, 0, ClickType.QUICK_MOVE, player);
                    break;
                }
            }
        }
    }
}