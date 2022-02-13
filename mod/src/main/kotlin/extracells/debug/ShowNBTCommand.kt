package extracells.debug

import codechicken.nei.util.NBTJson
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraft.util.ChatComponentTranslation

internal class ShowNBTCommand : CommandBase() {
  override fun getCommandName(): String {
    return "nbt"
  }

  override fun getCommandUsage(sender: ICommandSender): String {
    return "/nbt"
  }

  override fun canCommandSenderUseCommand(p_canCommandSenderUseCommand_1_: ICommandSender?): Boolean {
    return true
  }

  override fun processCommand(sender: ICommandSender, args: Array<out String>) {
    val player = getPlayer(sender, "Developer")
    val json = NBTJson.toJson(player.heldItem.tagCompound)
    sender.addChatMessage(ChatComponentTranslation(json))
  }
}
