package extracells.feature.part

import appeng.parts.AEBasePart
import extracells.feature.part.fluidbus.FluidExportBusPart
import extracells.feature.part.fluidbus.FluidImportBusPart

internal enum class ECPart(
  val meta: Int,
  val internalName: String,
  val partClass: Class<out AEBasePart>,
) {
  FluidExportBus(
    meta = 1,
    "fluid.export",
    FluidExportBusPart::class.java,
    // generatePair(Upgrades.CAPACITY, 2),
    // generatePair(Upgrades.REDSTONE, 1),
    // generatePair(Upgrades.SPEED, 2)
  ),
   FluidImportBus(
     meta = 2,
     "fluid.import",
     FluidImportBusPart::class.java,
     // generatePair(Upgrades.CAPACITY, 2),
     // generatePair(Upgrades.REDSTONE, 1),
     // generatePair(Upgrades.SPEED, 2)
   ),
  ;


  companion object {
    fun findPartByMeta(meta: Int): ECPart {
      return values().first { it.meta == meta }
    }
  }
}