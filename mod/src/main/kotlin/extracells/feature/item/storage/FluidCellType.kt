package extracells.feature.item.storage

import extracells.client.ECItemTexture
import net.minecraft.util.IIcon

internal enum class FluidCellType(
  val meta: Int,
  private val texture: ECItemTexture,
  val unlocalizedName: String,
  val part: StorageComponentType,
  val maxTypes: Int,
  val typeSize: Int,
  val idleDrain: Double,
) {
  Cell1kFluid(
    meta = 0,
    unlocalizedName = "extracells.item.storage.fluid.1k",
    texture = ECItemTexture.Cell1kFluid,
    part = StorageComponentType.Cell1kFluidPart,
    maxTypes = 9,
    typeSize = 8,
    idleDrain = 0.5,
  ),
  Cell4kFluid(
    meta = 1,
    unlocalizedName = "extracells.item.storage.fluid.4k",
    texture = ECItemTexture.Cell4kFluid,
    part = StorageComponentType.Cell4kFluidPart,
    maxTypes = 9,
    typeSize = 8 * 4,
    idleDrain = 1.0,
  ),
  Cell16kFluid(
    meta = 2,
    unlocalizedName = "extracells.item.storage.fluid.16k",
    texture = ECItemTexture.Cell16kFluid,
    part = StorageComponentType.Cell16kFluidPart,
    maxTypes = 9,
    typeSize = 8 * 16,
    idleDrain = 1.5,
  ),
  Cell64kFluid(
    meta = 3,
    unlocalizedName = "extracells.item.storage.fluid.64k",
    texture = ECItemTexture.Cell64kFluid,
    part = StorageComponentType.Cell64kFluidPart,
    maxTypes = 9,
    typeSize = 8 * 64,
    idleDrain = 2.0,
  ),
  Cell256kFluid(
    meta = 4,
    unlocalizedName = "extracells.item.storage.fluid.256k",
    texture = ECItemTexture.Cell256kFluid,
    part = StorageComponentType.Cell256kFluidPart,
    maxTypes = 9,
    typeSize = 8 * 256,
    idleDrain = 2.5,
  ),
  Cell1024kFluid(
    meta = 5,
    unlocalizedName = "extracells.item.storage.fluid.1024k",
    texture = ECItemTexture.Cell1024kFluid,
    part = StorageComponentType.Cell1024kFluidPart,
    maxTypes = 9,
    typeSize = 8 * 1024,
    idleDrain = 3.0,
  ),
  Cell4096kFluid(
    meta = 6,
    unlocalizedName = "extracells.item.storage.fluid.4096k",
    texture = ECItemTexture.Cell4096kFluid,
    part = StorageComponentType.Cell4096kFluidPart,
    maxTypes = 9,
    typeSize = 8 * 4096,
    idleDrain = 3.5,
  ),
  Cell16384kFluid(
    meta = 7,
    unlocalizedName = "extracells.item.storage.fluid.16384k",
    texture = ECItemTexture.Missing,
    part = StorageComponentType.Cell16384kFluidPart,
    maxTypes = 9,
    typeSize = 8 * 16384,
    idleDrain = 4.0,
  ),
  ;

  val icon: IIcon
    get() = texture.icon

  companion object {
    fun findPartByMeta(meta: Int): FluidCellType {
      return FluidCellType.values().first { it.meta == meta }
    }
  }
}
