package extracells.feature.part

import appeng.api.parts.IPart
import extracells.feature.part.fluidbus.FluidPartExportBus
import extracells.feature.part.fluidbus.FluidPartImportBus
import extracells.feature.part.fluidterminal.FluidTerminalPart

internal enum class ECPart(
  val meta: Int,
  val internalName: String,
  val partClass: Class<out IPart>,
) {
  FluidExportBus(
    meta = 1,
    "fluid.export",
    FluidPartExportBus::class.java,
    // generatePair(Upgrades.CAPACITY, 2),
    // generatePair(Upgrades.REDSTONE, 1),
    // generatePair(Upgrades.SPEED, 2)
  ),
  FluidImportBus(
    meta = 2,
    "fluid.import",
    FluidPartImportBus::class.java,
    // generatePair(Upgrades.CAPACITY, 2),
    // generatePair(Upgrades.REDSTONE, 1),
    // generatePair(Upgrades.SPEED, 2)
  ),
  FluidTerminal(
    meta = 4,
    "fluid.terminal",
    FluidTerminalPart::class.java,
  )
  ;

  companion object {
    fun findPartByMeta(meta: Int): ECPart {
      return values().first { it.meta == meta }
    }
  }
}
