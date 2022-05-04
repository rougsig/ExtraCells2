package extracells.core.proxy

import appeng.api.AEApi
import cpw.mods.fml.common.registry.GameRegistry
import extracells.core.storage.FluidCellHandler
import extracells.debug.ShowNBTCommand
import extracells.feature.block.ECBlock
import extracells.feature.block.certustank.CertusTankTileEntity
import extracells.feature.item.ECItem
import extracells.tileentity.TileEntityCraftingStorage
import net.minecraftforge.client.ClientCommandHandler

class ServerProxy : CommonProxy {
  override val isClient = false
  override val isServer = true

  override fun registerBlocks() {
    ECBlock.values()
      .forEach { GameRegistry.registerBlock(it.block, it.itemClass, it.internalName) }
  }

  override fun registerItems() {
    ECItem.values()
      .forEach { GameRegistry.registerItem(it.item, it.internalName) }
  }

  override fun registerTileEntities() {
    GameRegistry.registerTileEntity(TileEntityCraftingStorage::class.java, "tileEntityCraftingStorage")
    GameRegistry.registerTileEntity(CertusTankTileEntity::class.java, "tileEntityCertusTank")
  }

  override fun registerAppengIntegration() {
    AEApi.instance().registries().cell().addCellHandler(FluidCellHandler())
  }

  override fun registerDebugTools() {
    ClientCommandHandler.instance.registerCommand(ShowNBTCommand())
  }

  override fun registerRenderers() {
  }

  override fun registerTextures() {
  }

  override fun registerGuiHandler() {
  }
}