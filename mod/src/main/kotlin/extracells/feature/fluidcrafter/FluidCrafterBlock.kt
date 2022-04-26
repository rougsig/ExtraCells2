package extracells.feature.fluidcrafter

import extracells.ExtraCells
import extracells.feature.ECBaseBlock
import extracells.extension.EMPTY_TILE_ENTITY
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World

internal class FluidCrafterBlock : ECBaseBlock(
  modTab = ExtraCells.creativeTab,
  internalName = "fluidcrafter",
) {
  override fun createNewTileEntity(world: World, metadata: Int): TileEntity {
    return EMPTY_TILE_ENTITY
  }
}