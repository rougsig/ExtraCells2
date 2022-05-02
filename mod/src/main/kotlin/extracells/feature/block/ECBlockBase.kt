package extracells.feature.block

import net.minecraft.block.BlockContainer
import net.minecraft.block.material.Material

internal abstract class ECBlockBase(
  hardness: Float = 2f,
  resistance: Float = 10f,
  material: Material = Material.iron,
): BlockContainer(material) {
  init {
    this.setHardness(hardness)
    this.setResistance(resistance)
  }
}