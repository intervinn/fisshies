package dev.vs.blocks


import com.mojang.serialization.MapCodec
import dev.vs.registry.BlockEntities
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity
import net.minecraft.block.entity.BlockEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.block.WireOrientation
import org.slf4j.LoggerFactory

class RedstoneEmitterBlock(settings: Settings) : BlockWithEntity(settings) {
    private val logger = LoggerFactory.getLogger("emitter")

    override fun getCodec(): MapCodec<out BlockWithEntity> =
        createCodec(::RedstoneEmitterBlock)

    override fun createBlockEntity(pos: BlockPos?, state: BlockState?): BlockEntity? =
        RedstoneEmitterBlockEntity(pos, state)

    override fun getWeakRedstonePower(
        state: BlockState?,
        world: BlockView?,
        pos: BlockPos?,
        direction: Direction?
    ): Int {
        return when {
            direction != Direction.DOWN -> 0
            else -> when (val power = powerBelow(world, pos, direction)) {
                0 -> 0
                else -> power-1
            }
        }
    }

    private fun powerBelow(world: BlockView?, pos: BlockPos?, direction: Direction?): Int {
        val below = world?.getBlockState(pos?.down())
        return when {
            below?.emitsRedstonePower() == false -> 0
            else -> below?.getWeakRedstonePower(world, pos?.down(), direction) ?: 0
        }
    }

    override fun emitsRedstonePower(state: BlockState?): Boolean = true

    override fun neighborUpdate(
        state: BlockState?,
        world: World?,
        pos: BlockPos?,
        sourceBlock: Block?,
        wireOrientation: WireOrientation?,
        notify: Boolean
    ) {
        world?.updateNeighbor(pos?.up(), this, wireOrientation)
        super.neighborUpdate(state, world, pos, sourceBlock, wireOrientation, notify)
    }
}

class RedstoneEmitterBlockEntity(pos: BlockPos?, state: BlockState?) : BlockEntity(BlockEntities.REDSTONE_EMITTER, pos, state) {
    private var active = true

    fun toggle() {
        active = !active
        markDirty()
    }
}
