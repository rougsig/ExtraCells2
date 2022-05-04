package extracells.core.proxy

interface CommonProxy {
  val isClient: Boolean
  val isServer: Boolean

  fun registerAppengIntegration()
  fun registerBlocks()
  fun registerItems()
  fun registerTileEntities()

  fun registerRenderers()
  fun registerTextures()
  fun registerGuiHandler()

  fun registerDebugTools()
}