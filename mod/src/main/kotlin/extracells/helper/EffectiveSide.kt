package extracells.helper

import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.relauncher.Side

object EffectiveSide {
  private val FCH = FMLCommonHandler.instance()

  val isClientSide: Boolean
    get() = FCH.effectiveSide.isClient

  val isServerSide: Boolean
    get() = FCH.effectiveSide.isServer

  val side: Side
    get() = FCH.effectiveSide
}
