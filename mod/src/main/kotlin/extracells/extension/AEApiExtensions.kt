package extracells.extension

import appeng.api.IAppEngApi
import appeng.api.storage.data.IAEFluidStack
import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.fluids.FluidStack

fun IAppEngApi.createFluidStack(name: String, amount: Int): IAEFluidStack {
  return storage().createFluidStack(
    FluidStack(
      FluidRegistry.getFluid(name),
      amount,
    )
  )
}
