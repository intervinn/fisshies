package dev.vs.blocks

import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.entity.*
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.event.GameEvent
import net.minecraft.world.explosion.Explosion

class LightningTntBlock(settings: AbstractBlock.Settings): Block(settings) {
    override fun onUseWithItem(
        stack: ItemStack?,
        state: BlockState?,
        world: World?,
        pos: BlockPos?,
        player: PlayerEntity?,
        hand: Hand?,
        hit: BlockHitResult?
    ): ActionResult? {
        return stack?.let {
            return if (it.isOf(Items.FLINT_AND_STEEL)) {
                if (ignite(world, pos!!, player)) {
                    stack.damage(1, player, LivingEntity.getSlotForHand(hand))
                    world?.setBlockState(pos, Blocks.AIR.defaultState, Block.NOTIFY_ALL_AND_REDRAW)
                }

                ActionResult.SUCCESS
            } else {
                super.onUseWithItem(stack, state, world, pos, player, hand, hit)
            }
        }
    }

    private fun ignite(world: World?, pos: BlockPos, igniter: LivingEntity?): Boolean =
        when {
            world is ServerWorld -> {
                val tnt = LightningTntEntity(world, pos.x + 0.5, pos.y.toDouble(), pos.z + 0.5, igniter)
                world.spawnEntity(tnt)
                world.emitGameEvent(igniter, GameEvent.PRIME_FUSE, pos)
                true
            }
            else -> false
        }


}

class LightningTntEntity : TntEntity {
    constructor(type: EntityType<out TntEntity>, world: World?) : super(type, world)
    constructor(world: World, x: Double, y: Double, z: Double, igniter: LivingEntity?) : super(world, x, y, z, igniter)

    override fun tick() {
        tickPortalTeleportation()
        applyGravity()
        move(MovementType.SELF, velocity)
        tickBlockCollision()
        velocity = velocity.multiply(0.98)

        if (isOnGround) {
            velocity = velocity.multiply(0.7, -0.5, 0.7)
        }

        val i = fuse - 1
        fuse = i
        when {
            i <= 0 -> {
                discard()
                if (!world.isClient) {
                    explode()
                }
            }

            i == 10 -> {
                lighting()
            }

            else -> {
                updateWaterState()
                if (world.isClient) {
                    world.addParticleClient(ParticleTypes.SMOKE, x, y + 0.5, z, 0.0, 0.0, 0.0)
                }
            }
        }
    }

    private fun lighting() {
        val bolt = LightningEntity(EntityType.LIGHTNING_BOLT, world)
        bolt.setPosition(pos)
        world.spawnEntity(bolt)
    }

    private fun explode() {
        if (world !is ServerWorld) {
            return
        }

        world.createExplosion(
            this,
            Explosion.createDamageSource(world, this),
            null,
            x,
            getBodyY(0.0625),
            z,
            12.0f,
            false,
            World.ExplosionSourceType.TNT
        )
    }
}