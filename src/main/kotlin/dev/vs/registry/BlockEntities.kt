package dev.vs.registry

import dev.vs.Fishies
import dev.vs.blocks.CanBlockEntity
import dev.vs.blocks.CounterBlockEntity
import dev.vs.blocks.PortainerBlockEntity
import dev.vs.blocks.RedstoneEmitterBlockEntity
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

    val COUNTER = register("counter", ::CounterBlockEntity, Blocks.COUNTER)
    val REDSTONE_EMITTER = register("redstone_emitter", ::RedstoneEmitterBlockEntity, Blocks.REDSTONE_EMITTER)
    var PORTAINER = register("portainer", ::PortainerBlockEntity, Blocks.PORTAINER)
    val CAN = register("can", ::CanBlockEntity, Blocks.CAN)
}