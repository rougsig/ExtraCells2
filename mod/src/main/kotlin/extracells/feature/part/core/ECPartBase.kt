package extracells.feature.part.core

import appeng.api.AEApi
import appeng.api.config.Upgrades
import appeng.api.implementations.IPowerChannelState
import appeng.api.implementations.IUpgradeableHost
import appeng.api.networking.IGridHost
import appeng.api.networking.IGridNode
import appeng.api.networking.security.IActionHost
import appeng.api.networking.security.MachineSource
import appeng.api.parts.*
import appeng.api.util.AECableType
import appeng.api.util.DimensionalCoord
import appeng.api.util.IConfigManager
import appeng.me.GridAccessException
import appeng.util.Platform
import extracells.client.ECBlockTexture
import extracells.core.provider
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

  private var _side: ForgeDirection? = null

  private var _isPowered: Boolean = false
  private var _isActive: Boolean = false

  private var ownerID: Int = 0

  protected val src = MachineSource(this)

  protected val side: ForgeDirection?
    get() = this._side

  val requireSide: ForgeDirection
    get() = this.side ?: error("fluid monitor is null")

  open val idlePowerUsage: Double = 0.0

  val location: DimensionalCoord
    get() = DimensionalCoord(this._tile)

  val grid: ECGridBlock?
    get() = this._grid

  val requireFluidMonitor: ECFluidMonitor
    get() = this._grid?.fluidMonitor ?: error("fluid monitor is null")

  init {
    if (EffectiveSide.isServerSide) {
      this._grid = ECGridBlock(provider { this@ECPartBase })
    }
  }

  protected fun markForSave() {
    this._host?.markForSave()
  }

  // region IPart
  // region IPart render
  override fun cableConnectionRenderTo(): Int {
    return 0
  }

  override fun getBoxes(boxes: IPartCollisionHelper) {
    boxes.addBox(2.0, 2.0, 14.0, 14.0, 14.0, 16.0)
    boxes.addBox(4.0, 4.0, 13.0, 12.0, 12.0, 14.0)
  }

  override fun renderInventory(rh: IPartRenderHelper, renderer: RenderBlocks) {
    rh.setTexture(ECBlockTexture.Missing.icon)
    rh.setBounds(2f, 2f, 14f, 14f, 14f, 16f)
  }

  override fun renderStatic(x: Int, y: Int, z: Int, rh: IPartRenderHelper, renderer: RenderBlocks) {
    rh.setTexture(ECBlockTexture.Missing.icon)
    rh.setBounds(2f, 2f, 14f, 14f, 14f, 16f)
  }

  override fun getBreakingTexture(): IIcon? {
    return null
  }

  final override fun requireDynamicRender(): Boolean {
    return false
  }

  final override fun renderDynamic(x: Double, y: Double, z: Double, rh: IPartRenderHelper, renderer: RenderBlocks) {
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
  final override fun writeToNBT(data: NBTTagCompound?) {
    return this.writeToNBT(data, PartItemStack.World)
  }

  private fun writeToNBT(data: NBTTagCompound?, type: PartItemStack) {
    // no-op
  }

  override fun readFromNBT(data: NBTTagCompound?) {
    // no-op
  }

  override fun writeToStream(stream: ByteBuf) {
    stream.writeBoolean(this.isActive)
    stream.writeBoolean(this.isPowered)
  }

  override fun readFromStream(stream: ByteBuf): Boolean {
    val oldIsActive = this.isActive
    val oldIsPowered = this.isPowered

    this._isActive = stream.readBoolean()
    this._isPowered = stream.readBoolean()

    return oldIsActive != this._isActive || oldIsPowered != this._isPowered
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

  override fun onPlacement(player: EntityPlayer, held: ItemStack?, side: ForgeDirection?) {
    this.ownerID = AEApi.instance().registries().players().getID(player.gameProfile)
  }

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
    if (EffectiveSide.isClientSide) return
    val node = AEApi.instance().createGridNode(this._grid)
    node.playerID = this.ownerID
    this._node = node
  }

  override fun setPartHostInfo(side: ForgeDirection?, host: IPartHost?, tile: TileEntity?) {
    this._side = side
    this._host = host
    this._tile = tile
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
