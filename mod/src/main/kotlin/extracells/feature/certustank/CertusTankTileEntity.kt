package extracells.feature.certustank

import extracells.tileentity.TileEntityCertusTank
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.fluids.Fluid
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.FluidTank
import net.minecraftforge.fluids.FluidTankInfo
import net.minecraftforge.fluids.IFluidHandler
import java.util.LinkedList

internal class CertusTankTileEntity : TileEntity(), IFluidHandler {
  private val storage = FluidTank(CERTUS_TANK_CAPACITY)

  override fun fill(from: ForgeDirection?, resource: FluidStack?, doFill: Boolean): Int {
    if (resource == null || !this.canFill(from, resource.getFluid())) return 0

    return fillInternal(resource, doFill, isRootFill = true)
  }

  private fun fillInternal(
    resource: FluidStack,
    doFill: Boolean,
    isRootFill: Boolean,
  ): Int {
    if (!isRootFill) return this.storage.fill(resource, doFill)

    val tower = this.getTankTower()

    var needToFillAmount = resource.amount
    val iterator = tower.iterator()
    while (needToFillAmount > 0 && iterator.hasNext()) {
      val tank = iterator.next()
      // TODO: optimize work with Int and Fluid, not with FluidStack
      //  to prevent extra gc overhead
      val filled = tank.fillInternal(FluidStack(resource, needToFillAmount), doFill, isRootFill = false)
      needToFillAmount -= filled
    }

    return resource.amount - needToFillAmount
  }

  override fun drain(from: ForgeDirection?, resource: FluidStack?, doDrain: Boolean): FluidStack? {
    if (resource == null || !this.canDrain(from, resource.getFluid())) return null
    return drain(from, resource.amount, doDrain)
  }

  override fun drain(from: ForgeDirection?, maxDrain: Int, doDrain: Boolean): FluidStack? {
    return drainInternal(maxDrain, doDrain, isRootDrain = true)
  }

  private fun drainInternal(
    maxDrain: Int,
    doDrain: Boolean,
    isRootDrain: Boolean,
  ): FluidStack? {
    return this.storage.drain(maxDrain, doDrain)
  }

  override fun canFill(from: ForgeDirection?, fluid: Fluid?): Boolean {
    return fluid != null
      && this.storage.fluidAmount < CERTUS_TANK_CAPACITY
      && (this.storage.fluid == null || this.storage.fluid.getFluid() == fluid)
  }

  override fun canDrain(from: ForgeDirection?, fluid: Fluid?): Boolean {
    val storedFluid = this.storage.fluid?.getFluid()
    return fluid != null && storedFluid != null && fluid.name == storedFluid.name
  }

  override fun getTankInfo(from: ForgeDirection?): Array<FluidTankInfo> {
    val tower = this.getTankTower()

    val amount = tower.sumOf { it.storage.fluidAmount }
    val fluid = this.storage.fluid?.getFluid()
      ?.let { FluidStack(it, amount) }

    val capacity = tower.size * CERTUS_TANK_CAPACITY
    return arrayOf(FluidTankInfo(fluid, capacity))
  }

  private fun getTankTower(): List<CertusTankTileEntity> {
    val list = LinkedList<CertusTankTileEntity>()
    list.add(this)

    var fluidName = this.storage.fluid?.getFluid()?.name
    var y = 0

    y = this.yCoord
    while (true) {
      y -= 1
      val below = this.worldObj.getTileEntity(xCoord, y, zCoord) as? CertusTankTileEntity
      val belowFluidName = below?.storage?.fluid?.getFluid()?.name

      if (below != null && (fluidName == null || belowFluidName == null || belowFluidName == fluidName)) {
        fluidName = belowFluidName
        list.addFirst(below)
      } else break
    }

    y = this.yCoord
    while (true) {
      y += 1
      val upper = this.worldObj.getTileEntity(xCoord, y, zCoord) as? CertusTankTileEntity
      val upperFluidName = upper?.storage?.fluid?.getFluid()?.name

      if (upper != null && (fluidName == null || upperFluidName == null || upperFluidName == fluidName)) {
        fluidName = upperFluidName
        list.addLast(upper)
      } else break
    }

    return list
  }

  override fun readFromNBT(tag: NBTTagCompound) {
    super.readFromNBT(tag)
    this.storage.readFromNBT(tag)
  }

  override fun writeToNBT(tag: NBTTagCompound) {
    super.writeToNBT(tag)
    this.storage.writeToNBT(tag)
  }
}
