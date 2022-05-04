package extracells.feature.part.core

import appeng.api.implementations.IPowerChannelState
import appeng.api.implementations.IUpgradeableHost
import appeng.api.networking.IGridHost
import appeng.api.networking.security.IActionHost
import appeng.api.parts.IPart
import net.minecraft.item.ItemStack
import net.minecraftforge.common.util.ForgeDirection

internal abstract class ECPartBase(
  private val itemStack: ItemStack,
) : IPart, IGridHost, IActionHost, IUpgradeableHost, IPowerChannelState {
  var side: ForgeDirection? = null
    private set

  var grid: ECGridBlock? = null
    private set
}