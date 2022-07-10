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
    this.bindPlayerInventory(player.inventory, 0, 102)
    val config = part.config

    addSlotToContainer(OptionalSlotFluidFakeTypeOnly(config, this, 0, 80, 40, 0, 0, 0))
    addSlotToContainer(OptionalSlotFluidFakeTypeOnly(config, this, 1, 80, 40, -1, 0, 1))
    addSlotToContainer(OptionalSlotFluidFakeTypeOnly(config, this, 2, 80, 40, 1, 0, 1))
    addSlotToContainer(OptionalSlotFluidFakeTypeOnly(config, this, 3, 80, 40, 0, -1, 1))
    addSlotToContainer(OptionalSlotFluidFakeTypeOnly(config, this, 4, 80, 40, 0, 1, 1))
    addSlotToContainer(OptionalSlotFluidFakeTypeOnly(config, this, 5, 80, 40, -1, -1, 2))
    addSlotToContainer(OptionalSlotFluidFakeTypeOnly(config, this, 6, 80, 40, 1, -1, 2))
    addSlotToContainer(OptionalSlotFluidFakeTypeOnly(config, this, 7, 80, 40, -1, 1, 2))
    addSlotToContainer(OptionalSlotFluidFakeTypeOnly(config, this, 8, 80, 40, 1, 1, 2))
  }

  override fun isSlotEnabled(groupNum: Int): Boolean {
    return groupNum == 0
  }
}
