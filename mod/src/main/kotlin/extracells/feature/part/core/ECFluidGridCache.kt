package extracells.feature.part.core

import appeng.api.AEApi
import appeng.api.config.Actionable
import appeng.api.networking.IGrid
import appeng.api.networking.IGridHost
import appeng.api.networking.IGridNode
import appeng.api.networking.IGridStorage
import appeng.api.networking.security.BaseActionSource
import appeng.api.networking.storage.IStorageGrid
import appeng.api.storage.IMEMonitor
import appeng.api.storage.data.IAEFluidStack
import extracells.core.entity.ECFluidStack
import extracells.extension.createFluidStack

// TODO: implement energy drain
internal class ECFluidGridCache(
  private val grid: IGrid,
) : ECFluidGrid {
  private val listeners = mutableSetOf<ECFluidMonitor.Listener>()

  private val fluidInventory: IMEMonitor<IAEFluidStack>
    get() = grid.getCache<IStorageGrid>(IStorageGrid::class.java).fluidInventory

  private var notifyListeners = true

  // region fluid
  override fun inject(src: BaseActionSource, fluidName: String, amount: Int): Int {
    val stack = AEApi.instance().createFluidStack(fluidName, amount)
    val notInjected = fluidInventory.injectItems(stack, Actionable.MODULATE, src)
    this.notifyListeners = this.notifyListeners || notInjected.fluidStack.amount < amount
    return notInjected.fluidStack.amount
  }

  override fun simulateInject(src: BaseActionSource, fluidName: String, amount: Int): Int {
    val stack = AEApi.instance().createFluidStack(fluidName, amount)
    val notInjected = fluidInventory.injectItems(stack, Actionable.SIMULATE, src)
    return amount - notInjected.fluidStack.amount
  }

  override fun extract(src: BaseActionSource, fluidName: String, amount: Int): Int {
    val stack = AEApi.instance().createFluidStack(fluidName, amount)
    val extracted = fluidInventory.extractItems(stack, Actionable.MODULATE, src)
    val extractedAmount = extracted?.fluidStack?.amount ?: 0
    this.notifyListeners = this.notifyListeners || extractedAmount > 0
    return extractedAmount
  }

  override fun simulateExtract(src: BaseActionSource, fluidName: String, amount: Int): Int {
    val stack = AEApi.instance().createFluidStack(fluidName, amount)
    val extracted = fluidInventory.extractItems(stack, Actionable.SIMULATE, src)
    val extractedAmount = extracted?.fluidStack?.amount ?: 0
    return extractedAmount
  }

  override fun addListener(listener: ECFluidMonitor.Listener) {
    listeners.add(listener)
  }

  override fun removeListener(listener: ECFluidMonitor.Listener) {
    listeners.remove(listener)
  }

  override val storedFluids: List<ECFluidStack>
    get() = fluidInventory.storageList
      .map { aeFluid -> ECFluidStack(aeFluid.fluid.name, aeFluid.fluidStack.amount) }
  // endregion fluid

  // region appeng
  override fun onUpdateTick() {
    if (this.notifyListeners) {
      this.notifyListeners = false
      val storedFluids = this.storedFluids
      this.listeners.forEach { listener ->
        listener.onFluidsChange(storedFluids)
      }
    }
  }

  override fun removeNode(gridNode: IGridNode?, machine: IGridHost?) {
    // no-op for now
  }

  override fun addNode(gridNode: IGridNode?, machine: IGridHost?) {
    // no-op for now
  }

  override fun onSplit(destinationStorage: IGridStorage?) {
    // no-op
  }

  override fun onJoin(sourceStorage: IGridStorage?) {
    this.notifyListeners = true
  }

  override fun populateGridStorage(destinationStorage: IGridStorage?) {
    // no-op
  }
  // endregion appeng
}
