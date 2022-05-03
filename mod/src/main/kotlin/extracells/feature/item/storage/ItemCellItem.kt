package extracells.feature.item.storage

import appeng.api.AEApi
import appeng.api.config.FuzzyMode
import appeng.api.exceptions.MissingDefinition
import appeng.api.implementations.items.IStorageCell
import appeng.api.implementations.items.IUpgradeModule
import appeng.api.storage.StorageChannel
import appeng.api.storage.data.IAEItemStack
import appeng.core.AEConfig
import appeng.core.features.AEFeature
import appeng.items.contents.CellConfig
import appeng.items.contents.CellUpgrades
import appeng.util.InventoryAdaptor
import appeng.util.Platform
import extracells.feature.item.ECItem
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.IIcon
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.event.ForgeEventFactory

internal class ItemCellItem: Item(), IStorageCell {
  init {
    this.maxStackSize = 1
    this.hasSubtypes = true
    this.maxDamage = 0
  }

  override fun getIconFromDamage(meta: Int): IIcon {
    return ItemCellType.findPartByMeta(meta).icon
  }

  override fun getSubItems(item: Item, creativeTab: CreativeTabs?, itemList: MutableList<Any?>) {
    ItemCellType.values()
      .forEach { cell -> itemList.add(ItemStack(item, 1, cell.meta)) }
  }

  override fun getUnlocalizedName(itemStack: ItemStack): String {
    return ItemCellType.findPartByMeta(itemStack.itemDamage).unlocalizedName
  }

  override fun getBytes(itemStack: ItemStack): Int {
    return ItemCellType.findPartByMeta(itemStack.itemDamage).part.size
  }

  override fun BytePerType(itemStack: ItemStack): Int {
    return ItemCellType.findPartByMeta(itemStack.itemDamage).typeSize
  }

  override fun getBytesPerType(itemStack: ItemStack): Int {
    return ItemCellType.findPartByMeta(itemStack.itemDamage).typeSize
  }

  override fun getTotalTypes(itemStack: ItemStack): Int {
    return ItemCellType.findPartByMeta(itemStack.itemDamage).maxTypes
  }

  override fun getIdleDrain(): Double {
    // TODO: implement that logic. We haven't itemDamage here.
    //  ItemCellType.findPartByMeta(itemStack.itemDamage).idleDrain
    return 0.0
  }

  override fun isBlackListed(cellItem: ItemStack?, requestedAddition: IAEItemStack?): Boolean {
    return false
  }

  override fun storableInStorageCell(): Boolean {
    return false
  }

  override fun isStorageCell(itemStack: ItemStack): Boolean {
    return true
  }

  override fun isEditable(itemStack: ItemStack): Boolean {
    return true
  }

  override fun getUpgradesInventory(itemStack: ItemStack): IInventory {
    return CellUpgrades(itemStack, 2)
  }

  override fun getConfigInventory(itemStack: ItemStack?): IInventory? {
    return CellConfig(itemStack)
  }

  override fun getFuzzyMode(itemStack: ItemStack?): FuzzyMode? {
    val fz = Platform.openNbtData(itemStack).getString("FuzzyMode")
    return try {
      FuzzyMode.valueOf(fz)
    } catch (t: Throwable) {
      FuzzyMode.IGNORE_ALL
    }
  }

  override fun setFuzzyMode(itemStack: ItemStack?, fzMode: FuzzyMode) {
    Platform.openNbtData(itemStack).setString("FuzzyMode", fzMode.name)
  }

  override fun getOreFilter(itemStack: ItemStack?): String? {
    return Platform.openNbtData(itemStack).getString("OreFilter")
  }

  override fun setOreFilter(itemStack: ItemStack?, filter: String?) {
    Platform.openNbtData(itemStack).setString("OreFilter", filter)
  }

  override fun onItemRightClick(stack: ItemStack, world: World, player: EntityPlayer): ItemStack? {
    disassembleDrive(stack, world, player)
    return stack
  }

  private fun disassembleDrive(itemStack: ItemStack, world: World, player: EntityPlayer): Boolean {
    if (player.isSneaking) {
      if (Platform.isClient()) {
        return false
      }
      val playerInventory = player.inventory
      val inv = AEApi.instance().registries().cell().getCellInventory(itemStack, null, StorageChannel.ITEMS)
      if (inv != null && playerInventory.getCurrentItem() == itemStack) {
        val ia = InventoryAdaptor.getAdaptor(player, ForgeDirection.UNKNOWN)
        val list = inv.getAvailableItems(StorageChannel.ITEMS.createList())
        if (list.isEmpty && ia != null) {
          playerInventory.setInventorySlotContents(playerInventory.currentItem, null)

          // drop core
          val part = ItemCellType.findPartByMeta(itemStack.itemDamage)
          val extraB = ia.addItems(ItemStack(ECItem.StorageComponent.item, 1, part.meta))
          if (extraB != null) {
            player.dropPlayerItemWithRandomChoice(extraB, false)
          }

          // drop upgrades
          val upgradesInventory = getUpgradesInventory(itemStack)
          for (upgradeIndex in 0 until upgradesInventory.sizeInventory) {
            val upgradeStack = upgradesInventory.getStackInSlot(upgradeIndex)
            val leftStack = ia.addItems(upgradeStack)
            if (leftStack != null && upgradeStack.item is IUpgradeModule) {
              player.dropPlayerItemWithRandomChoice(upgradeStack, false)
            }
          }

          // drop empty storage cell case
          for (storageCellStack in AEApi.instance().definitions().materials().emptyStorageCell().maybeStack(1)
            .asSet()) {
            val extraA = ia.addItems(storageCellStack)
            if (extraA != null) {
              player.dropPlayerItemWithRandomChoice(extraA, false)
            }
          }
          if (player.inventoryContainer != null) {
            player.inventoryContainer.detectAndSendChanges()
          }
          return true
        }
      }
    }
    return false
  }

  override fun onItemUseFirst(
    stack: ItemStack,
    player: EntityPlayer,
    world: World,
    x: Int,
    y: Int,
    z: Int,
    side: Int,
    hitX: Float,
    hitY: Float,
    hitZ: Float
  ): Boolean {
    return if (ForgeEventFactory.onItemUseStart(player, stack, 1) <= 0) true
    else disassembleDrive(stack, world, player)
  }

  override fun getContainerItem(itemStack: ItemStack): ItemStack {
    for (stack in AEApi.instance().definitions().materials().emptyStorageCell().maybeStack(1).asSet()) {
      return stack
    }
    throw MissingDefinition("Tried to use empty storage cells while basic storage cells are defined.")
  }

  override fun hasContainerItem(stack: ItemStack): Boolean {
    return AEConfig.instance.isFeatureEnabled(AEFeature.EnableDisassemblyCrafting)
  }
}