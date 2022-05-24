package extracells.feature.gui

import extracells.helper.EffectiveSide
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Container

internal abstract class ECContainerBase(
  protected val player: EntityPlayer,
) : Container() {
  override fun canInteractWith(player: EntityPlayer?): Boolean {
    return !EffectiveSide.isClientSide
  }
}
