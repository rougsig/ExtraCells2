package extracells.feature.gui

import extracells.ExtraCells
import extracells.helper.EffectiveSide
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.util.ForgeDirection

enum class ECGui {
  FluidTerminal,
  SharedFluidBus,
  ;

  fun launch(
    player: EntityPlayer?,
    tile: TileEntity?,
    side: ForgeDirection?,
  ): Boolean {
    if (EffectiveSide.isClientSide || side == null || player == null || tile == null) return false

    val id = ECGuiMeta(
      gui = this,
      side = side,
      isTile = true,
    ).encode()

    player.openGui(
      ExtraCells.instance(),
      id,
      player.entityWorld,
      tile.xCoord,
      tile.yCoord,
      tile.zCoord,
    )

    return true
  }
}
