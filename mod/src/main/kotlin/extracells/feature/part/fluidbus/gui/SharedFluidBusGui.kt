package extracells.feature.part.fluidbus.gui

import appeng.client.gui.AEBaseGui
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import extracells.ExtraCells
import extracells.feature.part.fluidbus.SharedFluidBusPart
import net.minecraft.entity.player.EntityPlayer

@SideOnly(Side.CLIENT)
internal class SharedFluidBusGui(
  private val part: SharedFluidBusPart,
  private val player: EntityPlayer,
) : AEBaseGui(SharedFluidBusContainer(part, player)) {
  init {
    xSize = 176
    ySize = 204
  }

  override fun drawFG(offsetX: Int, offsetY: Int, mouseX: Int, mouseY: Int) {
  }

  override fun drawBG(offsetX: Int, offsetY: Int, mouseX: Int, mouseY: Int) {
    this.bindTexture(ExtraCells.MOD_ID, "gui/busiofluid.png")
    this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize)
  }
}
