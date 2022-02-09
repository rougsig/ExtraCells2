package extracells.core.storage

import appeng.api.config.Actionable
import appeng.api.networking.security.BaseActionSource
import appeng.api.storage.IMEInventory
import appeng.api.storage.StorageChannel
import appeng.api.storage.data.IAEFluidStack
import appeng.api.storage.data.IItemList

class FluidStorageCellInventory private constructor() : IMEInventory<IAEFluidStack> {
  override fun injectItems(p0: IAEFluidStack?, p1: Actionable?, p2: BaseActionSource?): IAEFluidStack {
    TODO("Not yet implemented")
  }

  override fun extractItems(p0: IAEFluidStack?, p1: Actionable?, p2: BaseActionSource?): IAEFluidStack {
    TODO("Not yet implemented")
  }

  override fun getAvailableItems(p0: IItemList<IAEFluidStack>?): IItemList<IAEFluidStack> {
    TODO("Not yet implemented")
  }

  override fun getChannel(): StorageChannel {
    TODO("Not yet implemented")
  }
}
