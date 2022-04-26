package extracells.feature.certustank

import net.minecraft.block.Block
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.IFluidContainerItem

internal class CertusTankItem(
  block: Block,
): ItemBlock(
  block,
), IFluidContainerItem {
  override fun getFluid(container: ItemStack?): FluidStack? {
    return null
  }

  override fun fill(container: ItemStack?, resource: FluidStack?, doFill: Boolean): Int {
    return 0
  }

  override fun drain(container: ItemStack?, amount: Int, doDrain: Boolean): FluidStack? {
    return null
  }

  override fun getCapacity(container: ItemStack?): Int {
    return CERTUS_TANK_CAPACITY
  }
}
