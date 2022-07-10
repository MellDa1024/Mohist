/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.common.loot;

import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.function.Function;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

/**
 * Implementation that defines what a global loot modifier must implement in order to be functional.
 * {@link LootModifier} Supplies base functionality; most modders should only need to extend that.<br/>
 * Requires a {@link Codec} to be registered: {@link ForgeRegistries#LOOT_MODIFIER_SERIALIZERS}, and returned in {@link #codec()}
 * Individual instances of modifiers must be registered via json, see forge:loot_modifiers/global_loot_modifiers
 */
public interface IGlobalLootModifier {
    Codec<IGlobalLootModifier> DIRECT_CODEC = ExtraCodecs.lazyInitializedCodec(() -> ForgeRegistries.LOOT_MODIFIER_SERIALIZERS.get().getCodec())
            .dispatch(IGlobalLootModifier::codec, Function.identity());

    Codec<LootItemCondition[]> LOOT_CONDITIONS_CODEC = Codec.PASSTHROUGH.flatXmap(
            d ->
            {
                try
                {
                    LootItemCondition[] conditions = LootModifierManager.GSON_INSTANCE.fromJson(getJson(d), LootItemCondition[].class);
                    return DataResult.success(conditions);
                }
                catch (JsonSyntaxException e)
                {
                    LootModifierManager.LOGGER.warn("Unable to decode loot conditions", e);
                    return DataResult.error(e.getMessage());
                }
            },
            conditions ->
            {
                try
                {
                    JsonElement element = LootModifierManager.GSON_INSTANCE.toJsonTree(conditions);
                    return DataResult.success(new Dynamic<>(JsonOps.INSTANCE, element));
                }
                catch (JsonSyntaxException e)
                {
                    LootModifierManager.LOGGER.warn("Unable to encode loot conditions", e);
                    return DataResult.error(e.getMessage());
                }
            }
    );

    @SuppressWarnings("unchecked")
    static <U> JsonElement getJson(Dynamic<?> dynamic) {
        Dynamic<U> typed = (Dynamic<U>) dynamic;
        return typed.getValue() instanceof JsonElement ?
                (JsonElement) typed.getValue() :
                typed.getOps().convertTo(JsonOps.INSTANCE, typed.getValue());
    }
    /**
     * Applies the modifier to the list of generated loot. This function needs to be responsible for
     * checking ILootConditions as well.
     * @param generatedLoot the list of ItemStacks that will be dropped, generated by loot tables
     * @param context the LootContext, identical to what is passed to loot tables
     * @return modified loot drops
     */
    @NotNull
    ObjectArrayList<ItemStack> apply(ObjectArrayList<ItemStack> generatedLoot, LootContext context);

    /**
     * Returns the registered codec for this modifier
     */
    Codec<? extends IGlobalLootModifier> codec();
}
