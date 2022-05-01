package extracells.helper

import net.minecraft.entity.player.EntityPlayer

import net.minecraft.item.ItemStack
import net.minecraftforge.fluids.FluidContainerRegistry


fun disposePlayerItem(
  stack: ItemStack,
  dropStack: ItemStack?,
  entityplayer: EntityPlayer?,
  allowDrop: Boolean
): Boolean {
  return disposePlayerItem(stack, dropStack, entityplayer, allowDrop, true)
}

fun disposePlayerItem(
  stack: ItemStack,
  dropStack: ItemStack?,
  entityplayer: EntityPlayer?,
  allowDrop: Boolean,
  allowReplace: Boolean
): Boolean {
  if (entityplayer == null || entityplayer.capabilities.isCreativeMode) {
    return true
  }
  if (allowReplace && stack.stackSize <= 1) {
    entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null)
    entityplayer.inventory.addItemStackToInventory(dropStack)
    return true
  } else if (allowDrop) {
    stack.stackSize -= 1
    if (dropStack != null && !entityplayer.inventory.addItemStackToInventory(dropStack)) {
      entityplayer.func_146097_a(dropStack, false, true)
    }
    return true
  }
  return false
}

fun isPlayerHoldingFluidContainer(player: EntityPlayer): Boolean {
  return FluidContainerRegistry.isContainer(player.currentEquippedItem)
}
