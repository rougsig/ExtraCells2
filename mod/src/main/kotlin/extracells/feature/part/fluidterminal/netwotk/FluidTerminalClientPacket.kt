package extracells.feature.part.fluidterminal.netwotk

import extracells.network.ECPacket
import extracells.network.ECPacketType
import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufInputStream
import io.netty.buffer.ByteBufOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable

internal class FluidTerminalClientPacket : ECPacket(ECPacketType.FluidTerminalClient) {
  companion object {
    fun create(variant: Variant) = FluidTerminalClientPacket().apply {
      this.variant = variant
    }
  }

  sealed class Variant : Serializable {
    object Empty : Variant()

    data class UpdateStoredFluids(
      val fluids: List<Fluid>,
    ) : Variant() {
      data class Fluid(
        val name: String,
        val amount: Int,
      ) : Serializable
    }

    data class UpdateSelectedFluid(
      val fluidName: String,
    ) : Variant()
  }

  var variant: Variant = Variant.Empty
    private set

  override fun ByteBuf.writePayload() {
    val bout = ByteBufOutputStream(this)
    val oout = ObjectOutputStream(bout)
    oout.writeObject(variant)
    oout.flush()
    oout.close()
  }

  override fun readPayload(data: ByteBuf) {
    val bin = ByteBufInputStream(data)
    val oin = ObjectInputStream(bin)
    this.variant = oin.readObject() as Variant
    oin.close()
    bin.close()
  }
}
