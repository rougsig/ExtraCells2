package extracells.feature.item.proxyfluid

import extracells.core.entity.ECFluidStack
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.fluids.FluidStack

internal class ProxyFluidItem : Item() {
  init {
    this.maxStackSize = 1
    this.maxDamage = 0
  }

  fun getFluidStack(itemStack: ItemStack): ECFluidStack {
    return ECFluidStack.createFrom(FluidStack.loadFluidStackFromNBT(itemStack.stackTagCompound))
  }

  fun setFluidStack(itemStack: ItemStack, fluidStack: ECFluidStack) {
    val tag = itemStack.stackTagCompound ?: NBTTagCompound()
    itemStack.stackTagCompound = tag
    fluidStack.toForgeFluidStack()?.writeToNBT(tag)
  }

  override fun getItemStackDisplayName(itemStack: ItemStack): String {
    return getFluidStack(itemStack).toForgeFluidStack()?.getFluid()?.localizedName ?: "Unknown Fluid"
  }
}
