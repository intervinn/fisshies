package dev.vs.blocks

import com.mojang.serialization.MapCodec
import dev.vs.registry.BlockEntities
import dev.vs.util.ImplementedInventory
import net.minecraft.block.BarrelBlock
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.LootableContainerBlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventories
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.screen.Generic3x3ContainerScreenHandler
import net.minecraft.screen.GenericContainerScreenHandler
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.ScreenHandler
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class PortainerBlock(settings: Settings) : BlockWithEntity(settings) {
    private val logger = LoggerFactory.getLogger("portainer")

    override fun getCodec(): MapCodec<out BlockWithEntity?>? =
        createCodec(::PortainerBlock)

    override fun createBlockEntity(pos: BlockPos?, state: BlockState?): BlockEntity? =
        PortainerBlockEntity(pos, state)

    override fun getRenderType(state: BlockState?): BlockRenderType? =
        BlockRenderType.MODEL

    override fun onUse(
        state: BlockState?,
        world: World?,
        pos: BlockPos?,
        player: PlayerEntity?,
        hit: BlockHitResult?
    ): ActionResult? {
        if (world?.isClient() == false) {
            val entity = world.getBlockEntity(pos) as? PortainerBlockEntity
            player?.openHandledScreen(entity)
        }

        return ActionResult.SUCCESS
    }

    override fun hasComparatorOutput(state: BlockState?): Boolean = true
    override fun getComparatorOutput(state: BlockState?, world: World?, pos: BlockPos?): Int {
        return ScreenHandler.calculateComparatorOutput(world?.getBlockEntity(pos))
    }
}

class PortainerBlockEntity(pos: BlockPos?, state: BlockState?)
    : LootableContainerBlockEntity(BlockEntities.PORTAINER, pos, state) {

    private var inventory = DefaultedList.ofSize(27, ItemStack.EMPTY)

    override fun size(): Int {
        return 27
    }

    override fun createScreenHandler(syncId: Int, playerInventory: PlayerInventory?): ScreenHandler? {
        LoggerFactory.getLogger("portainerr").info("DOES IT OPEN")
        return GenericContainerScreenHandler.createGeneric9x3(syncId, playerInventory, this)
    }

    override fun getContainerName(): Text? = Text.literal("portainer")

    override fun getHeldStacks(): DefaultedList<ItemStack?>? = inventory

    override fun setHeldStacks(inventory: DefaultedList<ItemStack?>?) {
        this.inventory = inventory
    }

    override fun readNbt(nbt: NbtCompound?, registries: RegistryWrapper.WrapperLookup?) {
        super.readNbt(nbt, registries)
        if (!readLootTable(nbt)) {
            Inventories.readNbt(nbt, inventory, registries)
        }
    }

    override fun writeNbt(nbt: NbtCompound?, registries: RegistryWrapper.WrapperLookup?) {
        super.writeNbt(nbt, registries)
        if (!writeLootTable(nbt)) {
            Inventories.writeNbt(nbt, inventory, registries)
        }
    }
}