package extracells.feature.craftingstorage

import extracells.ExtraCells
import extracells.feature.ECBaseBlock
import extracells.extension.EMPTY_TILE_ENTITY
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World

internal class CraftingStorageBlock : ECBaseBlock(
  modTab = ExtraCells.creativeTab,
  internalName = "craftingstorage",
) {
  override fun createNewTileEntity(world: World, metadata: Int): TileEntity {
    return EMPTY_TILE_ENTITY
  }
}