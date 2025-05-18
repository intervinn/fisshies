package dev.vs.items

import dev.vs.Fishies
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier
import java.util.function.Function

object Items {
    fun keyOf(name: String): RegistryKey<Item> =
        RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Fishies.ID, name))

    fun register(name: String, factory: (Item.Settings) -> Item, settings: Item.Settings): Item {
        val key = keyOf(name)
        val item = factory(settings)
        return Registry.register(Registries.ITEM, key, item)
    }


}