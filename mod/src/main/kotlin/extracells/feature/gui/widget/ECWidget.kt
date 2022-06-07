package extracells.feature.gui.widget

import net.minecraft.client.gui.Gui

abstract class ECWidget : Gui() {
  abstract fun drawBackground(offsetX: Int = 0, offsetY: Int = 0)
  abstract fun drawForeground(mouseX: Int, mouseY: Int, offsetX: Int = 0, offsetY: Int = 0)

  open fun mouseClicked(mouseX: Int, mouseY: Int, mouseBtn: Int) {
  }
}
