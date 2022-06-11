package extracells.feature.part.fluidbus.gui

import appeng.container.AEBaseContainer
import appeng.container.slot.IOptionalSlotHost
import extracells.feature.gui.slot.OptionalSlotFluidFakeTypeOnly
import extracells.feature.part.fluidbus.SharedFluidBusPart
import net.minecraft.entity.player.EntityPlayer

internal class SharedFluidBusContainer(
  private val part: SharedFluidBusPart,
  private val player: EntityPlayer,
) : AEBaseContainer(player.inventory, part), IOptionalSlotHost {

  init {
    this.bindSlots()
  }

  private fun bindSlots() {
    this.bindPlayerInventory(player.inventory, 0, 100)
    val config = part.config

    addSlotToContainer(
      OptionalSlotFluidFakeTypeOnly(
        config,
        this,
        0,
        0,
        0,
        0,
        0,
        0,
      )
    )
  }

  override fun isSlotEnabled(groupNum: Int): Boolean {
    return groupNum == 0
  }
}
