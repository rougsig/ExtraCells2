package extracells.network

import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.network.FMLEventChannel
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientCustomPacketEvent
import cpw.mods.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent
import cpw.mods.fml.common.network.NetworkRegistry
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint
import extracells.network.packet.ECPacket
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.network.NetHandlerPlayServer

internal class ECNetworkHandler private constructor() {
  companion object {
    const val CHANNEL_NAME = "EC2"

    lateinit var instance: ECNetworkHandler
      private set

    fun register() {
      this.instance = ECNetworkHandler()
    }
  }

  private val channel: FMLEventChannel

  init {
    FMLCommonHandler.instance().bus().register(this)
    this.channel = NetworkRegistry.INSTANCE.newEventDrivenChannel(CHANNEL_NAME)
    this.channel.register(this)
  }

  // region send funs
  fun sendToAll(message: ECPacket) {
    this.channel.sendToAll(message.createProxy())
  }

  fun sendTo(message: ECPacket, player: EntityPlayerMP) {
    this.channel.sendTo(message.createProxy(), player)
  }

  fun sendToAllAround(message: ECPacket, point: TargetPoint) {
    this.channel.sendToAllAround(message.createProxy(), point)
  }

  fun sendToDimension(message: ECPacket, dimensionId: Int) {
    this.channel.sendToDimension(message.createProxy(), dimensionId)
  }

  fun sendToServer(message: ECPacket) {
    this.channel.sendToServer(message.createProxy())
  }
  // endregion send funs

  // region handler login
  @SubscribeEvent
  fun serverPacket(ev: ServerCustomPacketEvent) {
    val server = ev.packet.handler() as NetHandlerPlayServer

    val packet = ev.packet
    val player = server.playerEntity
  }

  @SubscribeEvent
  fun clientPacket(ev: ClientCustomPacketEvent) {
    val packet = ev.packet
  }
  // endregion handler logic
}
