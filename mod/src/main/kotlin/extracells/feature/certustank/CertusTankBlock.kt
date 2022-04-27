package extracells.feature.certustank

import extracells.ExtraCells
import extracells.feature.ECBaseBlock
import extracells.extension.EMPTY_TILE_ENTITY
import net.minecraft.block.material.Material
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World

internal class CertusTankBlock : ECBaseBlock(
  modTab = ExtraCells.creativeTab,
  internalName = "certustank",
  material = Material.glass,
) {
  override fun createNewTileEntity(world: World, metadata: Int): TileEntity {
    return CertusTankTileEntity()
  }
}

const val CERTUS_TANK_CAPACITY = 32000
