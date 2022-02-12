package extracells.core.storage

import appeng.api.AEApi
import appeng.api.config.AccessRestriction
import appeng.api.config.Actionable
import appeng.api.networking.security.BaseActionSource
import appeng.api.storage.IMEInventoryHandler
import appeng.api.storage.ISaveProvider
import appeng.api.storage.StorageChannel
import appeng.api.storage.data.IAEFluidStack
import appeng.api.storage.data.IItemList
import extracells.api.IFluidStorageCell
import extracells.api.storage.IFluidStorage
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.fluids.FluidStack

// TODO:
//  replace IFluidStorageCell with new one
internal class FluidCellInventoryHandler(
  private val itemStack: ItemStack,
  private val host: ISaveProvider?,
) : IMEInventoryHandler<IAEFluidStack> {
  private val storage: IFluidStorage

  init {
    if (!itemStack.hasTagCompound()) itemStack.tagCompound = NBTTagCompound()

    val totalTypes = (itemStack.item as IFluidStorageCell).getMaxTypes(itemStack)
    val totalBytes = (itemStack.item as IFluidStorageCell).getMaxBytes(itemStack)

    this.storage = NBTTagFluidStorage(
      itemStack.tagCompound,
      maxTypes = totalTypes,
      typeSize = 0, // TODO: pass type size to cell
      size = totalBytes,
    )
  }

  override fun injectItems(input: IAEFluidStack, mode: Actionable, src: BaseActionSource?): IAEFluidStack {
    val fluidName = FluidRegistry.getFluidName(input.fluid)
    val injectFluidAmount = input.fluidStack.amount

    val notInjectedAmount = if (mode == Actionable.MODULATE) storage.inject(fluidName, injectFluidAmount)
    else storage.simulateInject(fluidName, injectFluidAmount)

    if (injectFluidAmount > 0) this.requestHostSave()

    return input.copy()
      .apply { stackSize = notInjectedAmount.toLong() }
  }

  override fun extractItems(request: IAEFluidStack, mode: Actionable, src: BaseActionSource): IAEFluidStack {
    val fluidName = FluidRegistry.getFluidName(request.fluid)
    val extractFluidAmount = request.fluidStack.amount

    val extractedAmount = if (mode == Actionable.MODULATE) storage.extract(fluidName, extractFluidAmount)
    else storage.simulateExtract(fluidName, extractFluidAmount)

    if (extractFluidAmount > 0) this.requestHostSave()

    return request.copy()
      .apply { stackSize = extractedAmount.toLong() }
  }

  private fun requestHostSave() {
    this.host?.saveChanges(this)
  }

  override fun getAvailableItems(out: IItemList<IAEFluidStack>): IItemList<IAEFluidStack> {
    val aeApiStorage = AEApi.instance().storage()
    this.storage.storedFluids.map { (fluidName, amount) ->
      FluidRegistry.getFluid(fluidName)?.let { fluid ->
        out.add(aeApiStorage.createFluidStack(FluidStack(fluid, amount)))
      }
    }
    return out
  }

  // TODO:
  //  implement isPrioritized
  override fun isPrioritized(input: IAEFluidStack?): Boolean {
    return false
  }

  // TODO:
  //  implement canAccept
  override fun canAccept(input: IAEFluidStack?): Boolean {
    return true
  }

  // TODO:
  //  implement getPriority
  override fun getPriority(): Int {
    return 0
  }

  // TODO:
  //  implement getSlot
  override fun getSlot(): Int {
    return 0
  }

  // TODO:
  //  implement validForPass
  override fun validForPass(i: Int): Boolean {
    return true
  }

  override fun getChannel(): StorageChannel {
    return StorageChannel.FLUIDS
  }

  override fun getAccess(): AccessRestriction {
    return AccessRestriction.READ_WRITE
  }
}
