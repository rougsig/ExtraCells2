package extracells.item.storage

import appeng.api.config.FuzzyMode
import extracells.api.storage.IFluidStorageCell
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.inventory.IInventory
import net.minecraft.item.Item
import net.minecraft.item.ItemStack

class FluidStorageCell : Item(), IFluidStorageCell {

  init {
    maxDamage = 0
    maxStackSize = 0
    hasSubtypes = true
  }

  override fun getSubItems(item: Item, creativeTab: CreativeTabs?, listSubItems: MutableList<Any?>) {
    FluidStorageCellVariant.values().forEach { variant ->
      listSubItems.add(variant.itemStack)
    }
  }

  override fun getUnlocalizedName(itemStack: ItemStack): String {
    return "extracells.item.storage.fluid.${FluidStorageCellVariant.values()[itemStack.itemDamage].name.lowercase()}"
  }

  // TODO:
  //  Not implemented part for fuzzy
  override fun getFuzzyMode(p0: ItemStack?): FuzzyMode {
    return FuzzyMode.IGNORE_ALL
  }

  override fun setFuzzyMode(p0: ItemStack?, p1: FuzzyMode?) {
    // no-op
  }

  // TODO:
  //  Not implemented part for cell workbench
  override fun isEditable(p0: ItemStack?): Boolean {
    return false
  }

  override fun getUpgradesInventory(p0: ItemStack?): IInventory {
    TODO("Not yet implemented")
  }

  override fun getConfigInventory(p0: ItemStack?): IInventory {
    TODO("Not yet implemented")
  }
}
