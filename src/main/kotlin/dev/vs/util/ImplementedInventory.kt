package dev.vs.util

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventories
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.util.collection.DefaultedList

interface ImplementedInventory : Inventory {
    val items : DefaultedList<ItemStack>

    override fun size(): Int = items.size
    override fun isEmpty(): Boolean = items.any { !it.isEmpty }
    override fun getStack(slot: Int): ItemStack? = items[slot]
    override fun removeStack(slot: Int): ItemStack? = Inventories.removeStack(items, slot)
    override fun canPlayerUse(player: PlayerEntity?): Boolean = true

    override fun removeStack(slot: Int, amount: Int): ItemStack? {
        val res = Inventories.splitStack(items, slot, amount)
        if (!res.isEmpty) markDirty()
        return res
    }

    override fun setStack(slot: Int, stack: ItemStack?) {
        items[slot] = stack
        stack?.let {
            if (it.count > it.maxCount) {
                it.count = it.maxCount
            }
        }
    }

    override fun clear() {
        items.clear()
    }
}