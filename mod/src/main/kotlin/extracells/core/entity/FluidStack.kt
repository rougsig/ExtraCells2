package extracells.core.entity

import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.fluids.FluidStack as ForgeFluidStack

internal data class FluidStack(
  val fluidName: String,
  val amount: Int,
) {
  companion object {
    val Empty = FluidStack(
      fluidName = "none",
      amount = 0,
    )

    fun fromForgeFluidStack(fluidStack: ForgeFluidStack?): FluidStack {
      return if (fluidStack == null) FluidStack.Empty
      else FluidStack(FluidRegistry.getFluidName(fluidStack.getFluid()), fluidStack.amount)
    }
  }

  fun toForgeFluidStack(): ForgeFluidStack? {
    return if (this == Empty) null
    else ForgeFluidStack(FluidRegistry.getFluid(this.fluidName), this.amount)
  }
}
