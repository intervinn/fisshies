package dev.vs.registry

import dev.vs.Fishies
import dev.vs.blocks.LightningTntEntity
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier

object Entities {
    fun keyOf(name: String): RegistryKey<EntityType<*>> =
        RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(Fishies.ID, name))

    fun <T> register(name: String, builder: EntityType.Builder<T>): EntityType<T> where T : Entity =
        Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(Fishies.ID, name),
            builder.build(keyOf(name))
        )

    val LIGHTING_TNT = register<LightningTntEntity>(
        "lightning_tnt",
        EntityType.Builder.create(::LightningTntEntity, SpawnGroup.MISC)
    )

}