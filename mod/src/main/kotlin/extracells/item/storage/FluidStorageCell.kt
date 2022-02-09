package extracells.item.storage

import appeng.api.config.FuzzyMode
import extracells.api.storage.IFluidStorageCell
import net.minecraft.inventory.IInventory
import net.minecraft.item.Item
import net.minecraft.item.ItemStack

class FluidStorageCell(variant: Variant) : Item(), IFluidStorageCell {
  enum class Variant {
    FluidCell1k,
    FluidCell4k,
    FluidCell16k,
    FluidCell64k,
    FluidCell256k,
    FluidCell1024k,
    FluidCell4096k,
  }

  override val idleDrain: Double
  override val bytesPerType: Int
  override val totalBytes: Int
  override val totalTypes = 5

  init {
    when (variant) {
      Variant.FluidCell1k -> {
        this.idleDrain = 0.5
        this.bytesPerType = 8
        this.totalBytes = 1024
      }
      Variant.FluidCell4k -> {
        this.idleDrain = 1.0
        this.bytesPerType = 32
        this.totalBytes = 1024 * 4
      }
      Variant.FluidCell16k -> {
        this.idleDrain = 1.5
        this.bytesPerType = 128
        this.totalBytes = 1024 * 16
      }
      Variant.FluidCell64k -> {
        this.idleDrain = 2.0
        this.bytesPerType = 512
        this.totalBytes = 1024 * 64
      }
      Variant.FluidCell256k -> {
        this.idleDrain = 2.5
        this.bytesPerType = 1024
        this.totalBytes = 1024 * 256
      }
      Variant.FluidCell1024k -> {
        this.idleDrain = 3.0
        this.bytesPerType = 4096
        this.totalBytes = 1024 * 1024
      }
      Variant.FluidCell4096k -> {
        this.idleDrain = 3.5
        this.bytesPerType = 16392
        this.totalBytes = 1024 * 4096
      }
    }
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
