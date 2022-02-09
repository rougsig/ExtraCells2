package extracells.item

import extracells.item.storage.FluidStorageCell
import net.minecraft.item.Item

enum class EC2Item(
  val internalName: String,
  val item: Item,
) {
  FluidStorageCell(
    // TODO:
    //  rename to storage.fluid after deletion old one
    internalName = "storage.fluid.new",
    item = FluidStorageCell(),
  );
}
