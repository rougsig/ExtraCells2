package extracells.feature.certustank

import codechicken.core.fluid.FluidUtils
import extracells.feature.ECBaseBlock
import extracells.helper.isPlayerHoldingFluidContainer
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.IIcon
import net.minecraft.util.MovingObjectPosition
import net.minecraft.world.World

internal class CertusTankBlock : ECBaseBlock(
  material = Material.glass,
) {
  init {
    setBlockBounds(0.0625f, 0.0f, 0.0625f, 0.9375f, 1.0f, 0.9375f)
  }

  override fun createNewTileEntity(world: World, metadata: Int): TileEntity {
    return CertusTankTileEntity()
  }

  override fun onBlockActivated(
    worldObj: World,
    x: Int,
    y: Int,
    z: Int,
    player: EntityPlayer,
    blockID: Int,
    offsetX: Float,
    offsetY: Float,
    offsetZ: Float,
  ): Boolean {
    val tileEntity = worldObj.getTileEntity(x, y, z) as CertusTankTileEntity

    // TODO: Add dropItemIfWrench
    if (FluidUtils.emptyTankIntoContainer(tileEntity, player, tileEntity.fluidStack)) return true
    if (FluidUtils.fillTankWithContainer(tileEntity, player)) return true
    if (isPlayerHoldingFluidContainer(player)) return true

    return false
  }

  override fun getPickBlock(
    target: MovingObjectPosition?,
    world: World,
    x: Int,
    y: Int,
    z: Int,
    player: EntityPlayer?,
  ): ItemStack? {
    val itemStack = super.getPickBlock(target, world, x, y, z, player) ?: return null
    val tileEntity = world.getTileEntity(x, y, z) as? CertusTankTileEntity ?: return null
    val nbtTag = NBTTagCompound()
    tileEntity.writeTankToNBT(nbtTag)
    itemStack.stackTagCompound = NBTTagCompound()
    itemStack.stackTagCompound.setTag(CERTUS_TANK_NBT_TAG_KEY, nbtTag)
    return itemStack
  }

  // TODO: Rewrite.
  //  Just migrated java to kotlin code

  var breakIcon: IIcon? = null
  var topIcon: IIcon? = null
  var bottomIcon: IIcon? = null
  var sideIcon: IIcon? = null
  var sideMiddleIcon: IIcon? = null
  var sideTopIcon: IIcon? = null
  var sideBottomIcon: IIcon? = null

  override fun getIcon(side: Int, b: Int): IIcon? {
    return when (b) {
      1 -> this.sideTopIcon
      2 -> this.sideBottomIcon
      3 -> this.sideMiddleIcon
      else -> if (side == 0) this.bottomIcon else if (side == 1) this.topIcon else this.sideIcon
    }
  }

  override fun canRenderInPass(pass: Int): Boolean {
    CertusTankRenderHandler.renderPass = pass
    return true
  }

  override fun getRenderBlockPass(): Int {
    return 1
  }

  override fun getRenderType(): Int {
    return CertusTankRenderHandler.renderId
  }

  override fun isOpaqueCube(): Boolean {
    return false
  }

  override fun registerBlockIcons(iconregister: IIconRegister) {
    breakIcon = iconregister.registerIcon("extracells:certustank")
    topIcon = iconregister.registerIcon("extracells:CTankTop")
    bottomIcon = iconregister.registerIcon("extracells:CTankBottom")
    sideIcon = iconregister.registerIcon("extracells:CTankSide")
    sideMiddleIcon = iconregister.registerIcon("extracells:CTankSideMiddle")
    sideTopIcon = iconregister.registerIcon("extracells:CTankSideTop")
    sideBottomIcon = iconregister.registerIcon("extracells:CTankSideBottom")
  }

  override fun renderAsNormalBlock(): Boolean {
    return false
  }
}

const val CERTUS_TANK_CAPACITY = 32000
const val CERTUS_TANK_NBT_TAG_KEY = "tileEntity"
