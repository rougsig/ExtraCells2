package extracells.feature.fluidinterface

import extracells.ExtraCells
import extracells.feature.ECBaseBlock
import extracells.extension.EMPTY_TILE_ENTITY
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World

internal class FluidInterfaceBlock : ECBaseBlock(
  modTab = ExtraCells.creativeTab,
  internalName = "fluidinterface",
) {
  override fun createNewTileEntity(world: World, metadata: Int): TileEntity {
    return EMPTY_TILE_ENTITY
  }
}