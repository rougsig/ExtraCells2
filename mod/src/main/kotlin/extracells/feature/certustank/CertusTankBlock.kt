package extracells.feature.certustank

import extracells.ExtraCells
import extracells.feature.ECBaseBlock
import extracells.extension.EMPTY_TILE_ENTITY
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World

internal class CertusTankBlock : ECBaseBlock(
  modTab = ExtraCells.creativeTab,
  internalName = "certustank",
) {
  override fun createNewTileEntity(world: World, metadata: Int): TileEntity {
    return EMPTY_TILE_ENTITY
  }
}

const val CERTUS_TANK_CAPACITY = 32000
