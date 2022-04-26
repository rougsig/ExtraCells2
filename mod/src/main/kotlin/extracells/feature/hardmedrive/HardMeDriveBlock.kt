package extracells.feature.hardmedrive

import extracells.ExtraCells
import extracells.feature.ECBaseBlock
import extracells.extension.EMPTY_TILE_ENTITY
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World

internal class HardMeDriveBlock : ECBaseBlock(
  modTab = ExtraCells.creativeTab,
  internalName = "hardmedrive",
) {
  override fun createNewTileEntity(world: World, metadata: Int): TileEntity {
    return EMPTY_TILE_ENTITY
  }
}