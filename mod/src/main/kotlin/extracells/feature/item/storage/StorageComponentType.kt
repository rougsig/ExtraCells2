package extracells.feature.item.storage

import extracells.client.ECItemTexture
import net.minecraft.util.IIcon

internal enum class StorageComponentType(
  val meta: Int,
  private val texture: ECItemTexture,
  val unlocalizedName: String,
  val size: Int,
) {
  Cell256kPart(
    meta = 0,
    unlocalizedName = "extracells.item.storage.component.physical.256k",
    texture = ECItemTexture.Cell256kPart,
    size = 256 * 1024,
  ),
  Cell1024kPart(
    meta = 1,
    unlocalizedName = "extracells.item.storage.component.physical.1024k",
    texture = ECItemTexture.Cell1024kPart,
    size = 1024 * 1024,
  ),
  Cell4096kPart(
    meta = 2,
    unlocalizedName = "extracells.item.storage.component.physical.4096k",
    texture = ECItemTexture.Cell4096kPart,
    size = 4096 * 1024,
  ),
  Cell16384kPart(
    meta = 3,
    unlocalizedName = "extracells.item.storage.component.physical.16384k",
    texture = ECItemTexture.Cell16384kPart,
    size = 16384 * 1024,
  ),

  Cell1kFluidPart(
    meta = 4,
    unlocalizedName = "extracells.item.storage.component.fluid.1k",
    texture = ECItemTexture.Cell1kFluidPart,
    size = 1024,
  ),
  Cell4kFluidPart(
    meta = 5,
    unlocalizedName = "extracells.item.storage.component.fluid.4k",
    texture = ECItemTexture.Cell4kFluidPart,
    size = 4 * 1024,
  ),
  Cell16kFluidPart(
    meta = 6,
    unlocalizedName = "extracells.item.storage.component.fluid.16k",
    texture = ECItemTexture.Cell16kFluidPart,
    size = 16 * 1024,
  ),
  Cell64kFluidPart(
    meta = 7,
    unlocalizedName = "extracells.item.storage.component.fluid.64k",
    texture = ECItemTexture.Cell64kFluidPart,
    size = 64 * 1024,
  ),
  Cell256kFluidPart(
    meta = 8,
    unlocalizedName = "extracells.item.storage.component.fluid.256k",
    texture = ECItemTexture.Cell256kFluidPart,
    size = 256 * 1024,
  ),
  Cell1024kFluidPart(
    meta = 9,
    unlocalizedName = "extracells.item.storage.component.fluid.1024k",
    texture = ECItemTexture.Cell1024kFluidPart,
    size = 1024 * 1024,
  ),
  Cell4096kFluidPart(
    meta = 10,
    unlocalizedName = "extracells.item.storage.component.fluid.4096k",
    texture = ECItemTexture.Cell4096kFluidPart,
    size = 4096 * 1024,
  ),
  Cell16384kFluidPart(
    meta = 11,
    unlocalizedName = "extracells.item.storage.component.fluid.16384k",
    texture = ECItemTexture.Missing,
    size = 16384 * 1024,
  ),
  ;

  val icon: IIcon
    get() = texture.icon

  companion object {
    fun findPartByMeta(meta: Int): StorageComponentType {
      return StorageComponentType.values().first { it.meta == meta }
    }
  }
}