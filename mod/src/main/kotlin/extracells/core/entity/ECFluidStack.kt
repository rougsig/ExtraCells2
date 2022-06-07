package extracells.core.entity

import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.fluids.FluidStack as ForgeFluidStack

data class ECFluidStack(
  val name: String,
  val amount: Int,
) {
  companion object {
    val Empty = ECFluidStack(
      name = "none",
      amount = 0,
    )

    fun createFrom(fluidStack: ForgeFluidStack?): ECFluidStack {
      return if (fluidStack == null) ECFluidStack.Empty
      else ECFluidStack(FluidRegistry.getFluidName(fluidStack.getFluid()), fluidStack.amount)
    }
  }

  fun toForgeFluidStack(): ForgeFluidStack? {
    return if (this == Empty) null
    else ForgeFluidStack(FluidRegistry.getFluid(this.name), this.amount)
  }
}
