package extracells.network

import appeng.core.sync.AppEngPacket
import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.network.FMLEventChannel
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientCustomPacketEvent
import cpw.mods.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent
import cpw.mods.fml.common.network.NetworkRegistry
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint
import cpw.mods.fml.common.network.internal.FMLProxyPacket
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.network.NetHandlerPlayServer

internal class ECNetworkHandler private constructor() {
  companion object {
    lateinit var instance: ECNetworkHandler
      private set

    fun register() {
      this.instance = ECNetworkHandler()
    }
  }

  val channelName = "EC2"

  private val channel: FMLEventChannel

  private val clientHandler: ECClientPacketHandler
  private val serverHandler: ECClientPacketHandler

  init {
    FMLCommonHandler.instance().bus().register(this)
    this.channel = NetworkRegistry.INSTANCE.newEventDrivenChannel(channelName)
    this.channel.register(this)

    this.clientHandler = this.createClientSidePacketHandler()
    this.serverHandler = this.createServerSidePacketHandler()
  }

  private fun createClientSidePacketHandler(): ECClientPacketHandler {
    return object : ECClientPacketHandler {
      override fun onPacketData(packet: FMLProxyPacket, player: EntityPlayer?) {
        TODO("createClientSidePacketHandler::onPacketData")
      }
    }
  }

  private fun createServerSidePacketHandler(): ECClientPacketHandler {
    return object : ECClientPacketHandler {
      override fun onPacketData(packet: FMLProxyPacket, player: EntityPlayer?) {
        TODO("createServerSidePacketHandler::onPacketData")
      }
    }
  }

  @SubscribeEvent
  fun serverPacket(ev: ServerCustomPacketEvent) {
    val srv = ev.packet.handler() as NetHandlerPlayServer
    this.serverHandler.onPacketData(ev.packet, srv.playerEntity)
  }

  @SubscribeEvent
  fun clientPacket(ev: ClientCustomPacketEvent) {
    clientHandler.onPacketData(ev.packet, null)
  }

  fun sendToAll(message: AppEngPacket) {
    this.channel.sendToAll(message.proxy)
  }

  fun sendTo(message: AppEngPacket, player: EntityPlayerMP) {
    this.channel.sendTo(message.proxy, player)
  }

  fun sendToAllAround(message: AppEngPacket, point: TargetPoint) {
    this.channel.sendToAllAround(message.proxy, point)
  }

  fun sendToDimension(message: AppEngPacket, dimensionId: Int) {
    this.channel.sendToDimension(message.proxy, dimensionId)
  }

  fun sendToServer(message: AppEngPacket) {
    this.channel.sendToServer(message.proxy)
  }
}
