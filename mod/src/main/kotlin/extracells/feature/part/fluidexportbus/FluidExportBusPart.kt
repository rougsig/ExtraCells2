package extracells.feature.part.fluidexportbus

import appeng.api.parts.IPartCollisionHelper
import appeng.api.parts.IPartRenderHelper
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import extracells.feature.part.ECPart
import extracells.feature.part.ECPartBase
import extracells.render.TextureManager
import net.minecraft.client.renderer.RenderBlocks
import net.minecraft.client.renderer.Tessellator

internal class FluidExportBusPart : ECPartBase(ECPart.FluidExportBus.id) {

  override fun cableConnectionRenderTo(): Int {
    return 5
  }

  override fun getBoxes(bch: IPartCollisionHelper) {
    bch.addBox(6.0, 6.0, 12.0, 10.0, 10.0, 13.0)
  }

  @SideOnly(Side.CLIENT)
  override fun renderInventory(rh: IPartRenderHelper, renderer: RenderBlocks?) {
    val ts = Tessellator.instance
    rh.setTexture(TextureManager.EXPORT_SIDE.texture)
    rh.setBounds(6f, 6f, 12f, 10f, 10f, 13f)
  }

  @SideOnly(Side.CLIENT)
  override fun renderStatic(
    x: Int, y: Int, z: Int, rh: IPartRenderHelper,
    renderer: RenderBlocks?
  ) {
    val ts = Tessellator.instance
    rh.setTexture(TextureManager.EXPORT_SIDE.texture)
    rh.setBounds(6f, 6f, 12f, 10f, 10f, 13f)
    rh.renderBlock(x, y, z, renderer)
  }
}