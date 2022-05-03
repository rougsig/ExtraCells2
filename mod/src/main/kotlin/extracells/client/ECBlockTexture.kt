package extracells.client

import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.util.IIcon

internal enum class ECBlockTexture(
  private val textureName: String,
) {
  Missing("missingno"),

  BlockCraftingStorage256k("crafting.storage.256k"),
  BlockCraftingStorage1024k("crafting.storage.1024k"),
  BlockCraftingStorage4096k("crafting.storage.4096k"),
  BlockCraftingStorage16384k("crafting.storage.16384k"),

  BlockCraftingStorage256kFit("crafting.storage.256k.fit"),
  BlockCraftingStorage1024kFit("crafting.storage.1024k.fit"),
  BlockCraftingStorage4096kFit("crafting.storage.4096k.fit"),
  BlockCraftingStorage16384kFit("crafting.storage.16384k.fit"),
  ;

  lateinit var icon: IIcon
    private set

  fun registerIcon(map: TextureMap) {
    this.icon = if (this == Missing) map.getAtlasSprite("missingno")
    else map.registerIcon("extracells:$textureName")
  }
}