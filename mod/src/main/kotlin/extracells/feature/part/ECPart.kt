package extracells.feature.part

import extracells.feature.part.fluidexportbus.FluidExportBusPart

internal enum class ECPart(
  val id: Int,
  val internalName: String,
  val partClass: Class<out ECPartBase>,
) {
  FluidExportBus(
    id = 1,
    "fluid.export",
    FluidExportBusPart::class.java,
    // generatePair(Upgrades.CAPACITY, 2),
    // generatePair(Upgrades.REDSTONE, 1),
    // generatePair(Upgrades.SPEED, 2)
  ),
  // FluidImportBus(
  //   id = 2,
  //   "fluid.import",
  //   FluidImportBusPart::class.java,
  //   // generatePair(Upgrades.CAPACITY, 2),
  //   // generatePair(Upgrades.REDSTONE, 1),
  //   // generatePair(Upgrades.SPEED, 2)
  // ),
  ;


  companion object {
    fun findPartById(id: Int): ECPart {
      return values().first { it.id == id }
    }
  }
}