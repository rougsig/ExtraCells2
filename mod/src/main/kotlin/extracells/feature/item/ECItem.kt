package extracells.feature.item

import extracells.ExtraCells
import extracells.feature.item.part.PartItem
import extracells.feature.item.storage.FluidCellItem
import extracells.feature.item.storage.ItemCellItem
import extracells.feature.item.storage.StorageComponentItem
import net.minecraft.item.Item

internal enum class ECItem(
  val internalName: String,
  val item: Item,
) {
  Part(
    "part.base.v2",
    PartItem(),
  ),
  FluidCell(
    "storage.fluid.v2",
    FluidCellItem(),
  ),
  ItemCell(
    "storage.physical.v2",
    ItemCellItem(),
  ),
  StorageComponent(
    "storage.component.v2",
    StorageComponentItem()
  )
  ;

  init {
    this.item.unlocalizedName = "extracells.$internalName"
    this.item.creativeTab = ExtraCells.creativeTab
  }
}