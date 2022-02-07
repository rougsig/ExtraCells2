package extracells.util

import appeng.api.features.IWirelessTermHandler
import appeng.api.util.IConfigManager
import extracells.api.{IWirelessGasTermHandler, IWirelessFluidTermHandler}
import extracells.item.ItemWirelessTerminalUniversalScala
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack


object HandlerUniversalWirelessTerminal extends IWirelessTermHandler with IWirelessFluidTermHandler with IWirelessGasTermHandler{
  override def getConfigManager(is: ItemStack): IConfigManager = ItemWirelessTerminalUniversalScala.getConfigManager(is)

  override def canHandle(is: ItemStack): Boolean = ItemWirelessTerminalUniversalScala.canHandle(is)

  override def usePower(player: EntityPlayer, amount: Double, is: ItemStack): Boolean = ItemWirelessTerminalUniversalScala.usePower(player, amount, is)

  override def hasPower(player: EntityPlayer, amount: Double, is: ItemStack): Boolean = ItemWirelessTerminalUniversalScala.hasPower(player, amount, is)

  override def isItemNormalWirelessTermToo(is: ItemStack): Boolean = ItemWirelessTerminalUniversalScala.isItemNormalWirelessTermToo(is)

  override def setEncryptionKey(item: ItemStack, encKey: String, name: String): Unit = ItemWirelessTerminalUniversalScala.setEncryptionKey(item, encKey, name)

  override def getEncryptionKey(item: ItemStack): String = ItemWirelessTerminalUniversalScala.getEncryptionKey(item)
}
