package extracells.feature.part.core

import appeng.api.implementations.IPowerChannelState
import appeng.api.implementations.IUpgradeableHost
import appeng.api.networking.IGridHost
import appeng.api.networking.security.IActionHost
import appeng.api.parts.IPart
import appeng.api.util.DimensionalCoord
import extracells.helper.EffectiveSide
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.util.ForgeDirection

internal abstract class ECPartBase(
  private val itemStack: ItemStack,
) : IPart, IGridHost, IActionHost, IUpgradeableHost, IPowerChannelState {
  lateinit var side: ForgeDirection
    private set

  lateinit var grid: ECGridBlock
    private set

  private lateinit var tile: TileEntity

  val location: DimensionalCoord
    get() = DimensionalCoord(this.tile)

  abstract val idlePowerUsage: Double

  init {
    if (EffectiveSide.isServerSide) {
      this.grid = ECGridBlock(this)
    }

    // TODO: continue here
  }

  override fun getTile(): TileEntity {
    return this.tile
  }

  override fun addToWorld() {

  }
}