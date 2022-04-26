package extracells.feature

import net.minecraft.block.BlockContainer
import net.minecraft.block.material.Material
import net.minecraft.creativetab.CreativeTabs

internal abstract class ECBaseBlock(
  modTab: CreativeTabs,
  val internalName: String,
  hardness: Float = 2f,
  resistance: Float = 10f,
): BlockContainer(Material.iron) {
  private val fullName = "extracells.block.$internalName"

  init {
    this.setBlockName(fullName)
    this.setCreativeTab(modTab)
    this.setHardness(hardness)
    this.setResistance(resistance)
  }

  override fun getUnlocalizedName(): String {
    return this.fullName
  }
}