package extracells.feature.part.core

import appeng.api.networking.IGridNode
import appeng.api.networking.ticking.IGridTickable
import appeng.api.networking.ticking.TickRateModulation
import appeng.api.networking.ticking.TickingRequest
import extracells.feature.part.ECPart

internal abstract class ECTickablePart(part: ECPart) : ECPartBase(part), IGridTickable {
  final override fun getTickingRequest(node: IGridNode): TickingRequest {
    return createTickingRequest()
  }

  final override fun tickingRequest(node: IGridNode, ticks: Int): TickRateModulation {
    return if (canDoWork()) doWork(ticks)
    else TickRateModulation.SLEEP
  }

  protected fun wakeDevice() {
    this.grid?.tickManager?.wakeDevice(this.gridNode)
  }

  protected fun sleepDevice() {
    this.grid?.tickManager?.sleepDevice(this.gridNode)
  }

  protected abstract fun createTickingRequest(): TickingRequest
  protected abstract fun canDoWork(): Boolean
  protected abstract fun doWork(ticks: Int): TickRateModulation
}
