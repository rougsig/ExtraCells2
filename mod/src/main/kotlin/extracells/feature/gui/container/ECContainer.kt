package extracells.feature.gui.container

import extracells.helper.EffectiveSide
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Container

internal abstract class ECContainer(
  protected val player: EntityPlayer,
) : Container() {
  override fun canInteractWith(player: EntityPlayer?): Boolean {
    return !EffectiveSide.isClientSide
  }
}
