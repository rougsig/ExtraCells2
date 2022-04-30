package extracells.feature.certustank

import extracells.core.entity.FluidStack
import extracells.core.storage.FluidTank
import extracells.helper.isServerWorld
import extracells.network.ChannelHandler
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.NetworkManager
import net.minecraft.network.Packet
import net.minecraft.network.play.server.S35PacketUpdateTileEntity
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.fluids.Fluid
import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.fluids.FluidTankInfo
import net.minecraftforge.fluids.IFluidHandler
import java.util.*
import net.minecraftforge.fluids.FluidStack as ForgeFluidStack

internal class CertusTankTileEntity : TileEntity(), IFluidHandler {
  private val storage = FluidTank(CERTUS_TANK_CAPACITY)
  val fluidStack: ForgeFluidStack?
    get() = storage.forgeFluidStack

  private fun fillTower(fluidName: String, amount: Int, doFill: Boolean): Int {
    val tower = this.getTankTower()

    var needToFillAmount = amount
    val iterator = tower.iterator()
    while (needToFillAmount > 0 && iterator.hasNext()) {
      val tank = iterator.next()
      val filled = tank.storage.fill(fluidName, needToFillAmount, doFill)
      if (filled > 0 && doFill) tank.sendUpdatePackage()
      needToFillAmount -= filled
    }

    return amount - needToFillAmount
  }

  private fun drainTower(fluidName: String?, amount: Int, doDrain: Boolean): FluidStack {
    val tower = this.getTankTower()
    if (!tower.first().storage.canDrain(fluidName)) return FluidStack.Empty

    val drainedFluidName = tower.first().storage.fluidName
    var needToDrainAmount = amount
    val iterator = tower.iterator()
    while (needToDrainAmount > 0 && iterator.hasNext()) {
      val tank = iterator.next()
      val drained = tank.storage.drain(fluidName, needToDrainAmount, doDrain).amount
      if (drained > 0 && doDrain) tank.sendUpdatePackage()
      needToDrainAmount -= drained
    }

    return FluidStack(
      drainedFluidName!!,
      amount - needToDrainAmount
    )
  }

  private fun getTankTower(): List<CertusTankTileEntity> {
    val list = LinkedList<CertusTankTileEntity>()
    list.add(this)

    var fluidName = storage.fluidName
    var y = 0

    y = this.yCoord
    while (true) {
      y -= 1
      val below = this.worldObj.getTileEntity(xCoord, y, zCoord) as? CertusTankTileEntity
      val belowFluidName = below?.storage?.fluidName

      if (below != null && (fluidName == null || belowFluidName == null || belowFluidName == fluidName)) {
        fluidName = belowFluidName
        list.addFirst(below)
      } else break
    }

    y = this.yCoord
    while (true) {
      y += 1
      val upper = this.worldObj.getTileEntity(xCoord, y, zCoord) as? CertusTankTileEntity
      val upperFluidName = upper?.storage?.fluidName

      if (upper != null && (fluidName == null || upperFluidName == null || upperFluidName == fluidName)) {
        fluidName = upperFluidName
        list.addLast(upper)
      } else break
    }

    return list
  }

  private fun sendUpdatePackage() {
    if (isServerWorld(worldObj)) ChannelHandler.sendPacketToAllPlayers(descriptionPacket, worldObj)
  }

  // region Forge
  override fun fill(from: ForgeDirection?, resource: ForgeFluidStack?, doFill: Boolean): Int {
    if (resource == null) return 0
    return fillTower(resource.getFluid().name, resource.amount, doFill)
  }

  override fun canFill(from: ForgeDirection?, fluid: Fluid?): Boolean {
    TODO("do not use storage for public checks. Should use tower")
    return storage.canFill(fluidName = fluid?.name)
  }

  override fun drain(from: ForgeDirection?, resource: ForgeFluidStack?, doDrain: Boolean): ForgeFluidStack? {
    if (resource == null) return null
    return drainTower(resource.getFluid().name, resource.amount, doDrain).toForgeFluidStack()
  }

  override fun drain(from: ForgeDirection?, maxDrain: Int, doDrain: Boolean): ForgeFluidStack? {
    return drainTower(fluidName = null, maxDrain, doDrain).toForgeFluidStack()
  }

  override fun canDrain(from: ForgeDirection?, fluid: Fluid?): Boolean {
    TODO("do not use storage for public checks. Should use tower")
    return storage.canDrain(fluidName = fluid?.name)
  }

  override fun getTankInfo(from: ForgeDirection?): Array<FluidTankInfo> {
    val tower = this.getTankTower()

    val amount = tower.sumOf { it.storage.fluidAmount }
    val fluid = storage.fluidName
      ?.let { fluidName -> ForgeFluidStack(FluidRegistry.getFluid(fluidName), amount) }

    val capacity = tower.size * CERTUS_TANK_CAPACITY
    return arrayOf(FluidTankInfo(fluid, capacity))
  }

  override fun readFromNBT(tag: NBTTagCompound?) {
    super.readFromNBT(tag)
    readTankFromNBT(tag)
  }

  fun readTankFromNBT(tag: NBTTagCompound?) {
    storage.readFromNBT(tag)
  }

  override fun writeToNBT(tag: NBTTagCompound?) {
    super.writeToNBT(tag)
    writeTankToNBT(tag)
  }

  fun writeTankToNBT(tag: NBTTagCompound?) {
    storage.writeToNBT(tag)
  }

  override fun getDescriptionPacket(): Packet {
    val nbtTag = NBTTagCompound()
    writeToNBT(nbtTag)
    return S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, nbtTag)
  }

  override fun onDataPacket(net: NetworkManager?, packet: S35PacketUpdateTileEntity) {
    worldObj.markBlockRangeForRenderUpdate(
      xCoord, yCoord, zCoord,
      xCoord, yCoord, zCoord,
    )
    readFromNBT(packet.func_148857_g())
  }
  // endregion
}
