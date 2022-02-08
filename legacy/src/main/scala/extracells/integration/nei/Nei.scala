package extracells.integration.nei

import codechicken.nei.api.API
import extracells.ExtracellsLegacy
import extracells.registries.{BlockEnum, ItemEnum}
import net.minecraft.item.{Item, ItemStack}

import java.util


object Nei {

  def init = {
    hideItems
    if (ExtracellsLegacy.proxy.isClient) {
      val handler = new UniversalTerminalRecipe
      API.registerUsageHandler(handler)
      API.registerRecipeHandler(handler)
    }

  }

  def hideItems = {
    API.hideItem(new ItemStack(ItemEnum.FLUIDITEM.getItem))
    API.hideItem(new ItemStack(ItemEnum.CRAFTINGPATTERN.getItem))
    for (item <- ItemEnum.values()) {
      if (item.getMod != null && (!item.getMod.isEnabled)) {
        val i = item.getItem
        val list = new util.ArrayList[ItemStack]
        i.getSubItems(i, ExtracellsLegacy.ModTab, list)
        val it = list.iterator
        while(it.hasNext){
          API.hideItem(it.next)
        }
      }
    }

    for (block <- BlockEnum.values()) {
      if (block.getMod != null && (!block.getMod.isEnabled)) {
        val b = block.getBlock
        val list = new util.ArrayList[ItemStack]
        b.getSubBlocks(Item.getItemFromBlock(b), ExtracellsLegacy.ModTab, list)
        val it = list.iterator
        while(it.hasNext){
          API.hideItem(it.next)
        }
      }
    }
  }

}
