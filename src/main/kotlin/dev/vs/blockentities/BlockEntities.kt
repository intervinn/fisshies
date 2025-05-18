package dev.vs.blockentities

import dev.vs.Fishies
import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.minecraft.block.Block
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object BlockEntities {
    fun <T> register(name: String, factory: FabricBlockEntityTypeBuilder.Factory<out T>, vararg blocks: Block): BlockEntityType<T> where T : BlockEntity =
        Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Identifier.of(Fishies.ID, name),
            FabricBlockEntityTypeBuilder.create(factory, *blocks).build()
        )
}