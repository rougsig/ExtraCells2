package extracells.feature.part.fluidbus.gui

import appeng.container.slot.IOptionalSlotHost
import extracells.feature.gui.container.ECContainerWithPlayerInventory
import extracells.feature.gui.slot.FakeFluidTypeSlot
import extracells.feature.part.fluidbus.SharedFluidBusPart
import net.minecraft.entity.player.EntityPlayer

internal class SharedFluidBusContainer(
  private val part: SharedFluidBusPart,
  player: EntityPlayer,
) : ECContainerWithPlayerInventory(player), IOptionalSlotHost {

  init {
    this.bindSlots()
  }

  private fun bindSlots() {
    this.bindPlayerInventory(player.inventory, hotbarPositionY = 204 - 24, inventoryOffsetY = 204 - 82)
    val config = part.config

    addSlotToContainer(FakeFluidTypeSlot(config, 0, 40, 80))
  }

  override fun isSlotEnabled(groupNum: Int): Boolean {
    return groupNum == 0
  }
}
