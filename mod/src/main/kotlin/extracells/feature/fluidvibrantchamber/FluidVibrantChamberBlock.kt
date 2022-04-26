package extracells.feature.fluidvibrantchamber

import extracells.ExtraCells
import extracells.feature.ECBaseBlock
import extracells.extension.EMPTY_TILE_ENTITY
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World

internal class FluidVibrantChamberBlock : ECBaseBlock(
  modTab = ExtraCells.creativeTab,
  internalName = "vibrantchamberfluid",
) {
  override fun createNewTileEntity(world: World, metadata: Int): TileEntity {
    return EMPTY_TILE_ENTITY
  }
}