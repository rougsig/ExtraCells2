package extracells.feature.certustank

import extracells.core.entity.FluidStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.fluids.Fluid
import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.fluids.FluidStack as ForgeFluidStack
import net.minecraftforge.fluids.FluidTankInfo
import net.minecraftforge.fluids.IFluidHandler
import java.util.*
import kotlin.math.min

internal class CertusTankTileEntity : TileEntity(), IFluidHandler {
  var fluidName: String? = null
    private set
  var fluidAmount: Int = 0
    private set

  private val capacity = CERTUS_TANK_CAPACITY

  private fun fill(fluidName: String, amount: Int, doFill: Boolean): Int {
    if (!canFill(fluidName)) return 0

    val maxFluidAmount = min(this.capacity - this.fluidAmount, amount)
    if (doFill) {
      this.fluidName = fluidName
      this.fluidAmount += maxFluidAmount
    }
    return maxFluidAmount
  }

  private fun fillTower(fluidName: String, amount: Int, doFill: Boolean): Int {
    val tower = this.getTankTower()

    var needToFillAmount = amount
    val iterator = tower.iterator()
    while (needToFillAmount > 0 && iterator.hasNext()) {
      val tank = iterator.next()
      val filled = tank.fill(fluidName, needToFillAmount, doFill)
      needToFillAmount -= filled
    }

    return amount - needToFillAmount
  }

  private fun canFill(fluidName: String?): Boolean {
    return fluidAmount < capacity
      && (this.fluidName == null || this.fluidName == fluidName)
  }

  private fun drain(fluidName: String?, amount: Int, doDrain: Boolean): FluidStack {
    if (!canDrain(fluidName)) return FluidStack.Empty

    val maxFluidAmount = min(this.fluidAmount, amount)
    if (doDrain) {
      this.fluidAmount -= maxFluidAmount
    }
    val result = FluidStack(
      this.fluidName!!,
      maxFluidAmount,
    )
    if (this.fluidAmount == 0) this.fluidName = null
    return result
  }

  private fun drainTower(fluidName: String?, amount: Int, doFill: Boolean): FluidStack {
    val tower = this.getTankTower()
    if (!tower.first().canDrain(fluidName)) return FluidStack.Empty

    val drainedFluidName = tower.first().fluidName
    var needToDrainAmount = amount
    val iterator = tower.iterator()
    while (needToDrainAmount > 0 && iterator.hasNext()) {
      val tank = iterator.next()
      val drained = tank.drain(fluidName, needToDrainAmount, doFill).amount
      needToDrainAmount -= drained
    }

    return FluidStack(
      drainedFluidName!!,
      amount - needToDrainAmount
    )
  }

  private fun canDrain(fluidName: String?): Boolean {
    return this.fluidName != null && fluidAmount > 0
      && (fluidName == null || this.fluidName == fluidName)
  }

  private fun getTankInfo(): FluidTankInfo {
    val tower = this.getTankTower()

    val amount = tower.sumOf { it.fluidAmount }
    val fluid = this.fluidName
      ?.let { fluidName -> ForgeFluidStack(FluidRegistry.getFluid(fluidName), amount) }

    val capacity = tower.size * CERTUS_TANK_CAPACITY
    return FluidTankInfo(fluid, capacity)
  }

  private fun getTankTower(): List<CertusTankTileEntity> {
    val list = LinkedList<CertusTankTileEntity>()
    list.add(this)

    var fluidName = this.fluidName
    var y = 0

    y = this.yCoord
    while (true) {
      y -= 1
      val below = this.worldObj.getTileEntity(xCoord, y, zCoord) as? CertusTankTileEntity
      val belowFluidName = below?.fluidName

      if (below != null && (fluidName == null || belowFluidName == null || belowFluidName == fluidName)) {
        fluidName = belowFluidName
        list.addFirst(below)
      } else break
    }

    y = this.yCoord
    while (true) {
      y += 1
      val upper = this.worldObj.getTileEntity(xCoord, y, zCoord) as? CertusTankTileEntity
      val upperFluidName = upper?.fluidName

      if (upper != null && (fluidName == null || upperFluidName == null || upperFluidName == fluidName)) {
        fluidName = upperFluidName
        list.addLast(upper)
      } else break
    }

    return list
  }

  override fun readFromNBT(tag: NBTTagCompound?) {
    super.readFromNBT(tag)
    if (tag != null) {
      if (tag.hasKey("Empty")) {
        this.fluidName = null
        this.fluidAmount = 0
      } else {
        this.fluidName = tag.getString("FluidName")
        this.fluidAmount = tag.getInteger("Amount")
      }
    }
  }

  override fun writeToNBT(tag: NBTTagCompound?) {
    super.writeToNBT(tag)
    if (tag != null) {
      if (this.fluidName == null) {
        tag.setString("Empty", "")
      } else {
        tag.setString("FluidName", this.fluidName)
        tag.setInteger("Amount", this.fluidAmount)
      }
    }
  }

  // region Forge
  override fun fill(from: ForgeDirection?, resource: ForgeFluidStack?, doFill: Boolean): Int {
    if (resource == null) return 0
    return fillTower(resource.getFluid().name, resource.amount, doFill)
  }

  override fun canFill(from: ForgeDirection?, fluid: Fluid?): Boolean {
    return canFill(fluidName = fluid?.name)
  }

  override fun drain(from: ForgeDirection?, resource: ForgeFluidStack?, doDrain: Boolean): ForgeFluidStack? {
    if (resource == null) return null
    return drainTower(resource.getFluid().name, resource.amount, doDrain).toForgeFluidStack()
  }

  override fun drain(from: ForgeDirection?, maxDrain: Int, doDrain: Boolean): ForgeFluidStack? {
    return drain(fluidName = null, maxDrain, doDrain).toForgeFluidStack()
  }

  override fun canDrain(from: ForgeDirection?, fluid: Fluid?): Boolean {
    return canDrain(fluidName = fluid?.name)
  }

  override fun getTankInfo(from: ForgeDirection?): Array<FluidTankInfo> {
    return arrayOf(getTankInfo())
  }
  // endregion
}
