package extracells.feature.part.fluidbus

import appeng.api.networking.ticking.TickRateModulation
import extracells.feature.part.ECPart
import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.fluids.FluidStack

internal class FluidPartExportBus : SharedFluidBusPart(ECPart.FluidExportBus) {
  override fun doWork(ticks: Int): TickRateModulation {
    var worked = false

    val dest = this.fluidHandler!!
    val fluidMonitor = requireFluidMonitor

    var fluidToSend = 144 * 8 // TODO: add upgrades login
    // for (fluid in config) {
    val fluidStack = FluidStack(FluidRegistry.LAVA, fluidToSend) // TODO: add config with gui

    val maxAmountToSend = dest.fill(requireSide.opposite, fluidStack, false)
    if (maxAmountToSend > 0) {
      val realAmountToSend = fluidMonitor.extract(src, fluidStack.getFluid().name, fluidStack.amount)
      if (realAmountToSend > 0) {
        dest.fill(requireSide.opposite, FluidStack(fluidStack.getFluid(), realAmountToSend), true)
        fluidToSend -= realAmountToSend
        worked = true
      }
    }

    // if (fluidToSend <= 0) break
    // }

    return if (worked) TickRateModulation.FASTER else TickRateModulation.SLOWER
  }
}
