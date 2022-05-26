package extracells.feature.part.fluidterminal

import extracells.core.entity.ECFluidStack
import extracells.feature.gui.container.ECGuiContainer
import extracells.feature.gui.widget.FluidWidget
import net.minecraft.entity.player.EntityPlayer
import org.lwjgl.opengl.GL11

internal class FluidTerminalGui(
  private val terminal: FluidTerminalPart,
  private val player: EntityPlayer,
) : ECGuiContainer(FluidTerminalContainer(terminal, player)) {

  private val fluidWidgets = arrayListOf<FluidWidget>()

  init {
    for (i in 0..3) {
      for (j in 0..8) {
        fluidWidgets.add(FluidWidget(
          x = j * 18 + 7,
          y = i * 18 + 17,
          width = 18,
          height = 18,
        ).apply {
          fluidStack = ECFluidStack("lava", 1000)
        })
      }
    }
    this.fluidWidgets.forEach { addWidget(it) }
  }

  override fun drawBackground(alpha: Float, sizeX: Int, sizeY: Int) {
    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
    this.bindTexture("terminalfluid.png")
    drawTexturedModalRect(guiLeft, guiTop, 0, 0, 176, 204)
  }
}
