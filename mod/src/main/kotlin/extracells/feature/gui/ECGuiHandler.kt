package extracells.feature.gui

import appeng.api.parts.IPartHost
import cpw.mods.fml.common.network.IGuiHandler
import extracells.feature.part.fluidterminal.FluidTerminalContainer
import extracells.feature.part.fluidterminal.FluidTerminalGui
import extracells.feature.part.fluidterminal.FluidTerminalPart
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World

internal class ECGuiHandler : IGuiHandler {
  override fun getServerGuiElement(meta: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Any {
    val meta = ECGuiMeta.decode(meta)

    val tile = (world.getTileEntity(x, y, z) as IPartHost).getPart(meta.side) as FluidTerminalPart
    return FluidTerminalContainer(tile, player)
  }

  override fun getClientGuiElement(meta: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Any {
    val meta = ECGuiMeta.decode(meta)

    val tile = (world.getTileEntity(x, y, z) as IPartHost).getPart(meta.side) as FluidTerminalPart
    return FluidTerminalGui(tile, player)
  }
}
