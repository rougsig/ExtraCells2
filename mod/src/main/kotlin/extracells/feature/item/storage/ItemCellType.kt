package extracells.feature.item.storage

import extracells.client.ECItemTexture
import net.minecraft.util.IIcon

internal enum class ItemCellType(
  val meta: Int,
  private val texture: ECItemTexture,
  val unlocalizedName: String,
  val part: StorageComponentType,
  val maxTypes: Int,
  val typeSize: Int,
  val idleDrain: Double,
) {
  Cell256k(
    meta = 0,
    unlocalizedName = "extracells.item.storage.physical.256k",
    texture = ECItemTexture.Cell256k,
    part = StorageComponentType.Cell256kPart,
    maxTypes = 63,
    typeSize = 8 * 256,
    idleDrain = 2.5,
  ),
  Cell1024k(
    meta = 1,
    unlocalizedName = "extracells.item.storage.physical.1024k",
    texture = ECItemTexture.Cell1024k,
    part = StorageComponentType.Cell1024kPart,
    maxTypes = 63,
    typeSize = 8 * 1024,
    idleDrain = 3.0,
  ),
  Cell4096k(
    meta = 2,
    unlocalizedName = "extracells.item.storage.physical.4096k",
    texture = ECItemTexture.Cell4096k,
    part = StorageComponentType.Cell4096kPart,
    maxTypes = 63,
    typeSize = 8 * 4096,
    idleDrain = 3.5,
  ),
  Cell16384k(
    meta = 3,
    unlocalizedName = "extracells.item.storage.physical.16384k",
    texture = ECItemTexture.Cell16384k,
    part = StorageComponentType.Cell16384kPart,
    maxTypes = 63,
    typeSize = 8 * 16384,
    idleDrain = 4.0
  ),
  ;

  val icon: IIcon
    get() = texture.icon

  companion object {
    fun findPartByMeta(meta: Int): ItemCellType {
      return ItemCellType.values().first { it.meta == meta }
    }
  }
}
