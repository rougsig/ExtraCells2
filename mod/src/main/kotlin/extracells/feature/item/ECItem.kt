package extracells.feature.item

import extracells.ExtraCells
import extracells.feature.item.part.PartItem
import net.minecraft.item.Item

internal enum class ECItem(
  val internalName: String,
  val item: Item,
) {
  Part(
    "part.base.v2",
    PartItem(),
  ),
  ;

  init {
    this.item.setUnlocalizedName("extracells.$internalName")
    this.item.setCreativeTab(ExtraCells.creativeTab)
  }
}