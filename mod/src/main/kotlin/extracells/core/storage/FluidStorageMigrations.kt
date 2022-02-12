package extracells.core.storage

import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.fluids.FluidStack

// TODO:
//  make two variants of items. Legacy and one one.
//  migration should be realized with minecraft recipe
internal fun migrateFromLegacyToV1(nbt: NBTTagCompound): NBTTagCompound {
  if (nbt.hasKey("storageVersion")) return nbt

  val fluidStorage = nbt.getCompoundTag("fluids")
  (0..4).forEach { index ->
    parseLegacyFluid(nbt, index)?.let { fluidStack ->
      fluidStorage.setInteger(FluidRegistry.getFluidName(fluidStack), fluidStack.amount)
    }
  }
  nbt.setTag("fluids", fluidStorage)
  nbt.setInteger("storageVersion", 1)

  return nbt
}

private fun parseLegacyFluid(nbt: NBTTagCompound, index: Int): FluidStack? {
  val key = "Fluid#$index"
  if (!nbt.hasKey(key)) return null

  val fluidStack = FluidStack.loadFluidStackFromNBT(nbt.getCompoundTag(key))
  nbt.removeTag(key)
  return fluidStack
}
