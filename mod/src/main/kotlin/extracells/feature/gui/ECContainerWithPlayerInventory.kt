package extracells.feature.gui

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.inventory.Slot

internal abstract class ECContainerWithPlayerInventory(
  player: EntityPlayer,
) : ECContainerBase(player) {
  protected fun bindPlayerInventory(inventoryPlayer: InventoryPlayer, offsetX: Int, offsetY: Int) {
    // bind player inventory
    for (i in 0 until ROWS) {
      for (j in 0 until COLUMNS) {
        addSlotToContainer(Slot(
          inventoryPlayer,
          j + i * ROWS + COLUMNS,
          SLOT_MARGIN + j * SLOT_SIZE + offsetX,
          i * SLOT_SIZE + offsetY,
        ))
      }
    }

    // bind player hotbar
    for (i in 0 until ROWS) {
      addSlotToContainer(Slot(
        inventoryPlayer,
        i,
        SLOT_MARGIN + i * SLOT_SIZE + offsetX,
        SLOT_SIZE * ROWS + HOTBAR_ROW_MARGIN_TOP + offsetY,
      ))
    }
  }
}

private const val ROWS = 3
private const val COLUMNS = 9
private const val SLOT_SIZE = 18
private const val SLOT_MARGIN = 8
private const val HOTBAR_ROW_MARGIN_TOP = 4
