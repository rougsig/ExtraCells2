package extracells.feature.block.certustank

import net.minecraft.block.Block
import net.minecraft.client.model.ModelBase
import net.minecraft.client.model.ModelRenderer
import net.minecraft.client.renderer.RenderBlocks
import net.minecraft.client.renderer.Tessellator
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.IBlockAccess
import net.minecraftforge.fluids.FluidRegistry
import org.lwjgl.opengl.GL11

// TODO: Rewrite.
//  Just migrated java to kotlin code
internal class CertusTankModel : ModelBase() {
  private var shape: ModelRenderer

  init {
    textureWidth = 64
    textureHeight = 64
    shape = ModelRenderer(this, 0, 0)
    shape.addBox(0f, 0f, 0f, 14, 16, 14)
    shape.setRotationPoint(-7f, -8f, -7f)
    shape.setTextureSize(textureWidth, textureHeight)
    shape.mirror = true
    setRotation(shape, 0f, 0f, 0f)
  }

  fun render(f: Float) {
    shape.render(f)
  }

  fun renderFluid(
    tileEntity: TileEntity?, x: Double, y: Double,
    z: Double, renderer: RenderBlocks
  ) {
    val tessellator = Tessellator.instance
    if (tileEntity != null
      && (tileEntity as CertusTankTileEntity).fluidStack != null
    ) {
      val info = tileEntity.fluidStack
      val storedFluid = info?.getFluid()
      val scale = (info?.amount?.toFloat() ?: 0f) / CERTUS_TANK_CAPACITY.toFloat()
      if (storedFluid != null && scale > 0) {
        GL11.glEnable(GL11.GL_BLEND)
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
        val id = Block.getBlockById(FluidRegistry.WATER.id)
        var fluidIcon = storedFluid.icon
        if (fluidIcon == null) fluidIcon = FluidRegistry.LAVA.icon
        renderer.setRenderBounds(
          0.08, 0.001, 0.08, 0.92,
          (
            scale * 0.999f).toDouble(), 0.92
        )
        tessellator.setColorRGBA_F(
          (storedFluid.color shr 16 and 0xFF) / 255.0f,
          (storedFluid.color shr 8 and 0xFF) / 255.0f,
          (storedFluid.color and 0xFF) / 255.0f,
          1.0f
        )
        tessellator.setNormal(0.0f, -1f, 0.0f)
        renderer.renderFaceYNeg(id, x, y, z, fluidIcon)
        tessellator.setNormal(0.0f, 1.0f, 0.0f)
        renderer.renderFaceYPos(id, x, y, z, fluidIcon)
        tessellator.setNormal(0.0f, 0.0f, -1f)
        renderer.renderFaceZNeg(id, x, y, z, fluidIcon)
        tessellator.setNormal(0.0f, 0.0f, 1.0f)
        renderer.renderFaceZPos(id, x, y, z, fluidIcon)
        tessellator.setNormal(-1f, 0.0f, 0.0f)
        renderer.renderFaceXNeg(id, x, y, z, fluidIcon)
        tessellator.setNormal(1.0f, 0.0f, 0.0f)
        renderer.renderFaceXPos(id, x, y, z, fluidIcon)
      }
    }
  }

  fun renderInnerBlock(
    block: Block, x: Int, y: Int, z: Int,
    renderer: RenderBlocks, world: IBlockAccess
  ) {
    val tessellator = Tessellator.instance
    GL11.glPushMatrix()
    GL11.glEnable(GL11.GL_BLEND)
    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
    tessellator.setBrightness(15728640)
    val tankUp = world.getTileEntity(x, y + 1, z) is CertusTankTileEntity
    val tankDown = world.getTileEntity(x, y - 1, z) is CertusTankTileEntity
    var meta = 0
    if (tankUp && tankDown) meta = 3 else if (tankUp) meta = 2 else if (tankDown) meta = 1
    if (!tankUp) {
      tessellator.setNormal(0f, -1f, 0f)
      renderer.renderFaceYNeg(block, x.toDouble(), (y + 0.99f).toDouble(), z.toDouble(), block.getIcon(1, 0))
    }
    if (!tankDown) {
      tessellator.setNormal(0f, 1f, 0f)
      renderer.renderFaceYPos(block, x.toDouble(), (y - 0.99f).toDouble(), z.toDouble(), block.getIcon(0, 0))
    }
    val sideIcon = block.getIcon(3, meta)
    tessellator.setNormal(0f, 0f, -1f)
    renderer.renderFaceZNeg(block, x.toDouble(), y.toDouble(), (z + 0.875f).toDouble(), sideIcon)
    tessellator.setNormal(0f, 0f, 1f)
    renderer.renderFaceZPos(block, x.toDouble(), y.toDouble(), (z - 0.875f).toDouble(), sideIcon)
    tessellator.setNormal(-1f, 0f, 0f)
    renderer.renderFaceXNeg(block, (x + 0.875f).toDouble(), y.toDouble(), z.toDouble(), sideIcon)
    tessellator.setNormal(1f, 0f, 0f)
    renderer.renderFaceXPos(block, (x - 0.875f).toDouble(), y.toDouble(), z.toDouble(), sideIcon)
    GL11.glPopMatrix()
  }

  fun renderOuterBlock(
    block: Block, x: Int, y: Int, z: Int,
    renderer: RenderBlocks, world: IBlockAccess
  ) {
    val tessellator = Tessellator.instance
    GL11.glPushMatrix()
    GL11.glEnable(GL11.GL_BLEND)
    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
    val tankUp = world.getTileEntity(x, y + 1, z) is CertusTankTileEntity
    val tankDown = world.getTileEntity(x, y - 1, z) is CertusTankTileEntity
    var meta = 0
    if (tankUp && tankDown) meta = 3 else if (tankUp) meta = 2 else if (tankDown) meta = 1
    if (!tankDown) {
      tessellator.setNormal(0.0f, -1f, 0.0f)
      renderer.renderFaceYNeg(block, x.toDouble(), y.toDouble(), z.toDouble(), block.getIcon(0, 0))
    }
    if (!tankUp) {
      tessellator.setNormal(0.0f, 1.0f, 0.0f)
      renderer.renderFaceYPos(block, x.toDouble(), y.toDouble(), z.toDouble(), block.getIcon(1, 0))
    }
    val sideIcon = block.getIcon(3, meta)
    tessellator.setNormal(0.0f, 0.0f, -1f)
    renderer.renderFaceZNeg(block, x.toDouble(), y.toDouble(), z.toDouble(), sideIcon)
    tessellator.setNormal(0.0f, 0.0f, 1.0f)
    renderer.renderFaceZPos(block, x.toDouble(), y.toDouble(), z.toDouble(), sideIcon)
    tessellator.setNormal(-1f, 0.0f, 0.0f)
    renderer.renderFaceXNeg(block, x.toDouble(), y.toDouble(), z.toDouble(), sideIcon)
    tessellator.setNormal(1.0f, 0.0f, 0.0f)
    renderer.renderFaceXPos(block, x.toDouble(), y.toDouble(), z.toDouble(), sideIcon)
    GL11.glPopMatrix()
  }

  private fun setRotation(model: ModelRenderer, x: Float, y: Float, z: Float) {
    model.rotateAngleX = x
    model.rotateAngleY = y
    model.rotateAngleZ = z
  }
}
