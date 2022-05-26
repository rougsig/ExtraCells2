package extracells.feature.part.core

import appeng.api.networking.IGrid
import appeng.api.networking.IGridHost
import appeng.api.networking.IGridNode
import appeng.api.networking.IGridStorage
import appeng.api.networking.security.BaseActionSource
import extracells.core.entity.ECFluidStack

internal class ECFluidGridCache(
  private val grid: IGrid,
) : ECFluidGrid {
  private val listeners = mutableSetOf<ECFluidMonitor.Listener>()
  private var isOutdated: Boolean = true

  // region fluid
  override fun inject(src: BaseActionSource, fluidName: String, amount: Int): Int {
    TODO("Not yet implemented")
  }

  override fun simulateInject(src: BaseActionSource, fluidName: String, amount: Int): Int {
    TODO("Not yet implemented")
  }

  override fun extract(src: BaseActionSource, fluidName: String, amount: Int): Int {
    TODO("Not yet implemented")
  }

  override fun simulateExtract(src: BaseActionSource, fluidName: String, amount: Int): Int {
    TODO("Not yet implemented")
  }

  override fun addListener(listener: ECFluidMonitor.Listener) {
    listeners.add(listener)
  }

  override fun removeListener(listener: ECFluidMonitor.Listener) {
    listeners.remove(listener)
  }

  override val storedFluids: List<ECFluidStack>
    get() = TODO("Not yet implemented")
  // endregion fluid

  // region appeng
  override fun onUpdateTick() {
    // no-op for now
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
    this.isOutdated = true
  }

  override fun populateGridStorage(destinationStorage: IGridStorage?) {
    // no-op
  }
  // endregion appeng
}
