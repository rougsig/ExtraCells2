package extracells.feature.block.craftingstorage

import appeng.block.crafting.BlockCraftingStorage
import extracells.client.ECBlockTexture
import extracells.tileentity.TileEntityCraftingStorage
import net.minecraft.item.ItemStack
import net.minecraft.util.IIcon

internal class CraftingStorageBlock : BlockCraftingStorage() {
  init {
    this.hasSubtypes = true
    this.setTileEntity(TileEntityCraftingStorage::class.java)
  }

  override fun getUnlocalizedName(itemStack: ItemStack): String? {
    return when (itemStack.itemDamage) {
      0 -> "tile.extracells.block.craftingstorage.256k"
      1 -> "tile.extracells.block.craftingstorage.1024k"
      2 -> "tile.extracells.block.craftingstorage.4096k"
      3 -> "tile.extracells.block.craftingstorage.16384k"
      else -> this.getItemUnlocalizedName(itemStack)
    }
  }

  override fun getIcon(side: Int, meta: Int): IIcon? {
    return when (meta and 4.inv()) {
      0 -> ECBlockTexture.BlockCraftingStorage256k.icon
      1 -> ECBlockTexture.BlockCraftingStorage1024k.icon
      2 -> ECBlockTexture.BlockCraftingStorage4096k.icon
      3 -> ECBlockTexture.BlockCraftingStorage16384k.icon
      0 or 8 -> ECBlockTexture.BlockCraftingStorage256kFit.icon
      1 or 8 -> ECBlockTexture.BlockCraftingStorage1024kFit.icon
      2 or 8 -> ECBlockTexture.BlockCraftingStorage4096kFit.icon
      3 or 8 -> ECBlockTexture.BlockCraftingStorage16384kFit.icon
      else -> null
    }
  }
}