package dev.vs.blocks

import dev.vs.Fishies
import dev.vs.items.Items
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroups
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier
import java.util.function.Function

object Blocks {
    fun keyOf(name: String): RegistryKey<Block> =
        RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Fishies.ID, name))

    fun register(name: String, factory: (AbstractBlock.Settings) -> Block, settings: AbstractBlock.Settings, addItem: Boolean): Block {
        val keyBlock = keyOf(name)
        val block = factory(settings.registryKey(keyBlock))

        if (addItem) {
            val keyItem = Items.keyOf(name)
            val item = BlockItem(block, Item.Settings().registryKey(keyItem))
            Registry.register(Registries.ITEM, keyItem, item);
        }
        return Registry.register(Registries.BLOCK, keyBlock, block)
    }

    val COUNTER = register(
        "counter",
        ::CounterBlock,
        AbstractBlock.Settings.create(),
        true)

    val REDSTONE_EMITTER = register(
        "redstone_emitter",
        ::RedstoneEmitterBlock,
        AbstractBlock.Settings.create(),
        true
    )

    init {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register {
            it.add(COUNTER)
            it.add(REDSTONE_EMITTER)
        }
    }
}