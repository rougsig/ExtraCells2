package extracells.feature.fluidvibrantchamber

import extracells.extension.EMPTY_TILE_ENTITY
import extracells.feature.ECBaseBlock
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World

internal class FluidVibrantChamberBlock : ECBaseBlock() {
  override fun createNewTileEntity(world: World, metadata: Int): TileEntity {
    return EMPTY_TILE_ENTITY
  }
}