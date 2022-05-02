package extracells.feature

import extracells.ExtraCells
import extracells.feature.certustank.CertusTankBlock
import extracells.feature.certustank.CertusTankItem
import extracells.feature.craftingstorage.CraftingStorageBlock
import extracells.feature.craftingstorage.CraftingStorageItem
import extracells.feature.fluidcrafter.FluidCrafterBlock
import extracells.feature.fluidfiller.FluidFillerBlock
import extracells.feature.fluidinterface.FluidInterfaceBlock
import extracells.feature.fluidvibrantchamber.FluidVibrantChamberBlock
import extracells.feature.hardmedrive.HardMeDriveBlock
import extracells.feature.legacy.LegacyECBaseBlock
import extracells.feature.walrus.WalrusBlock
import net.minecraft.block.Block
import net.minecraft.item.ItemBlock

internal enum class ECBlock(
  val internalName: String,
  val block: Block,
  val itemClass: Class<out ItemBlock> = ItemBlock::class.java,
) {
  CertusTank(
    "certustank",
    CertusTankBlock(),
    CertusTankItem::class.java,
  ),
  FluidCrafter(
    "fluidcrafter",
    FluidCrafterBlock(),
  ),
  FluidInterface(
    "fluidinterface",
    FluidInterfaceBlock(),
  ),
  FluidFiller(
    "fluidfiller",
    FluidFillerBlock(),
  ),
  HardMeDrive(
    "hardmedrive",
    HardMeDriveBlock(),
  ),
  FluidVibrantChamber(
    "vibrantchamberfluid",
    FluidVibrantChamberBlock(),
  ),
  CraftingStorage(
    "craftingstorage",
    CraftingStorageBlock(),
    CraftingStorageItem::class.java,
  ),

  // Legacy blocks
  LegacyECBase(
    "ecbaseblock",
    LegacyECBaseBlock(),
  ),
  Walrus(
    "walrus",
    WalrusBlock(),
  ),
  ;

  init {
    this.block.setBlockName("extracells.block.$this.internalName")
    this.block.setCreativeTab(ExtraCells.creativeTab)
  }
}