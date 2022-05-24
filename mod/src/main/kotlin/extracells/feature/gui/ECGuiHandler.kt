package extracells.feature.gui

import appeng.client.gui.GuiNull
import cpw.mods.fml.common.network.IGuiHandler
import extracells.feature.part.fluidterminal.FluidTerminalContainer
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World

internal class ECGuiHandler : IGuiHandler {
  override fun getServerGuiElement(meta: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Any {
    // val meta = ECGuiMeta.decode(meta)

    return FluidTerminalContainer(player)
  }

  override fun getClientGuiElement(meta: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Any {
    // val meta = ECGuiMeta.decode(meta)

    return GuiNull(FluidTerminalContainer(player))
  }
}
