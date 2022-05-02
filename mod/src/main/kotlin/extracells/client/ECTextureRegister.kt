package extracells.client

import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.client.event.TextureStitchEvent

internal class ECTextureRegister {
  @SubscribeEvent
  fun handlePreTextureStitchEvent(ev: TextureStitchEvent.Pre) {
    // if (ev.map.textureType == 1) {
    //   for (et in ExtraCellsItemTexture.values()) {
    //     et.registerIcon(ev.map)
    //   }
    // }
    if (ev.map.textureType == 0) {
      for (et in ECBlockTexture.values()) {
        et.registerIcon(ev.map)
      }
    }
  }
}