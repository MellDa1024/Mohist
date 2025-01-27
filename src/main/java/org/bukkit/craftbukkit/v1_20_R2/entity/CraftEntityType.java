package org.bukkit.craftbukkit.v1_20_R2.entity;

import com.google.common.base.Preconditions;
import com.mohistmc.api.EntityAPI;
import net.minecraft.core.registries.Registries;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.v1_20_R2.CraftRegistry;
import org.bukkit.craftbukkit.v1_20_R2.util.CraftNamespacedKey;
import org.bukkit.entity.EntityType;

public class CraftEntityType {

    public static EntityType minecraftToBukkit(net.minecraft.world.entity.EntityType<?> minecraft) {
        Preconditions.checkArgument(minecraft != null);

        net.minecraft.core.Registry<net.minecraft.world.entity.EntityType<?>> registry = CraftRegistry.getMinecraftRegistry(Registries.ENTITY_TYPE);
        EntityType bukkit = Registry.ENTITY_TYPE.get(CraftNamespacedKey.fromMinecraft(registry.getResourceKey(minecraft).orElseThrow().location()));
        if (bukkit == null) {
            bukkit = EntityAPI.entityType(bukkit, EntityType.UNKNOWN);
        }
        Preconditions.checkArgument(bukkit != null);

        return bukkit;
    }

    public static net.minecraft.world.entity.EntityType<?> bukkitToMinecraft(EntityType bukkit) {
        Preconditions.checkArgument(bukkit != null);

        return CraftRegistry.getMinecraftRegistry(Registries.ENTITY_TYPE)
                .getOptional(CraftNamespacedKey.toMinecraft(bukkit.getKey())).orElseThrow();
    }
}
