package extracells.feature.part.core

import appeng.api.networking.*
import appeng.api.networking.energy.IEnergyGrid
import appeng.api.networking.security.ISecurityGrid
import appeng.api.networking.storage.IStorageGrid
import appeng.api.parts.PartItemStack
import appeng.api.util.AEColor
import appeng.api.util.DimensionalCoord
import extracells.core.Provider
import net.minecraft.item.ItemStack
import net.minecraftforge.common.util.ForgeDirection
import java.util.*

internal class ECGridBlock(
  private val partProvider: Provider<ECPartBase>,
) : IGridBlock {
  override fun getConnectableSides(): EnumSet<ForgeDirection> {
    return EnumSet.noneOf(ForgeDirection::class.java)
  }

  private val part: ECPartBase
    get() = this.partProvider.get()

  val grid: IGrid?
    get() = this.part.gridNode?.grid

  val energyGrid: IEnergyGrid?
    get() = this.grid?.getCache(IEnergyGrid::class.java)

  val fluidMonitor: ECFluidMonitor?
    get() = this.grid?.getCache(ECFluidGrid::class.java)

  val securityGrid: ISecurityGrid?
    get() = this.grid?.getCache(ISecurityGrid::class.java)

  val storageGrid: IStorageGrid?
    get() = this.grid?.getCache(IStorageGrid::class.java)

  override fun getFlags(): EnumSet<GridFlags> {
    return EnumSet.of(GridFlags.REQUIRE_CHANNEL)
  }

  override fun getGridColor(): AEColor {
    return AEColor.Transparent
  }

  override fun getIdlePowerUsage(): Double {
    return this.part.idlePowerUsage
  }

  override fun getLocation(): DimensionalCoord {
    return this.part.location
  }

  override fun getMachine(): IGridHost {
    return this.part
  }

  override fun getMachineRepresentation(): ItemStack {
    return this.part.getItemStack(PartItemStack.Network)
  }

  override fun isWorldAccessible(): Boolean {
    return false
  }

  override fun gridChanged() {
    // no-op
  }

  override fun onGridNotification(notification: GridNotification) {
    // no-op
  }

  override fun setNetworkStatus(grid: IGrid, usedChannels: Int) {
    // no-op
  }
}
