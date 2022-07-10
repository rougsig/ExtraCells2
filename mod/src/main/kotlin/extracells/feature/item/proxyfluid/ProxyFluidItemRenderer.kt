package extracells.feature.item.proxyfluid

import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import extracells.feature.item.ECItem
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.item.ItemStack
import net.minecraftforge.client.IItemRenderer
import net.minecraftforge.client.MinecraftForgeClient
import org.lwjgl.opengl.GL11

@SideOnly(Side.CLIENT)
object ProxyFluidItemRenderer : IItemRenderer {

  fun register() {
    MinecraftForgeClient.registerItemRenderer(ECItem.ProxyFluid.item, this)
  }

  override fun handleRenderType(
    itemStack: ItemStack,
    type: IItemRenderer.ItemRenderType,
  ): Boolean {
    return itemStack.stackTagCompound != null && type == IItemRenderer.ItemRenderType.INVENTORY
  }

  override fun shouldUseRenderHelper(
    type: IItemRenderer.ItemRenderType?,
    item: ItemStack?,
    helper: IItemRenderer.ItemRendererHelper?,
  ): Boolean {
    return false
  }

  override fun renderItem(
    type: IItemRenderer.ItemRenderType,
    itemStack: ItemStack,
    vararg data: Any?,
  ) {
    val fluidStack = (itemStack.item as ProxyFluidItem).getFluidStack(itemStack).toForgeFluidStack()

    Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture)
    GL11.glDisable(GL11.GL_LIGHTING)
    GL11.glEnable(GL11.GL_BLEND)
    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
    GL11.glColor3f(1f, 1f, 1f)

    val fluid = fluidStack?.fluid
    if (fluid != null) {
      val icon = fluid.icon
      GL11.glColor3f(
        (fluid.color shr 16 and 0xFF) / 255.0f,
        (fluid.color shr 8 and 0xFF) / 255.0f,
        (fluid.color and 0xFF) / 255.0f,
      )
      val tess = Tessellator.instance
      tess.startDrawingQuads()
      tess.addVertexWithUV(0.0, 16.0, 0.0, icon.minU.toDouble(), icon.maxV.toDouble())
      tess.addVertexWithUV(16.0, 16.0, 0.0, icon.maxU.toDouble(), icon.maxV.toDouble())
      tess.addVertexWithUV(16.0, 0.0, 0.0, icon.maxU.toDouble(), icon.minV.toDouble())
      tess.addVertexWithUV(0.0, 0.0, 0.0, icon.minU.toDouble(), icon.minV.toDouble())
      tess.draw()
    }

    GL11.glColor3f(1f, 1f, 1f)
    GL11.glEnable(GL11.GL_LIGHTING)
    GL11.glDisable(GL11.GL_BLEND)
  }
}
