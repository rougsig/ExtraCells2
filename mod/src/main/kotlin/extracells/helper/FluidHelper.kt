package extracells.helper

import extracells.helper.disposePlayerItem
import extracells.helper.isClientWorld
import extracells.helper.isServerWorld
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.fluids.FluidContainerRegistry
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.IFluidHandler

fun fillContainerFromHandler(
  world: World,
  handler: IFluidHandler,
  player: EntityPlayer,
  tankFluid: FluidStack?
): Boolean {
  var container = player.currentEquippedItem
  if (FluidContainerRegistry.isEmptyContainer(container)) {
    val returnStack = FluidContainerRegistry.fillFluidContainer(tankFluid, container)
    val fluid = FluidContainerRegistry.getFluidForFilledItem(returnStack)
    if (fluid == null || returnStack == null) {
      return false
    }
    if (isClientWorld(world)) {
      return true
    }
    if (!player.capabilities.isCreativeMode) {
      if (container!!.stackSize == 1) {
        player.inventory.setInventorySlotContents(player.inventory.currentItem, returnStack)
        container.stackSize--
        if (container.stackSize <= 0) {
          container = null
        }
      } else {
        if (disposePlayerItem(player.currentEquippedItem, returnStack, player, true)) {
          player.openContainer.detectAndSendChanges()
          (player as EntityPlayerMP).sendContainerAndContentsToPlayer(
            player.openContainer,
            player.openContainer.inventory
          )
        }
      }
    }
    handler.drain(ForgeDirection.UNKNOWN, fluid.amount, true)
    return true
  }
  return false
}

fun fillHandlerWithContainer(world: World, handler: IFluidHandler, player: EntityPlayer): Boolean {
  val container = player.currentEquippedItem
  val fluid = FluidContainerRegistry.getFluidForFilledItem(container)
  if (fluid != null) {
    if (handler.fill(ForgeDirection.UNKNOWN, fluid, false) == fluid.amount || player.capabilities.isCreativeMode) {
      val returnStack = FluidContainerRegistry.drainFluidContainer(container)
      if (isClientWorld(world)) {
        return true
      }
      if (!player.capabilities.isCreativeMode) {
        if (disposePlayerItem(player.currentEquippedItem, returnStack, player, true)) {
          if (isServerWorld(world)) {
            player.openContainer.detectAndSendChanges()
            (player as EntityPlayerMP).sendContainerAndContentsToPlayer(
              player.openContainer,
              player.openContainer.inventory
            )
          }
        }
      }
      handler.fill(ForgeDirection.UNKNOWN, fluid, true)
      return true
    }
  }
  return false
}