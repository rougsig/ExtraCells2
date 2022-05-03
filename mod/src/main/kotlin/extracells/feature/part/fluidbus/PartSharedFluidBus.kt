package extracells.feature.part.fluidbus

import appeng.api.config.Upgrades
import appeng.parts.automation.PartSharedItemBus
import appeng.tile.inventory.AppEngInternalAEInventory
import extracells.feature.item.ECItem
import net.minecraft.item.ItemStack
import net.minecraftforge.fluids.IFluidHandler

abstract class PartSharedFluidBus(id: Int) : PartSharedItemBus(ItemStack(ECItem.Part.item, 1, id)) {
  protected val fluidHandler: IFluidHandler?
    get() = host.tile.worldObj.getTileEntity(
      host.tile.xCoord + side.offsetX,
      host.tile.yCoord + side.offsetY,
      host.tile.zCoord + side.offsetZ,
    ) as? IFluidHandler

  protected val calculateFluidToSend: Int
    get() = when (getInstalledUpgrades(Upgrades.SPEED)) {
      0 -> 144
      1 -> 288
      2 -> 576
      3 -> 1152
      4 -> 2304
      else -> 144
    }

  protected val config: AppEngInternalAEInventory
    get() = getInventoryByName("config") as AppEngInternalAEInventory
}