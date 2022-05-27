package extracells.feature.part.fluidterminal.netwotk

import extracells.core.entity.ECFluidStack
import extracells.network.ECPacket
import extracells.network.ECPacketType
import io.netty.buffer.ByteBuf

internal class FluidTerminalClientPacket : ECPacket(ECPacketType.FluidTerminalClient) {
  companion object {
    fun create(fluids: List<ECFluidStack>): FluidTerminalClientPacket {
      return FluidTerminalClientPacket().apply {
        this.fluids = fluids
      }
    }
  }

  var fluids: List<ECFluidStack> = emptyList()
    private set

  override fun ByteBuf.writePayload() {
  }

  override fun readPayload(data: ByteBuf) {
  }
}
