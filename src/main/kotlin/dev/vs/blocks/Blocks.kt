package dev.vs.blocks

import dev.vs.Fishies
import dev.vs.items.Items
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier
import java.util.function.Function

object Blocks {
    fun keyOf(name: String): RegistryKey<Block> =
        RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Fishies.ID, name))

    fun register(name: String, factory: Function<AbstractBlock.Settings, Block>, settings: AbstractBlock.Settings, addItem: Boolean): Block {
        val keyBlock = keyOf(name)
        val block = factory.apply(settings)

        if (addItem) {
            val keyItem = Items.keyOf(name)
            val item = BlockItem(block, Item.Settings().registryKey(keyItem))
            Registry.register(Registries.ITEM, keyItem, item);
        }
        return Registry.register(Registries.BLOCK, keyBlock, block)
    }

}