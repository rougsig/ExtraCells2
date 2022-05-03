package extracells.feature.item.storage

import appeng.api.implementations.items.IStorageComponent
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.IIcon

internal class StorageComponentItem : Item(), IStorageComponent {

  init {
    this.hasSubtypes = true
    this.maxDamage = 0
  }

  override fun getBytes(itemStack: ItemStack): Int {
    return StorageComponentType.findPartByMeta(itemStack.itemDamage).size
  }

  override fun isStorageComponent(itemStack: ItemStack): Boolean {
    return itemStack.item === this
  }

  override fun getIconFromDamage(meta: Int): IIcon {
    return StorageComponentType.findPartByMeta(meta).icon
  }

  override fun getSubItems(item: Item, creativeTab: CreativeTabs?, itemList: MutableList<Any?>) {
    StorageComponentType.values()
      .forEach { part -> itemList.add(ItemStack(item, 1, part.meta)) }
  }

  override fun getUnlocalizedName(itemStack: ItemStack): String {
    return StorageComponentType.findPartByMeta(itemStack.itemDamage).unlocalizedName
  }
}