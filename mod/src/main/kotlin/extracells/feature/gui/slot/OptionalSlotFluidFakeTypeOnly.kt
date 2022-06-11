package extracells.feature.gui.slot

import appeng.container.slot.IOptionalSlotHost
import appeng.container.slot.OptionalSlotFakeTypeOnly
import extracells.core.entity.ECFluidStack
import extracells.feature.item.ECItem
import extracells.feature.item.proxyfluid.ProxyFluidItem
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraftforge.fluids.FluidContainerRegistry

class OptionalSlotFluidFakeTypeOnly(
  inv: IInventory,
  containerBus: IOptionalSlotHost,
  idx: Int,
  x: Int,
  y: Int,
  offX: Int,
  offY: Int,
  groupNum: Int,
) : OptionalSlotFakeTypeOnly(inv, containerBus, idx, x, y, offX, offY, groupNum) {
  override fun putStack(itemStack: ItemStack?) {
    val itemStack = itemStack ?: return super.putStack(itemStack)
    // TODO: do not cast that
    val proxyFluidItem = ECItem.ProxyFluid.item as ProxyFluidItem
    if (itemStack.item is ProxyFluidItem) return super.putStack(itemStack)

    val fluidStack = FluidContainerRegistry.getFluidForFilledItem(itemStack) ?: return

    val proxyItemStack = ItemStack(proxyFluidItem)
    proxyFluidItem.setFluidStack(proxyItemStack, ECFluidStack.createFrom(fluidStack))

    super.putStack(proxyItemStack)
  }
}
