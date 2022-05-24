package extracells.core.storage

import extracells.core.entity.ECFluidStack
import extracells.extension.keys
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.fluids.FluidRegistry
import kotlin.math.min
import net.minecraftforge.fluids.FluidStack as ForgeFluidStack

internal class FluidTank(val capacity: Int) {
  var fluidName: String? = null
    private set
  var fluidAmount: Int = 0
    private set

  val forgeFluidStack: ForgeFluidStack?
    get() {
      return if (this.fluidName != null) {
        ForgeFluidStack(FluidRegistry.getFluid(this.fluidName), this.fluidAmount)
      } else {
        null
      }
    }

  fun fill(fluidName: String, amount: Int, doFill: Boolean): Int {
    if (!canFill(fluidName)) return 0

    val maxFluidAmount = min(this.capacity - this.fluidAmount, amount)
    if (doFill) {
      this.fluidName = fluidName
      this.fluidAmount += maxFluidAmount
    }
    return maxFluidAmount
  }

  fun canFill(fluidName: String?): Boolean {
    return fluidAmount < capacity
      && (this.fluidName == null || this.fluidName == fluidName)
  }

  fun drain(fluidName: String?, amount: Int, doDrain: Boolean): ECFluidStack {
    if (!canDrain(fluidName)) return ECFluidStack.Empty

    val maxFluidAmount = min(this.fluidAmount, amount)
    if (doDrain) {
      this.fluidAmount -= maxFluidAmount
    }
    val result = ECFluidStack(
      this.fluidName!!,
      maxFluidAmount,
    )
    if (this.fluidAmount == 0) this.fluidName = null
    return result
  }

  fun canDrain(fluidName: String?): Boolean {
    return this.fluidName != null && fluidAmount > 0
      && (fluidName == null || this.fluidName == fluidName)
  }

  fun readFromNBT(tag: NBTTagCompound?) {
    if (tag != null && tag.keys.isNotEmpty()) {
      if (tag.hasKey("Empty")) {
        this.fluidName = null
        this.fluidAmount = 0
      } else {
        this.fluidName = tag.getString("FluidName")
        this.fluidAmount = tag.getInteger("Amount")
      }
    }
  }

  fun writeToNBT(tag: NBTTagCompound?) {
    if (tag != null) {
      if (this.fluidName == null) {
        tag.setString("Empty", "")
      } else {
        tag.setString("FluidName", this.fluidName)
        tag.setInteger("Amount", this.fluidAmount)
      }
    }
  }
}
