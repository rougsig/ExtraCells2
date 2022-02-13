package extracells.item

import appeng.api.config.FuzzyMode
import extracells.api.storage.IFluidCell
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.item.Item
import net.minecraft.item.ItemStack

// TODO:
//  Add texture to that item
internal class FluidCellItem : Item(), IFluidCell {
  private data class Variant(
    val size: Int,
    val typeSize: Int,
    val maxTypes: Int,
    val idleDrain: Double,
    val suffix: String,
  ) {
    companion object {
      val VALUES = generateSequence(
        seed = Variant(
          size = 1024,
          typeSize = 8,
          maxTypes = 5,
          idleDrain = 0.5,
          suffix = "1k"
        )
      ) { variant ->
        variant.copy(
          size = variant.size * 4,
          typeSize = variant.typeSize * 4,
          maxTypes = variant.maxTypes,
          idleDrain = variant.idleDrain * 2,
          suffix = "${variant.size * 4 / 1024}k"
        )
      }.take(7).toList()
    }
  }

  init {
    maxStackSize = 1
    maxDamage = 0
    hasSubtypes = true
  }

  override fun getSubItems(item: Item, creativeTab: CreativeTabs?, listSubItems: MutableList<Any?>) {
    Variant.VALUES.forEachIndexed { index, variant ->
      listSubItems.add(ItemStack(item, 1, index))
    }
  }

  override fun getUnlocalizedName(itemStack: ItemStack): String {
    return "${EC2Item.FluidCell.itemName}.${Variant.VALUES[itemStack.itemDamage].suffix}"
  }

  override fun addInformation(
    itemStack: ItemStack?,
    player: EntityPlayer?,
    list: MutableList<Any?>?,
    var4: Boolean
  ) {
    // TODO:
    //  Add cell status to tooltip
  }

  override fun getSize(itemStack: ItemStack): Int {
    return Variant.VALUES[itemStack.itemDamage].size
  }

  override fun getTypeSize(itemStack: ItemStack): Int {
    return Variant.VALUES[itemStack.itemDamage].typeSize
  }

  override fun getMaxTypes(itemStack: ItemStack): Int {
    return Variant.VALUES[itemStack.itemDamage].maxTypes
  }

  override fun getIdleDrain(itemStack: ItemStack): Double {
    return Variant.VALUES[itemStack.itemDamage].idleDrain
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

