package extracells.feature.part.fluidbus

import appeng.api.networking.ticking.TickingRequest
import appeng.tile.inventory.AppEngInternalInventory
import extracells.feature.gui.ECGui
import extracells.feature.part.ECPart
import extracells.feature.part.core.ECTickablePart
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.Vec3
import net.minecraftforge.fluids.IFluidHandler

internal abstract class SharedFluidBusPart(part: ECPart) : ECTickablePart(part) {
  val config = AppEngInternalInventory(null, 9, 1)

  protected val fluidHandler: IFluidHandler?
    get() {
      val tile = this.tile ?: return null
      val side = this.side ?: return null

      return tile.worldObj.getTileEntity(
        tile.xCoord + side.offsetX,
        tile.yCoord + side.offsetY,
        tile.zCoord + side.offsetZ,
      ) as? IFluidHandler
    }

  // region Tickable
  override fun createTickingRequest(): TickingRequest {
    return TickingRequest(5, 20, this.canDoWork(), false);
  }

  override fun canDoWork(): Boolean {
    return fluidHandler != null && !config.isEmpty
  }
  // endregion Tickable

  // region PlayerIntersections
  override fun onActivate(player: EntityPlayer?, pos: Vec3?): Boolean {
    return ECGui.SharedFluidBus.launch(player, tile, side)
  }
  // endregion PlayerIntersections

  // region IPart
  override fun onNeighborChanged() {
    if (canDoWork()) this.wakeDevice()
  }
  // endregion
}
