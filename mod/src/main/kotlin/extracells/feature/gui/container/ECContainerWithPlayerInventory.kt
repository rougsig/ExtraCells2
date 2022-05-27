package extracells.feature.gui.container

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.inventory.Slot

internal abstract class ECContainerWithPlayerInventory(
  player: EntityPlayer,
) : ECContainer(player) {
  protected fun bindPlayerInventory(
    playerInventory: InventoryPlayer,
    hotbarPositionY: Int = 0,
    inventoryOffsetY: Int = 0,
  ) {
    // Hot-bar ID's 0-8
    for (column in 0 until COLUMNS) {
      addSlotToContainer(
        Slot(
          playerInventory,
          column,
          INVENTORY_X_OFFSET + column * SLOT_SIZE,
          hotbarPositionY,
        )
      )
    }

    // Main inventory ID's 9-36
    for (row in 0 until ROWS) {
      for (column in 0 until COLUMNS) {
        addSlotToContainer(
          Slot(
            playerInventory,
            COLUMNS + column + row * COLUMNS,
            INVENTORY_X_OFFSET + column * SLOT_SIZE,
            row * SLOT_SIZE + inventoryOffsetY,
          )
        )
      }
    }
  }
}

private const val ROWS = 3
private const val COLUMNS = 9
private const val SLOT_SIZE = 18
private const val INVENTORY_X_OFFSET = 8
