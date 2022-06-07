package extracells.extension

import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack

// TODO: add checks for max stack size
fun IInventory.incrStackSize(slot: Int, qty: Int): ItemStack {
  val cs = this.getStackInSlot(slot)
  val ns = cs.copy()
  ns.stackSize = ns.stackSize + qty
  this.setInventorySlotContents(slot, ns)
  return ns
}
