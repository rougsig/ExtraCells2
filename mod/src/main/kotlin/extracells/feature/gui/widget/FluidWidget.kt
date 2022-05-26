package extracells.feature.gui.widget

import extracells.core.entity.ECFluidStack
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraftforge.fluids.Fluid
import net.minecraftforge.fluids.FluidRegistry
import org.lwjgl.opengl.GL11

class FluidWidget(
  private val x: Int,
  private val y: Int,
  private val width: Int,
  private val height: Int,
) : ECWidget() {
  var fluidStack: ECFluidStack? = null

  override fun drawBackground(offsetX: Int, offsetY: Int) {
    val x = this.x + offsetX
    val y = this.y + offsetY

    Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture)
    GL11.glDisable(GL11.GL_LIGHTING)
    GL11.glEnable(GL11.GL_BLEND)
    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
    GL11.glColor3f(1f, 1f, 1f)

    val fluid: Fluid? = FluidRegistry.getFluid(fluidStack?.fluidName)

    if (fluid?.icon != null) {
      GL11.glColor3f(
        (fluid.color shr 16 and 0xFF) / 255.0f,
        (fluid.color shr 8 and 0xFF) / 255.0f,
        (fluid.color and 0xFF) / 255.0f
      )
      drawTexturedModelRectFromIcon(
        x + 1,
        y + 1,
        fluid.icon,
        height - 2,
        width - 2
      )
    }

    GL11.glColor3f(1f, 1f, 1f)
    GL11.glEnable(GL11.GL_LIGHTING)
    GL11.glDisable(GL11.GL_BLEND)
  }

  override fun drawForeground(mouseX: Int, mouseY: Int, offsetX: Int, offsetY: Int) {
    val x = this.x + offsetX
    val y = this.y + offsetY
  }
}
