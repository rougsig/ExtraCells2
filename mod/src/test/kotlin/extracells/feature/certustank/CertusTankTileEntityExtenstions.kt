package extracells.feature.certustank

import extracells.core.entity.FluidStack
import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.fluids.FluidStack as ForgeFluidStack

internal fun CertusTankTileEntity.fill(fluidName: String, amount: Int, doFill: Boolean): Int {
  return this.fill(
    ForgeDirection.UNKNOWN,
    ForgeFluidStack(FluidRegistry.getFluid(fluidName), amount),
    doFill,
  )
}

internal fun CertusTankTileEntity.drain(fluidName: String?, amount: Int, doDrain: Boolean): FluidStack {
  return if (fluidName == null) {
    FluidStack.fromForgeFluidStack(
      this.drain(
        ForgeDirection.UNKNOWN,
        amount,
        doDrain,
      )
    )
  } else {
    FluidStack.fromForgeFluidStack(
      this.drain(
        ForgeDirection.UNKNOWN,
        ForgeFluidStack(FluidRegistry.getFluid(fluidName), amount),
        doDrain,
      )
    )
  }
}