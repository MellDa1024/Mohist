/*
 * MohistMC
 * Copyright (C) 2019-2022.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.mohistmc.entity;

import com.mohistmc.api.ServerAPI;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftRaider;
import org.bukkit.entity.EntityCategory;
import org.bukkit.entity.EntityType;

public class CraftCustomRaider extends CraftRaider {

    public String entityName;

    public CraftCustomRaider(CraftServer server, AbstractRaiderEntity entity) {
        super(server, entity);
        this.entityName = ServerAPI.entityTypeMap.get(entity.getType());
        if (entityName == null) {
            entityName = entity.getName().getString();
        }
    }

    @Override
    public EntityType getType() {
        EntityType type = EntityType.fromName(this.entityName);
        if (type != null) {
            return type;
        } else {
            return EntityType.FORGE_MOD_PROJECTILE;
        }
    }

    @Override
    public String toString() {
        return "CraftCustomRaider{" + entityName + '}';
    }

    @Override
    public EntityCategory getCategory() {
        return EntityCategory.ILLAGER;
    }
}
