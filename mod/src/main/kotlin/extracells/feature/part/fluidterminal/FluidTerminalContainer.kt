package extracells.feature.part.fluidterminal

import extracells.core.entity.ECFluidStack
import extracells.feature.gui.container.ECContainerWithPlayerInventory
import extracells.feature.part.core.ECFluidMonitor
import extracells.helper.EffectiveSide
import net.minecraft.entity.player.EntityPlayer

internal class FluidTerminalContainer(
  private val terminal: FluidTerminalPart,
  player: EntityPlayer,
) : ECContainerWithPlayerInventory(player), ECFluidMonitor.Listener {
  init {
    this.bindSlots()
    this.subscribeOnFluidListChanges()
  }

  private fun bindSlots() {
    this.bindPlayerInventory(player.inventory, hotbarPositionY = 204 - 24, inventoryOffsetY = 204 - 82)
  }

  override fun onContainerClosed(p_75134_1_: EntityPlayer?) {
    super.onContainerClosed(p_75134_1_)
    this.unsubscribeOnFluidListChanges()
  }

  override fun onFluidsChange(fluids: List<ECFluidStack>) {

  }

  private fun subscribeOnFluidListChanges() {
    if (EffectiveSide.isClientSide) return
    terminal.requireFluidMonitor.addListener(this)
  }

  private fun unsubscribeOnFluidListChanges() {
    if (EffectiveSide.isClientSide) return
    terminal.requireFluidMonitor.removeListener(this)
  }
}
