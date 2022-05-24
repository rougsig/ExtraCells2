package extracells.feature.part.fluidterminal

import extracells.feature.gui.ECGui
import extracells.feature.part.ECPart
import extracells.feature.part.core.ECPartBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.Vec3

internal class FluidTerminalPart : ECPartBase(ECPart.FluidTerminal) {
  override fun onActivate(player: EntityPlayer?, pos: Vec3?): Boolean {
    return ECGui.FluidTerminal.launch(player, tile, side)
  }
}
