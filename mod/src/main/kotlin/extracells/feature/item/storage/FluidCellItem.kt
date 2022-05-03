package extracells.feature.item.storage

import appeng.api.config.FuzzyMode
import extracells.api.storage.IFluidCell
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.inventory.IInventory
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.IIcon

internal class FluidCellItem : Item(), IFluidCell {

  init {
    this.maxStackSize = 1
    this.hasSubtypes = true
    this.maxDamage = 0
  }

  override fun getIconFromDamage(meta: Int): IIcon {
    return FluidCellType.findPartByMeta(meta).icon
  }

  override fun getSubItems(item: Item, creativeTab: CreativeTabs?, itemList: MutableList<Any?>) {
    FluidCellType.values()
      .forEach { cell -> itemList.add(ItemStack(item, 1, cell.meta)) }
  }

  override fun getUnlocalizedName(itemStack: ItemStack): String {
    return FluidCellType.findPartByMeta(itemStack.itemDamage).unlocalizedName
  }

  override fun getSize(itemStack: ItemStack): Int {
    return FluidCellType.findPartByMeta(itemStack.itemDamage).part.size
  }

  override fun getTypeSize(itemStack: ItemStack): Int {
    return FluidCellType.findPartByMeta(itemStack.itemDamage).typeSize
  }

  override fun getMaxTypes(itemStack: ItemStack): Int {
    return FluidCellType.findPartByMeta(itemStack.itemDamage).maxTypes
  }

  override fun getIdleDrain(itemStack: ItemStack): Double {
    return FluidCellType.findPartByMeta(itemStack.itemDamage).idleDrain
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