package extracells.feature.part.fluidbus.gui

import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import extracells.feature.gui.container.ECGuiContainer
import extracells.feature.part.fluidbus.SharedFluidBusPart
import net.minecraft.entity.player.EntityPlayer
import org.lwjgl.opengl.GL11

@SideOnly(Side.CLIENT)
internal class SharedFluidBusGui(
  private val part: SharedFluidBusPart,
  private val player: EntityPlayer,
) : ECGuiContainer(SharedFluidBusContainer(part, player)) {

  override fun drawBackground(alpha: Float, sizeX: Int, sizeY: Int) {
    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
    this.bindTexture("busiofluid.png")
    drawTexturedModalRect(guiLeft, guiTop, 0, 0, 176, 204)
  }

}
