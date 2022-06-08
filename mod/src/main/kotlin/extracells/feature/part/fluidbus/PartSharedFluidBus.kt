package extracells.feature.part.fluidbus

import appeng.api.networking.ticking.TickingRequest
import extracells.feature.part.ECPart
import extracells.feature.part.core.ECTickablePart
import net.minecraftforge.fluids.IFluidHandler

internal abstract class PartSharedFluidBus(part: ECPart) : ECTickablePart(part) {
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
    return fluidHandler != null
  }
  // endregion Tickable

  override fun onNeighborChanged() {
    if (canDoWork()) this.wakeDevice()
  }
}
