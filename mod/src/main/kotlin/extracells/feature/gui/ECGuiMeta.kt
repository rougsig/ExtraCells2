package extracells.feature.gui

import net.minecraftforge.common.util.ForgeDirection

internal data class ECGuiMeta(
  val gui: ECGui,
  val side: ForgeDirection,
  val isTile: Boolean,
) {
  fun encode(): Int {
    var source = 0

    source += gui.id shl 5
    source += side.ordinal shl 1
    source += if (isTile) 1 else 0

    return source
  }

  companion object {
    fun decode(source: Int): ECGuiMeta {
      val meta = source shr 5
      val side = source shr 1 and 1
      val isTile = (source and 1) == 1

      return ECGuiMeta(
        gui = ECGui.values()[meta],
        side = ForgeDirection.values()[side],
        isTile = isTile,
      )
    }
  }
}
