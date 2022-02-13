package extracells.core.storage

import appeng.api.implementations.tiles.IChestOrDrive
import appeng.api.storage.*
import appeng.api.storage.data.IAEStack
import appeng.client.texture.ExtraBlockTextures
import extracells.api.storage.IFluidCell
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.IIcon

internal class FluidCellHandler : ICellHandler {
  override fun getCellInventory(
    itemStack: ItemStack,
    host: ISaveProvider?,
    channel: StorageChannel,
  ): IMEInventoryHandler<out IAEStack<*>>? {
    if (channel != StorageChannel.FLUIDS || itemStack.item !is IFluidCell) return null
    return FluidCellInventoryHandler(itemStack, host)
  }

  override fun isCell(itemStack: ItemStack): Boolean {
    return itemStack.item is IFluidCell
  }

  // TODO:
  //  Implement cell status logic
  override fun getStatusForCell(itemStack: ItemStack?, handler: IMEInventory<out IAEStack<*>>): Int {
    return if (handler is FluidCellInventoryHandler) handler.statusForCell else 0
  }

  override fun cellIdleDrain(itemStack: ItemStack, handler: IMEInventory<out IAEStack<*>>?): Double {
    return (itemStack.item as IFluidCell).getIdleDrain(itemStack)
  }

  // TODO:
  //  Not implemented part for ME chest
  override fun getTopTexture_Light(): IIcon? {
    return ExtraBlockTextures.BlockMEChestItems_Light.icon
  }

  override fun getTopTexture_Medium(): IIcon? {
    return ExtraBlockTextures.BlockMEChestItems_Medium.icon
  }

  override fun getTopTexture_Dark(): IIcon? {
    return ExtraBlockTextures.BlockMEChestItems_Dark.icon
  }

  override fun openChestGui(
    player: EntityPlayer,
    chest: IChestOrDrive,
    cellHandler: ICellHandler,
    inventory: IMEInventoryHandler<out IAEStack<*>>,
    item: ItemStack,
    channel: StorageChannel,
  ) {
    // no-op
  }
}
