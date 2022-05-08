package extracells.feature.part.core

import appeng.api.config.Upgrades
import appeng.api.implementations.IPowerChannelState
import appeng.api.implementations.IUpgradeableHost
import appeng.api.networking.IGridHost
import appeng.api.networking.IGridNode
import appeng.api.networking.security.IActionHost
import appeng.api.parts.*
import appeng.api.util.AECableType
import appeng.api.util.IConfigManager
import appeng.me.GridAccessException
import appeng.util.Platform
import extracells.feature.item.ECItem
import extracells.feature.part.ECPart
import extracells.helper.EffectiveSide
import io.netty.buffer.ByteBuf
import net.minecraft.client.renderer.RenderBlocks
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.IIcon
import net.minecraft.util.Vec3
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection
import java.util.*

internal abstract class ECPartBase(
  private val itemStack: ItemStack,
) : IPart, IGridHost, IActionHost, IUpgradeableHost, IPowerChannelState {
  constructor(part: ECPart) : this(ItemStack(ECItem.Part.item, 1, part.meta))

  private var _grid: ECGridBlock? = null
  private var _host: IPartHost? = null
  private var _node: IGridNode? = null

  private var _tile: TileEntity? = null

  private var _side = ForgeDirection.UNKNOWN

  private var _isPowered = false
  private var _isActive = false

  // region IPart
  // region IPart render
  override fun getBoxes(boxes: IPartCollisionHelper) {
    // no-op
  }

  override fun renderInventory(rh: IPartRenderHelper, renderer: RenderBlocks) {
    // no-op
  }

  override fun renderStatic(x: Int, y: Int, z: Int, rh: IPartRenderHelper, renderer: RenderBlocks) {
    // no-op
  }

  override fun getBreakingTexture(): IIcon {
    TODO("Not yet implemented")
  }

  override fun requireDynamicRender(): Boolean {
    return false
  }

  override fun renderDynamic(x: Double, y: Double, z: Double, rh: IPartRenderHelper, renderer: RenderBlocks) {
    error("renderDynamic should be never invoked")
  }

  override fun getLightLevel(): Int {
    return 0
  }
  // endregion IPart render

  // region IPart redstone
  override fun canConnectRedstone(): Boolean {
    return false
  }

  override fun isProvidingStrongPower(): Int {
    return 0
  }

  override fun isProvidingWeakPower(): Int {
    return 0
  }
  // endregion IPart redstone

  // region IPart config
  override fun isSolid(): Boolean {
    return false
  }

  override fun canBePlacedOn(what: BusSupport?): Boolean {
    return what == BusSupport.CABLE
  }

  override fun isLadder(entity: EntityLivingBase?): Boolean {
    return false
  }
  // endregion IPart config

  // region IPart network
  override fun writeToNBT(data: NBTTagCompound?) {
    return this.writeToNBT(data, PartItemStack.World)
  }

  private fun writeToNBT(data: NBTTagCompound?, type: PartItemStack) {
    // no-op
  }

  override fun readFromNBT(data: NBTTagCompound?) {
    // no-op
  }

  override fun writeToStream(data: ByteBuf?) {
    // no-op
  }

  override fun readFromStream(data: ByteBuf?): Boolean {
    TODO("Not yet implemented")
  }
  // endregion IPart network

  // region IPart getters
  override fun getGridNode(): IGridNode? {
    return this._node
  }

  override fun getDrops(drops: MutableList<ItemStack>?, wrenched: Boolean) {
    // no-op
  }

  override fun getItemStack(type: PartItemStack?): ItemStack {
    val itemStack = this.itemStack.copy()

    if (type == PartItemStack.Wrench) {
      val tag = NBTTagCompound()
      writeToNBT(tag, PartItemStack.Wrench)
      if (!tag.hasNoTags()) {
        itemStack.tagCompound = tag
      }
    }

    return itemStack
  }

  override fun getExternalFacingNode(): IGridNode? {
    return null
  }
  // endregion IPart getters

  override fun onActivate(player: EntityPlayer?, pos: Vec3?): Boolean {
    return false
  }

  override fun onShiftActivate(player: EntityPlayer?, pos: Vec3?): Boolean {
    return false
  }

  override fun onNeighborChanged() {
    // no-op
  }

  override fun onEntityCollision(entity: Entity) {
    // no-op
  }

  override fun removeFromWorld() {
    // no-op
  }

  override fun addToWorld() {
    // no-op
  }

  override fun setPartHostInfo(side: ForgeDirection?, host: IPartHost?, tile: TileEntity?) {
    // no-op
  }

  override fun randomDisplayTick(world: World?, x: Int, y: Int, z: Int, r: Random?) {
    // no-op
  }
  // endregion IPart

  // region IGridHost
  override fun getGridNode(dir: ForgeDirection?): IGridNode? {
    return this._node
  }

  override fun getCableConnectionType(dir: ForgeDirection?): AECableType {
    return AECableType.SMART
  }

  override fun securityBreak() {
    val tile = this._tile ?: error("securityBreak can't be processed _tile is null")
    val host = this._host ?: error("securityBreak can't be processed _host is null")

    val drops = mutableListOf<ItemStack>()

    drops.add(this.getItemStack(PartItemStack.Break))

    this.getDrops(drops, false)

    Platform.spawnDrops(
      tile.worldObj,
      tile.xCoord,
      tile.yCoord,
      tile.zCoord,
      drops,
    )

    host.removePart(this._side, false)
  }
  // endregion IGridHost

  // region IActionHost
  override fun getActionableNode(): IGridNode? {
    return this._node
  }
  // endregion IActionHost

  // region IUpgradeableHost
  override fun getConfigManager(): IConfigManager? {
    return null
  }

  override fun getInventoryByName(name: String): IInventory? {
    return null
  }

  override fun getInstalledUpgrades(u: Upgrades): Int {
    return 0
  }

  override fun getTile(): TileEntity? {
    return this._tile
  }
  // endregion IUpgradeableHost

  // region IPowerChannelState
  override fun isPowered(): Boolean {
    try {
      if (EffectiveSide.isServerSide && this._grid != null) {
        this._isPowered = this._grid?.energyGrid?.isNetworkPowered ?: false
      }
    } catch (ex: GridAccessException) {
      // no-op
    }

    return this._isPowered
  }

  override fun isActive(): Boolean {
    if (EffectiveSide.isServerSide) {
      this._isActive = this._node?.isActive ?: false
    }

    return this._isActive
  }
  // endregion IPowerChannelState
}
