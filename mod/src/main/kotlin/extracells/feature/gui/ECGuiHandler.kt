package extracells.feature.gui

import appeng.api.parts.IPartHost
import cpw.mods.fml.common.network.IGuiHandler
import extracells.feature.part.fluidbus.SharedFluidBusPart
import extracells.feature.part.fluidbus.gui.SharedFluidBusGui
import extracells.feature.part.fluidbus.gui.SharedFluidBusContainer
import extracells.feature.part.fluidterminal.FluidTerminalPart
import extracells.feature.part.fluidterminal.gui.FluidTerminalContainer
import extracells.feature.part.fluidterminal.gui.FluidTerminalGui
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World

internal class ECGuiHandler : IGuiHandler {
  override fun getServerGuiElement(meta: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Any {
    val meta = ECGuiMeta.decode(meta)

    val tile = (world.getTileEntity(x, y, z) as IPartHost).getPart(meta.side)
    return when (meta.gui) {
      ECGui.FluidTerminal -> FluidTerminalContainer(tile as FluidTerminalPart, player)
      ECGui.SharedFluidBus -> SharedFluidBusContainer(tile as SharedFluidBusPart, player)
    }
  }

  override fun getClientGuiElement(meta: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Any {
    val meta = ECGuiMeta.decode(meta)

    val tile = (world.getTileEntity(x, y, z) as IPartHost).getPart(meta.side)
    return when (meta.gui) {
      ECGui.FluidTerminal -> FluidTerminalGui(tile as FluidTerminalPart, player)
      ECGui.SharedFluidBus -> SharedFluidBusGui(tile as SharedFluidBusPart, player)
    }
  }
}
