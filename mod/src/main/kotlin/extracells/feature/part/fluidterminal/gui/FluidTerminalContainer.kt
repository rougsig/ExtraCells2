package extracells.feature.part.fluidterminal.gui

import extracells.core.entity.ECFluidStack
import extracells.feature.gui.container.ECContainerWithPlayerInventory
import extracells.feature.part.core.ECFluidMonitor
import extracells.feature.part.fluidterminal.FluidTerminalPart
import extracells.feature.part.fluidterminal.netwotk.FluidTerminalClientPacket
import extracells.feature.part.fluidterminal.netwotk.FluidTerminalServerPacket
import extracells.helper.EffectiveSide
import extracells.network.ECNetworkHandler
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.inventory.Slot

internal class FluidTerminalContainer(
  private val terminal: FluidTerminalPart,
  player: EntityPlayer,
) : ECContainerWithPlayerInventory(player), ECFluidMonitor.Listener {

  init {
    this.bindSlots()
    this.subscribeOnFluidListChanges()
    this.requestStoredFluids()
  }

  private fun bindSlots() {
    this.bindPlayerInventory(player.inventory, hotbarPositionY = 204 - 24, inventoryOffsetY = 204 - 82)
    val internalInventory = terminal.getInventoryByName("internal")
    this.addSlotToContainer(Slot(internalInventory, 0, 8, 92))
    this.addSlotToContainer(Slot(internalInventory, 1, 26, 92))
  }

  override fun onContainerClosed(p_75134_1_: EntityPlayer?) {
    super.onContainerClosed(p_75134_1_)
    this.unsubscribeOnFluidListChanges()
  }

  override fun onFluidsChange(fluids: List<ECFluidStack>) {
    this.sendStoredFluids(fluids)
  }

  private fun subscribeOnFluidListChanges() {
    if (EffectiveSide.isClientSide) return
    terminal.requireFluidMonitor.addListener(this)
  }

  private fun unsubscribeOnFluidListChanges() {
    if (EffectiveSide.isClientSide) return
    terminal.requireFluidMonitor.removeListener(this)
  }

  // region network
  private fun requestStoredFluids() {
    if (EffectiveSide.isServerSide) return
    ECNetworkHandler.instance.sendToServer(FluidTerminalServerPacket.create())
  }

  private fun sendStoredFluids(fluids: List<ECFluidStack>) {
    if (EffectiveSide.isClientSide) return
    ECNetworkHandler.instance.sendToPlayer(
      FluidTerminalClientPacket.create(fluids),
      player as EntityPlayerMP,
    )
  }

  fun handleServerPacket(packet: FluidTerminalServerPacket) {
    this.sendStoredFluids(terminal.requireFluidMonitor.storedFluids)
  }
  // endregion network
}
