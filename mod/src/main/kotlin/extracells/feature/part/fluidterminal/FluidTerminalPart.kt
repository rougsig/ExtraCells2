package extracells.feature.part.fluidterminal

import appeng.api.networking.ticking.TickRateModulation
import appeng.api.networking.ticking.TickingRequest
import appeng.tile.inventory.AppEngInternalInventory
import appeng.tile.inventory.IAEAppEngInventory
import appeng.tile.inventory.InvOperation
import extracells.feature.gui.ECGui
import extracells.feature.part.ECPart
import extracells.feature.part.core.ECTickablePart
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.util.Vec3
import net.minecraftforge.fluids.FluidContainerRegistry
import kotlin.math.max
import kotlin.math.min

internal class FluidTerminalPart : ECTickablePart(ECPart.FluidTerminal), IAEAppEngInventory {
  private val inventory = AppEngInternalInventory(this, 2)

  // region Tickable
  override fun createTickingRequest(): TickingRequest {
    return TickingRequest(5, 20, this.canDoWork(), false)
  }

  // TODO: do not uses magic numbers for slot index
  // TODO: check if fluid containers the same
  override fun canDoWork(): Boolean {
    val hasFilledContainers = (inventory.getStackInSlot(0)?.stackSize ?: 0) > 0
    val hasSpaceForEmptyContainers = (inventory.getStackInSlot(1)?.stackSize ?: 0) < 64
    return hasFilledContainers && hasSpaceForEmptyContainers
  }

  override fun doWork(ticks: Int): TickRateModulation {
    val fluidMonitor = requireFluidMonitor

    val filledContainers = inventory.getStackInSlot(0)
    val emptyContainers = inventory.getStackInSlot(1)

    val fluidStack = FluidContainerRegistry.getFluidForFilledItem(filledContainers)
    val injectedAmount = fluidMonitor.simulateInject(src, fluidStack.getFluid().name, fluidStack.amount)
    if (injectedAmount == fluidStack.amount) {
      fluidMonitor.inject(src, fluidStack.getFluid().name, fluidStack.amount)
      val newFilledContainers = filledContainers.copy()
      newFilledContainers.stackSize = max(0, newFilledContainers.stackSize - 1)
      inventory.setInventorySlotContents(0, newFilledContainers)

      val newEmptyContainers = emptyContainers?.copy() ?: FluidContainerRegistry.drainFluidContainer(filledContainers)
      newEmptyContainers.stackSize = min(64, newEmptyContainers.stackSize + 1)
      inventory.setInventorySlotContents(1, newEmptyContainers)
    }

    return TickRateModulation.FASTER
  }
  // endregion Tickable

  // region PlayerIntersections
  override fun onActivate(player: EntityPlayer?, pos: Vec3?): Boolean {
    return ECGui.FluidTerminal.launch(player, tile, side)
  }
  // endregion PlayerIntersections

  override fun getInventoryByName(name: String): IInventory? {
    return when (name) {
      "internal" -> this.inventory
      else -> super.getInventoryByName(name)
    }
  }

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
    if (inv == inventory && this.canDoWork()) this.wakeDevice()
  }
  // endregion
}
