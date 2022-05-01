package extracells.feature.craftingstorage

import appeng.block.AEBaseItemBlock
import net.minecraft.block.Block

internal class CraftingStorageItem(block: Block) : AEBaseItemBlock(block) {
  init {
    this.hasSubtypes = true
  }
}