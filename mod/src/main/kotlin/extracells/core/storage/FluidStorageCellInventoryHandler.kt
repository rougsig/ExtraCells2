package extracells.core.storage

import appeng.api.config.AccessRestriction
import appeng.api.storage.IMEInventory
import appeng.api.storage.IMEInventoryHandler
import appeng.api.storage.data.IAEFluidStack

class FluidStorageCellInventoryHandler(
  private val inventory: FluidStorageCellInventory,
) : IMEInventoryHandler<IAEFluidStack>, IMEInventory<IAEFluidStack> by inventory {
  override fun getAccess(): AccessRestriction {
    TODO("Not yet implemented")
  }

  override fun isPrioritized(p0: IAEFluidStack?): Boolean {
    TODO("Not yet implemented")
  }

  override fun canAccept(p0: IAEFluidStack?): Boolean {
    TODO("Not yet implemented")
  }

  override fun getPriority(): Int {
    TODO("Not yet implemented")
  }

  override fun getSlot(): Int {
    TODO("Not yet implemented")
  }

  override fun validForPass(p0: Int): Boolean {
    TODO("Not yet implemented")
  }
}
