package extracells.feature.gui.slot

import extracells.core.entity.ECFluidStack
import extracells.feature.item.ECItem
import extracells.feature.item.proxyfluid.ProxyFluidItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack
import net.minecraftforge.fluids.FluidContainerRegistry

class FakeFluidTypeSlot(inventory: IInventory, slotIndex: Int, x: Int, y: Int) : Slot(inventory, slotIndex, x, y) {
  override fun putStack(itemStack: ItemStack?) {
    val itemStack = itemStack ?: return super.putStack(itemStack)
    // TODO: do not cast that
    val proxyFluidItem = ECItem.ProxyFluid.item as ProxyFluidItem
    val fluidStack = FluidContainerRegistry.getFluidForFilledItem(itemStack) ?: return

    val proxyItemStack = ItemStack(proxyFluidItem)
    proxyFluidItem.setFluidStack(proxyItemStack, ECFluidStack.createFrom(fluidStack))

    super.putStack(proxyItemStack)
  }

  override fun canTakeStack(p_82869_1_: EntityPlayer?): Boolean {
    return false
  }
}
