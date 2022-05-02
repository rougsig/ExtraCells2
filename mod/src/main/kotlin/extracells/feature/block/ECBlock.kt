package extracells.feature.block

import extracells.ExtraCells
import extracells.feature.block.certustank.CertusTankBlock
import extracells.feature.block.certustank.CertusTankItem
import extracells.feature.block.craftingstorage.CraftingStorageBlock
import extracells.feature.block.craftingstorage.CraftingStorageItem
import extracells.feature.block.fluidassembler.FluidAssemblerBlock
import extracells.feature.block.fluidfiller.FluidFillerBlock
import extracells.feature.block.fluidinterface.FluidInterfaceBlock
import extracells.feature.block.fluidvibrantchamber.FluidVibrantChamberBlock
import extracells.feature.block.hardmedrive.HardMeDriveBlock
import extracells.feature.block.legacy.LegacyECBaseBlock
import extracells.feature.block.walrus.WalrusBlock
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
  FluidAssembler(
    "fluidcrafter",
    FluidAssemblerBlock(),
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
    this.block.setBlockName("extracells.block.$internalName")
    this.block.setCreativeTab(ExtraCells.creativeTab)
  }
}