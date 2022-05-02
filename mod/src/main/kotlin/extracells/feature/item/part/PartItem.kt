package extracells.feature.item.part

import appeng.api.AEApi
import appeng.api.parts.IPart
import appeng.api.parts.IPartItem
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import extracells.feature.part.ECPart
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.world.World

internal class PartItem : Item(), IPartItem {

  init {
    maxDamage = 0
    hasSubtypes = true
    AEApi.instance().partHelper().setItemBusRenderer(this)
  }

  override fun createPartFromItemStack(itemStack: ItemStack): IPart? {
    val clazz = ECPart.findPartById(itemStack.itemDamage).partClass
    val instance = clazz.newInstance()
    // TODO: instance.initialize(itemStack)
    return instance
  }

  override fun getSubItems(item: Item, creativeTab: CreativeTabs?, itemList: MutableList<Any?>) {
    ECPart.values()
      .forEach { part -> itemList.add(ItemStack(item, 1, part.id)) }
  }

  @SideOnly(Side.CLIENT)
  override fun getSpriteNumber(): Int {
    return 0
  }

  override fun onItemUse(
    `is`: ItemStack?, player: EntityPlayer?, world: World?,
    x: Int, y: Int, z: Int, side: Int, hitX: Float, hitY: Float, hitZ: Float
  ): Boolean {
    return AEApi.instance().partHelper().placeBus(`is`, x, y, z, side, player, world)
  }
}