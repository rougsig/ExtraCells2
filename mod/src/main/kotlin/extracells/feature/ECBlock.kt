package extracells.feature

import extracells.feature.certustank.CertusTankBlock
import extracells.feature.certustank.CertusTankItem
import extracells.feature.craftingstorage.CraftingStorageBlock
import extracells.feature.fluidcrafter.FluidCrafterBlock
import extracells.feature.fluidfiller.FluidFillerBlock
import extracells.feature.fluidinterface.FluidInterfaceBlock
import extracells.feature.fluidvibrantchamber.FluidVibrantChamberBlock
import extracells.feature.hardmedrive.HardMeDriveBlock
import extracells.feature.legacy.LegacyECBaseBlock
import extracells.feature.walrus.WalrusBlock
import net.minecraft.item.ItemBlock

internal enum class ECBlock(
  val block: ECBaseBlock,
  val itemClass: Class<out ItemBlock> = ItemBlock::class.java,
) {
  CertusTank(CertusTankBlock(), CertusTankItem::class.java),
  FluidCrafter(FluidCrafterBlock()),
  FluidInterface(FluidInterfaceBlock()),
  FluidFiller(FluidFillerBlock()),
  HardMeDrive(HardMeDriveBlock()),
  FluidVibrantChamber(FluidVibrantChamberBlock()),
  CraftingStorage(CraftingStorageBlock()),

  // Legacy blocks
  LegacyECBase(LegacyECBaseBlock()),
  Walrus(WalrusBlock()),
}