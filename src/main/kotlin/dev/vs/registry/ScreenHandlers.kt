package dev.vs.registry

import dev.vs.Fishies
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.resource.featuretoggle.FeatureSet
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.util.Identifier

object ScreenHandlers {
    fun<T> register(name: String, factory: ScreenHandlerType.Factory<T>, features: FeatureSet): ScreenHandlerType<T> where T : ScreenHandler =
        Registry.register(
            Registries.SCREEN_HANDLER,
            Identifier.of(Fishies.ID, name),
            ScreenHandlerType(factory, features)
        )

    init {

    }
}