package dev.vs

import dev.vs.blocks.BlockEntities
import dev.vs.blocks.Blocks
import dev.vs.items.Items
import net.fabricmc.api.ModInitializer
import org.slf4j.LoggerFactory

object Fishies : ModInitializer {
    private val logger = LoggerFactory.getLogger("fishies")
	const val ID = "fishies"

	override fun onInitialize() {
		Items
		Blocks
		BlockEntities
	}
}