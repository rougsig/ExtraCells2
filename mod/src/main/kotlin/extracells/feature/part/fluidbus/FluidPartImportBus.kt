package extracells.feature.part.fluidbus

import appeng.api.networking.ticking.TickRateModulation
import extracells.feature.item.ECItem
import extracells.feature.item.proxyfluid.ProxyFluidItem
import extracells.feature.part.ECPart
import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.fluids.FluidStack

internal class FluidPartImportBus : SharedFluidBusPart(ECPart.FluidImportBus) {
  override fun doWork(ticks: Int): TickRateModulation {
    var worked = false

    val dest = this.fluidHandler!!
    val fluidMonitor = requireFluidMonitor

    var fluidToSend = 144 * 8 // TODO: add upgrades login
    for (fluid in config) {
      if (fluid == null) continue
      val selectedFluid = (ECItem.ProxyFluid.item as ProxyFluidItem).getFluidStack(fluid)
      val fluidStack = FluidStack(
        FluidRegistry.getFluid(selectedFluid.name),
        fluidToSend,
      )
      val maxAmountToSend = dest.drain(requireSide.opposite, fluidStack, false)

      if (maxAmountToSend != null) {
        val notInjectedAmount = fluidMonitor.inject(src, maxAmountToSend.getFluid().name, maxAmountToSend.amount)

        val realAmountToSend = maxAmountToSend.amount - notInjectedAmount
        dest.drain(requireSide.opposite, realAmountToSend, true)
        fluidToSend -= realAmountToSend
        worked = true
      }

      if (fluidToSend <= 0) break
    }

    return if (worked) TickRateModulation.FASTER else TickRateModulation.SLOWER
  }
}
