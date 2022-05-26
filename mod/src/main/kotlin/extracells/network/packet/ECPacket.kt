package extracells.network.packet

import cpw.mods.fml.common.network.internal.FMLProxyPacket
import extracells.network.ECNetworkHandler
import io.netty.buffer.ByteBuf

internal abstract class ECPacket {
  abstract val type: ECPacketType

  protected abstract fun createPayload(): ByteBuf

  fun createProxy(): FMLProxyPacket {
    return FMLProxyPacket(createPayload(), ECNetworkHandler.CHANNEL_NAME)
  }
}
