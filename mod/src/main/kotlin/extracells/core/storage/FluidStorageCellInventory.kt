package extracells.core.storage

import appeng.api.config.Actionable
import appeng.api.networking.security.BaseActionSource
import appeng.api.storage.IMEInventory
import appeng.api.storage.StorageChannel
import appeng.api.storage.data.IAEFluidStack
import appeng.api.storage.data.IItemList
import extracells.item.storage.FluidStorageCellVariant
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.fluids.FluidStack

class FluidStorageCellInventory(storageStack: ItemStack) : IMEInventory<IAEFluidStack> {
  private val cellVariant: FluidStorageCellVariant
  private val cellData: NBTTagCompound

  // TODO: Use inline class
  //  FluidName to StoredAmount
  private val stored: Array<FluidStack?>
  private var freeBytes = 0

  init {
    if (!storageStack.hasTagCompound()) storageStack.tagCompound = NBTTagCompound()
    this.cellData = storageStack.tagCompound
    this.cellVariant = FluidStorageCellVariant.values()[storageStack.itemDamage]
    this.stored = arrayOfNulls(this.cellVariant.maxTypes)
    this.readCellData()
  }

  // Aka deserializer
  private fun readCellData() {
    var usedBytes = 0
    for (i in (0..stored.lastIndex)) {
      val fluidKey = "F#$i"
      if (this.cellData.hasKey(fluidKey)) {
        val fluidStack: FluidStack? = FluidStack.loadFluidStackFromNBT(this.cellData.getCompoundTag(fluidKey))
        this.stored[i] = fluidStack
        if (fluidStack != null) {
          usedBytes += (fluidStack.amount + this.cellVariant.bytesPerType)
        }
      }
    }
    this.freeBytes = this.cellVariant.size - usedBytes
  }

  override fun injectItems(input: IAEFluidStack, mode: Actionable, source: BaseActionSource): IAEFluidStack? {
    val fluid = input.fluid
    val amountToStore = input.stackSize
    val amountNotStored = this.injectFluidToCell(fluid.name, amountToStore, mode)


    TODO("Not yet implemented")
  }

  private fun injectFluidToCell(fluidName: String, amountToStore: Long, mode: Actionable): Long {
    return amountToStore
  }

  override fun extractItems(request: IAEFluidStack, mode: Actionable, source: BaseActionSource): IAEFluidStack {
    TODO("Not yet implemented")
  }

  override fun getAvailableItems(list: IItemList<IAEFluidStack>): IItemList<IAEFluidStack> {
    // TODO: Fill list with info about contains fluid
    return list
  }

  override fun getChannel(): StorageChannel {
    return StorageChannel.FLUIDS
  }
}
