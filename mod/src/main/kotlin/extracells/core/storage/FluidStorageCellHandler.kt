package extracells.core.storage

import appeng.api.implementations.tiles.IChestOrDrive
import appeng.api.storage.*
import appeng.api.storage.data.IAEStack
import appeng.client.texture.ExtraBlockTextures
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.IIcon

class FluidStorageCellHandler : ICellHandler {
  override fun isCell(item: ItemStack): Boolean {
    TODO("Not yet implemented")
  }

  override fun getCellInventory(
    item: ItemStack,
    container: ISaveProvider,
    channel: StorageChannel,
  ): IMEInventoryHandler<out IAEStack<*>> {
    TODO("Not yet implemented")
  }

  override fun getStatusForCell(
    item: ItemStack,
    inventory: IMEInventory<out IAEStack<*>>?,
  ): Int {
    TODO("Not yet implemented")
  }

  override fun cellIdleDrain(
    item: ItemStack,
    inventory: IMEInventory<out IAEStack<*>>?,
  ): Double {
    TODO("Not yet implemented")
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
