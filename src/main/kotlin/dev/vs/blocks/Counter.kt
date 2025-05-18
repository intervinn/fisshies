package dev.vs.blocks

import com.mojang.serialization.MapCodec
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class CounterBlock(settings: Settings): BlockWithEntity(settings) {
    init {

    }

    override fun getCodec(): MapCodec<out BlockWithEntity> =
        createCodec(::CounterBlock)

    override fun createBlockEntity(pos: BlockPos?, state: BlockState?): BlockEntity? =
        CounterBlockEntity(pos, state)

    override fun onUse(
        state: BlockState?,
        world: World?,
        pos: BlockPos?,
        player: PlayerEntity?,
        hit: BlockHitResult?
    ): ActionResult {
        val entity = world?.getBlockEntity(pos) as? CounterBlockEntity

        entity?.increment()

        player?.sendMessage(Text.literal("the count is ${entity?.count}"), true)
        return ActionResult.SUCCESS
    }
}

class CounterBlockEntity(pos: BlockPos?, state: BlockState?): BlockEntity(BlockEntities.COUNTER, pos, state) {
    var count = 0

    fun increment() {
        count++
    }
}