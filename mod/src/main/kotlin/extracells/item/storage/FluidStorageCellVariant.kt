package extracells.item.storage

import extracells.item.EC2Item
import net.minecraft.item.ItemStack

enum class FluidStorageCellVariant(
  val maxTypes: Int,
  val size: Int,
  val idleDrain: Double,
  val bytesPerType: Int,
) {
  FluidCell1k(
    maxTypes = 5,
    idleDrain = 0.5,
    bytesPerType = 8,
    size = 1024,
  ),
  FluidCell4k(
    maxTypes = 5,
    idleDrain = 0.5,
    bytesPerType = 8,
    size = 1024,
  ),
  FluidCell16k(
    maxTypes = 5,
    idleDrain = 0.5,
    bytesPerType = 8,
    size = 1024,
  ),
  FluidCell64k(
    maxTypes = 5,
    idleDrain = 0.5,
    bytesPerType = 8,
    size = 1024,
  ),
  FluidCell256k(
    maxTypes = 5,
    idleDrain = 0.5,
    bytesPerType = 8,
    size = 1024,
  ),
  FluidCell1024k(
    maxTypes = 5,
    idleDrain = 0.5,
    bytesPerType = 8,
    size = 1024,
  ),
  FluidCell4096k(
    maxTypes = 5,
    idleDrain = 0.5,
    bytesPerType = 8,
    size = 1024,
  );

  val itemStack: ItemStack
    get() = ItemStack(EC2Item.FluidStorageCell.item, 1, this.ordinal)

}
