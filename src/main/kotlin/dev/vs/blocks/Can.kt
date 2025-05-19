package dev.vs.blocks

import com.mojang.serialization.MapCodec
import dev.vs.registry.BlockEntities
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import org.slf4j.LoggerFactory
import kotlin.jvm.optionals.getOrDefault

class CanBlock(settings: Settings) : BlockWithEntity(settings) {
    override fun getCodec(): MapCodec<out BlockWithEntity?>? =
        createCodec(::CanBlock)

    override fun createBlockEntity(pos: BlockPos?, state: BlockState?): BlockEntity? =
        CanBlockEntity(pos, state)

    override fun onUse(
        state: BlockState?,
        world: World?,
        pos: BlockPos?,
        player: PlayerEntity?,
        hit: BlockHitResult?
    ): ActionResult? {
        throwItem(world, pos)
        return ActionResult.SUCCESS
    }

    private fun throwItem(world: World?, pos: BlockPos?) {
        val entity = world?.getBlockEntity(pos) as? CanBlockEntity

        entity?.let {
            if (!it.isEmpty) {
                val stack = it.stack
                it.stack = ItemStack.EMPTY
                dropStack(world, pos?.up(), stack)
            }
        }
    }

    override fun onUseWithItem(
        stack: ItemStack?,
        state: BlockState?,
        world: World?,
        pos: BlockPos?,
        player: PlayerEntity?,
        hand: Hand?,
        hit: BlockHitResult?
    ): ActionResult? {
        val entity = world?.getBlockEntity(pos) as? CanBlockEntity
        val stack = player?.getStackInHand(hand) ?: return ActionResult.SUCCESS

        entity?.let {
            when {
                stack.isEmpty && !it.isEmpty -> throwItem(world, pos)
                !stack.isEmpty && !it.isEmpty -> player.sendMessage(Text.literal("can is full"), true)

                else -> {
                    val copy = stack.copy();
                    player.getStackInHand(hand)?.count = 0
                    entity.stack = copy
                }
            }
        }

        return ActionResult.SUCCESS
    }
}

class CanBlockEntity(pos: BlockPos?, state: BlockState?): BlockEntity(BlockEntities.CAN, pos, state) {
    private val logger = LoggerFactory.getLogger("can")

    var stack: ItemStack? = ItemStack.EMPTY
        set(value) {
            field = value
            markDirty()
        }

    val isEmpty
        get() = stack == ItemStack.EMPTY || stack == null

    override fun readNbt(nbt: NbtCompound?, registries: RegistryWrapper.WrapperLookup?) {
        super.readNbt(nbt, registries)

        stack = nbt?.get<ItemStack>("stack", ItemStack.CODEC)?.getOrDefault(ItemStack.EMPTY)
    }

    override fun writeNbt(nbt: NbtCompound?, registries: RegistryWrapper.WrapperLookup?) {
        super.writeNbt(nbt, registries)
        logger.info("saving ${stack}")
        if (!isEmpty) {
            nbt?.put("stack", stack?.toNbt(registries))
        }
    }
}