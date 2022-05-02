package extracells.feature.part

import appeng.parts.automation.PartUpgradeable
import extracells.feature.item.ECItem
import net.minecraft.item.ItemStack

internal abstract class ECPartUpgradeable(id: Int) : PartUpgradeable(ItemStack(ECItem.Part.item, 1, id))