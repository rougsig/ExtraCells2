package extracells.client

import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.util.IIcon

internal enum class ECItemTexture(
  private val textureName: String,
) {
  Missing("missingno"),

  Cell256kPart("storage.component.physical.256k"),
  Cell1024kPart("storage.component.physical.1024k"),
  Cell4096kPart("storage.component.physical.4096k"),
  Cell16384kPart("storage.component.physical.16384k"),

  Cell256k("storage.physical.256k"),
  Cell1024k("storage.physical.1024k"),
  Cell4096k("storage.physical.4096k"),
  Cell16384k("storage.physical.16384k"),

  Cell1kFluidPart("storage.component.fluid.1k"),
  Cell4kFluidPart("storage.component.fluid.4k"),
  Cell16kFluidPart("storage.component.fluid.16k"),
  Cell64kFluidPart("storage.component.fluid.64k"),
  Cell256kFluidPart("storage.component.fluid.256k"),
  Cell1024kFluidPart("storage.component.fluid.1024k"),
  Cell4096kFluidPart("storage.component.fluid.4096k"),

  Cell1kFluid("storage.fluid.1k"),
  Cell4kFluid("storage.fluid.4k"),
  Cell16kFluid("storage.fluid.16k"),
  Cell64kFluid("storage.fluid.64k"),
  Cell256kFluid("storage.fluid.256k"),
  Cell1024kFluid("storage.fluid.1024k"),
  Cell4096kFluid("storage.fluid.4096k"),
  ;

  lateinit var icon: IIcon
    private set

  fun registerIcon(map: TextureMap) {
    this.icon = if (this == Missing) map.getAtlasSprite("missingno")
    else map.registerIcon("extracells:$textureName")
  }
}