package extracells.feature.block.certustank

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler
import cpw.mods.fml.client.registry.RenderingRegistry
import net.minecraft.block.Block
import net.minecraft.client.renderer.RenderBlocks
import net.minecraft.client.renderer.Tessellator
import net.minecraft.world.IBlockAccess

// TODO: Rewrite.
//  Just migrated java to kotlin code
internal object CertusTankRenderHandler : ISimpleBlockRenderingHandler {
  var _renderId: Int = 0
    private set
  var renderPass: Int = 0

  private val model = CertusTankModel()

  fun register() {
    _renderId = RenderingRegistry.getNextAvailableRenderId()
    RenderingRegistry.registerBlockHandler(_renderId, this)
  }

  override fun renderInventoryBlock(
    block: Block?,
    metadata: Int,
    modelId: Int,
    renderer: RenderBlocks?,
  ) = Unit

  override fun renderWorldBlock(
    world: IBlockAccess,
    x: Int,
    y: Int,
    z: Int,
    block: Block?,
    modelId: Int,
    renderer: RenderBlocks,
  ): Boolean {
    if (block is CertusTankBlock) {
      Tessellator.instance.setColorOpaque_F(1f, 1f, 1f)
      val oldAO = renderer.enableAO
      renderer.enableAO = false
      if (renderPass == 0) {
        model.renderOuterBlock(block, x, y, z, renderer, world)
      } else {
        model.renderInnerBlock(block, x, y, z, renderer, world)
        val tileEntity = world.getTileEntity(x, y, z)
        model.renderFluid(tileEntity, x.toDouble(), y.toDouble(), z.toDouble(), renderer)
      }
      renderer.enableAO = oldAO

      return true
    }
    return false
  }

  override fun shouldRender3DInInventory(modelId: Int): Boolean {
    return true
  }

  override fun getRenderId(): Int {
    return _renderId
  }
}