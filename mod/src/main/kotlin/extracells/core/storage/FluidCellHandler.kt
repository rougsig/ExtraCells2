package extracells.core.storage

import appeng.api.implementations.tiles.IChestOrDrive
import appeng.api.storage.*
import appeng.api.storage.data.IAEStack
import appeng.client.texture.ExtraBlockTextures
import extracells.api.IFluidStorageCell
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.IIcon

internal class FluidCellHandler : ICellHandler {
  // TODO:
  //  replace IFluidStorageCell with new one
  override fun getCellInventory(
    itemStack: ItemStack,
    host: ISaveProvider?,
    channel: StorageChannel,
  ): IMEInventoryHandler<out IAEStack<*>>? {
    if (channel != StorageChannel.FLUIDS || !(itemStack.item is IFluidStorageCell)) return null
    return FluidCellInventoryHandler(itemStack, host)
  }

  // TODO:
  //  replace IFluidStorageCell with new one
  override fun isCell(itemStack: ItemStack): Boolean {
    return itemStack.item is IFluidStorageCell
  }

  // TODO:
  //  Implement cell status logic
  override fun getStatusForCell(itemStack: ItemStack?, handler: IMEInventory<out IAEStack<*>>?): Int {
    return 0
  }

  // TODO:
  //  Implement cell AE power drain logic
  override fun cellIdleDrain(itemStack: ItemStack, handler: IMEInventory<out IAEStack<*>>?): Double {
    return 0.0
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
