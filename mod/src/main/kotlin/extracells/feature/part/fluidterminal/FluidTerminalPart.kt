package extracells.feature.part.fluidterminal

import appeng.api.networking.ticking.TickRateModulation
import appeng.api.networking.ticking.TickingRequest
import appeng.tile.inventory.AppEngInternalInventory
import appeng.tile.inventory.IAEAppEngInventory
import appeng.tile.inventory.InvOperation
import extracells.extension.incrStackSize
import extracells.feature.gui.ECGui
import extracells.feature.part.ECPart
import extracells.feature.part.core.ECTickablePart
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.util.Vec3
import net.minecraftforge.fluids.Fluid
import net.minecraftforge.fluids.FluidContainerRegistry
import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.fluids.FluidStack

internal class FluidTerminalPart : ECTickablePart(ECPart.FluidTerminal), IAEAppEngInventory {
  companion object {
    const val INPUT_SLOT_INDEX = 0
    const val OUTPUT_SLOT_INDEX = 1
  }

  val internalInventory = AppEngInternalInventory(this, 2)

  var selectedFluid: Fluid? = null
    set(value) {
      field = value
      if (this.canDoWork()) this.wakeDevice()
    }

  private var inputSlot: ItemStack?
    set(value) = internalInventory.setInventorySlotContents(INPUT_SLOT_INDEX, value)
    get() = internalInventory.getStackInSlot(INPUT_SLOT_INDEX)

  private var outputSlot: ItemStack?
    set(value) = internalInventory.setInventorySlotContents(OUTPUT_SLOT_INDEX, value)
    get() = internalInventory.getStackInSlot(OUTPUT_SLOT_INDEX)

  // region Tickable
  override fun createTickingRequest(): TickingRequest {
    return TickingRequest(5, 20, this.canDoWork(), false)
  }

  private fun isInputFluidContainer(): Boolean {
    return FluidContainerRegistry.isContainer(this.inputSlot)
  }

  private fun hasInput(): Boolean {
    return (this.inputSlot?.stackSize ?: 0) > 0
  }

  // TODO: add checks for max stack size
  private fun hasOutputSpace(): Boolean {
    return (this.outputSlot?.stackSize ?: 0) < 64
  }

  private fun isInjectMode(): Boolean {
    return FluidContainerRegistry.isFilledContainer(this.inputSlot)
  }

  private fun isSameContainers(): Boolean {
    val isInjectMode = isInjectMode()

    val filledContainers = if (isInjectMode) this.inputSlot else this.outputSlot
    val emptyContainers = if (isInjectMode) this.outputSlot else this.inputSlot

    val filledContainerType = FluidContainerRegistry.drainFluidContainer(filledContainers)
    return filledContainers == null || emptyContainers == null || filledContainerType.isItemEqual(emptyContainers)
  }

  private fun isExtractFluidSelected(): Boolean {
    return this.selectedFluid != null
  }

  private fun isSameFluid(): Boolean {
    val selectedFluid = this.selectedFluid
    val filledFluid = FluidContainerRegistry.getFluidForFilledItem(this.outputSlot)
    return filledFluid == null || (selectedFluid != null && selectedFluid.name == filledFluid?.getFluid()?.name)
  }

  override fun canDoWork(): Boolean {
    return if (isInjectMode()) {
      isInputFluidContainer() && hasInput() && hasOutputSpace() && isSameContainers()
    } else {
      isExtractFluidSelected() && isInputFluidContainer() && hasInput() && hasOutputSpace() && isSameContainers() && isSameFluid()
    }
  }

  private fun doInjectWork(ticks: Int): TickRateModulation {
    val fluidMonitor = requireFluidMonitor

    val filledContainers = this.inputSlot
    val emptyContainers = this.outputSlot

    val fluidStack = FluidContainerRegistry.getFluidForFilledItem(filledContainers)
    val fluidName = fluidStack.getFluid().name

    val injectedAmount = fluidMonitor.simulateInject(src, fluidName, fluidStack.amount)
    if (injectedAmount == fluidStack.amount) {
      fluidMonitor.inject(src, fluidName, fluidStack.amount)
      internalInventory.decrStackSize(INPUT_SLOT_INDEX, 1)

      if (emptyContainers == null) {
        this.outputSlot = FluidContainerRegistry.drainFluidContainer(filledContainers)
      } else {
        internalInventory.incrStackSize(OUTPUT_SLOT_INDEX, 1)
      }
    }

    return TickRateModulation.FASTER
  }

  private fun doExtractWork(ticks: Int): TickRateModulation {
    val fluidMonitor = requireFluidMonitor

    val filledContainers = this.outputSlot
    val emptyContainers = this.inputSlot

    val fluidStack = FluidStack(
      this.selectedFluid!!,
      FluidContainerRegistry.BUCKET_VOLUME,
    )
    val fluidName = this.selectedFluid!!.name

    val amountToExtract = FluidContainerRegistry.getContainerCapacity(fluidStack, emptyContainers)
    // 0 means unsupported fluid type for container
    if (amountToExtract == 0) return TickRateModulation.SLEEP

    val extractedAmount = fluidMonitor.simulateExtract(src, fluidName, amountToExtract)
    if (extractedAmount == amountToExtract) {
      fluidMonitor.extract(src, fluidName, extractedAmount)
      internalInventory.decrStackSize(INPUT_SLOT_INDEX, 1)

      if (filledContainers == null) {
        this.outputSlot = FluidContainerRegistry.fillFluidContainer(
          FluidStack(FluidRegistry.getFluid(fluidName), extractedAmount),
          emptyContainers,
        )
      } else {
        internalInventory.incrStackSize(OUTPUT_SLOT_INDEX, 1)
      }
    }

    return TickRateModulation.IDLE
  }

  override fun doWork(ticks: Int): TickRateModulation {
    return if (isInjectMode()) doInjectWork(ticks) else doExtractWork(ticks)
  }
  // endregion Tickable

  // region PlayerIntersections
  override fun onActivate(player: EntityPlayer?, pos: Vec3?): Boolean {
    return ECGui.FluidTerminal.launch(player, tile, side)
  }
  // endregion PlayerIntersections

  // region IAEAppEngInventory
  override fun saveChanges() {
    this.markForSave()
  }

  override fun onChangeInventory(
    inv: IInventory?,
    slot: Int,
    mc: InvOperation?,
    removedStack: ItemStack?,
    newStack: ItemStack?
  ) {
    if (inv == internalInventory && this.canDoWork()) this.wakeDevice()
  }
  // endregion
}
