package extracells

import extracells.proxy.CommonProxy
import extracells.registries.ItemEnum
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.ItemStack

object ExtracellsLegacy {
  val ModTab = new CreativeTabs("Extra_Cells") {
    override def getIconItemStack = new ItemStack(ItemEnum.FLUIDSTORAGE.getItem)

    override def getTabIconItem = ItemEnum.FLUIDSTORAGE.getItem
  }
  var proxy: CommonProxy = null

  var VERSION = ""

  var bcBurnTimeMultiplicator = 4

  var shortenedBuckets = true
  var dynamicTypes = true

  def THIS = this

  def setProxy(proxy: CommonProxy): Unit = {
    this.proxy = proxy
  }
}
