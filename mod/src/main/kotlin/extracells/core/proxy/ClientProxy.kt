package extracells.core.proxy

import extracells.client.ECTextureRegister
import extracells.feature.block.certustank.CertusTankRenderHandler
import extracells.feature.item.proxyfluid.ProxyFluidItemRenderer
import net.minecraftforge.common.MinecraftForge

class ClientProxy : CommonProxy by ServerProxy() {
  override val isClient = true
  override val isServer = false

  override fun registerRenderers() {
    CertusTankRenderHandler.register()
    ProxyFluidItemRenderer.register()
  }

  override fun registerTextures() {
    MinecraftForge.EVENT_BUS.register(ECTextureRegister())
  }
}
