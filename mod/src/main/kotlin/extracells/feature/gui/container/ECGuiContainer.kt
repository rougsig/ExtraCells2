package extracells.feature.gui.container

import extracells.ExtraCells
import extracells.feature.gui.widget.ECWidget
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.inventory.Slot
import net.minecraft.util.ResourceLocation

internal abstract class ECGuiContainer(container: ECContainer) : GuiContainer(container) {
  private val widgets = mutableListOf<ECWidget>()

  fun addWidget(widget: ECWidget) {
    this.widgets.add(widget)
  }

  protected fun bindTexture(file: String) {
    val loc = ResourceLocation(ExtraCells.MOD_ID, "textures/gui/$file")
    mc.textureManager.bindTexture(loc)
  }

  protected open fun drawBackground(alpha: Float, sizeX: Int, sizeY: Int) {
  }

  final override fun drawGuiContainerBackgroundLayer(alpha: Float, sizeX: Int, sizeY: Int) {
    drawBackground(alpha, sizeX, sizeY)
    widgets.forEach { it.drawBackground(offsetX = guiLeft, offsetY = guiTop) }
  }

  protected open fun drawForeground(mouseX: Int, mouseY: Int) {
  }

  final override fun drawGuiContainerForegroundLayer(mouseX: Int, mouseY: Int) {
    drawForeground(mouseX, mouseY)
    widgets.forEach { it.drawForeground(mouseX - guiLeft, mouseY - guiTop) }
  }

  override fun mouseClicked(mouseX: Int, mouseY: Int, mouseBtn: Int) {
    super.mouseClicked(mouseX, mouseY, mouseBtn)
    for (widget in widgets) {
      widget.mouseClicked(mouseX - guiLeft, mouseY - guiTop, mouseBtn)
    }
  }

  override fun handleMouseClick(p_146984_1_: Slot?, p_146984_2_: Int, p_146984_3_: Int, p_146984_4_: Int) {
    super.handleMouseClick(p_146984_1_, p_146984_2_, p_146984_3_, p_146984_4_)
  }
}
