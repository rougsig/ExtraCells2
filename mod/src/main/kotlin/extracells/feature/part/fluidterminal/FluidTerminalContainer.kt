package extracells.feature.part.fluidterminal

import extracells.feature.gui.ECContainerWithPlayerInventory
import net.minecraft.entity.player.EntityPlayer

internal class FluidTerminalContainer(player: EntityPlayer) : ECContainerWithPlayerInventory(player) {
  init {
    this.bindPlayerInventory(player.inventory, 0, 0)
  }
}
