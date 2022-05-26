package extracells.feature.gui

import net.minecraftforge.common.util.ForgeDirection

internal data class ECGuiMeta(
  val gui: ECGui,
  val side: ForgeDirection,
  val isTile: Boolean,
) {
  fun encode(): Int {
    var encoded = 0

    encoded += if (isTile) 1 else 0
    encoded += side.ordinal shl 1
    encoded += gui.ordinal shl 4

    return encoded
  }

  companion object {
    fun decode(encoded: Int): ECGuiMeta {
      val gui = encoded shr 4 and 127
      val side = encoded shr 1 and 7
      val isTile = encoded and 1

      return ECGuiMeta(
        gui = ECGui.values()[gui],
        side = ForgeDirection.values()[side],
        isTile = isTile == 1,
      )
    }
  }
}
