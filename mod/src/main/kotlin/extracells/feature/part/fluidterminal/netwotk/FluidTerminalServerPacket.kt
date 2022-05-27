package extracells.feature.part.fluidterminal.netwotk

import extracells.network.ECPacket
import extracells.network.ECPacketType
import io.netty.buffer.ByteBuf

internal class FluidTerminalServerPacket : ECPacket(ECPacketType.FluidTerminalServer) {
  companion object {
    fun create() = FluidTerminalServerPacket()
  }

  override fun ByteBuf.writePayload() {
  }

  override fun readPayload(data: ByteBuf) {
  }
}
