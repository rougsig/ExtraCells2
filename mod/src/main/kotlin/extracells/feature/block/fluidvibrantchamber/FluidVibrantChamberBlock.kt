package extracells.feature.block.fluidvibrantchamber

import extracells.extension.EMPTY_TILE_ENTITY
import extracells.feature.block.ECBlockBase
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World

internal class FluidVibrantChamberBlock : ECBlockBase() {
  override fun createNewTileEntity(world: World, metadata: Int): TileEntity {
    return EMPTY_TILE_ENTITY
  }
}