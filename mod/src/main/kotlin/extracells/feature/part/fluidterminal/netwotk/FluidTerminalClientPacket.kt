package extracells.feature.part.fluidterminal.netwotk

import extracells.core.entity.ECFluidStack
import extracells.network.ECPacket
import extracells.network.ECPacketType
import extracells.network.readString
import extracells.network.writeString
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
    writeInt(fluids.size)
    fluids.forEach { fluidStack ->
      writeString(fluidStack.fluidName)
      writeInt(fluidStack.amount)
    }
  }

  override fun readPayload(data: ByteBuf) {
    val fluids = mutableListOf<ECFluidStack>()

    for (i in 0 until data.readInt()) {
      fluids.add(
        ECFluidStack(
          fluidName = data.readString(),
          amount = data.readInt(),
        )
      )
    }

    this.fluids = fluids
  }
}
