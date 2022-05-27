package extracells.network

import cpw.mods.fml.common.network.internal.FMLProxyPacket
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled

internal abstract class ECPacket(
  val type: ECPacketType,
) {
  companion object {
    fun createFrom(data: ByteBuf): ECPacket {
      val type = ECPacketType.values()[data.readInt()]
      val packet = type.clazz.newInstance()
      packet.readPayload(data)
      return packet
    }
  }

  protected abstract fun ByteBuf.writePayload()
  abstract fun readPayload(data: ByteBuf)

  fun createProxy(): FMLProxyPacket {
    val data = Unpooled.buffer()
    data.writeInt(type.ordinal)
    data.writePayload()
    return FMLProxyPacket(data, ECNetworkHandler.CHANNEL_NAME)
  }
}
