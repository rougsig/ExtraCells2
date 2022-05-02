package extracells.item

import extracells.ExtraCells
import net.minecraft.item.Item

@Deprecated("Use ECItem")
internal enum class EC2Item(
  val itemName: String,
  val item: Item,
) {
  FluidCell(
    itemName = "extracells.v2.storage.fluid",
    item = FluidCellItem(),
  );

  init {
    item.unlocalizedName = this.itemName
    item.creativeTab = ExtraCells.creativeTab
  }
}
