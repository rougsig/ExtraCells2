package extracells.feature.part

import appeng.parts.AEBasePart
import extracells.feature.item.ECItem
import net.minecraft.item.ItemStack

internal abstract class ECPartBase(id: Int) : AEBasePart(ItemStack(ECItem.Part.item, 1, id))