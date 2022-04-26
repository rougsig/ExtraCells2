package extracells.extension

import net.minecraft.tileentity.TileEntity

internal val EMPTY_TILE_ENTITY = object : TileEntity() {
  override fun canUpdate() = false
}
