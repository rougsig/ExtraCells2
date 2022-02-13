package extracells.api.storage

import appeng.api.storage.ICellWorkbenchItem
import net.minecraft.item.ItemStack

internal interface IFluidCell : ICellWorkbenchItem {
  fun getSize(itemStack: ItemStack): Int
  fun getTypeSize(itemStack: ItemStack): Int
  fun getMaxTypes(itemStack: ItemStack): Int
  fun getIdleDrain(itemStack: ItemStack): Double
}
