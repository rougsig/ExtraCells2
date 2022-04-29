package extracells.feature.certustank

import extracells.ExtraCells
import extracells.feature.ECBaseBlock
import extracells.helper.fillContainerFromHandler
import extracells.helper.fillHandlerWithContainer
import extracells.helper.isPlayerHoldingFluidContainer
import net.minecraft.block.material.Material
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.MovingObjectPosition
import net.minecraft.world.World

internal class CertusTankBlock : ECBaseBlock(
  modTab = ExtraCells.creativeTab,
  internalName = "certustank",
  material = Material.glass,
) {
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

    if (fillContainerFromHandler(worldObj, tileEntity, player, tileEntity.fluidStack)) return true
    if (fillHandlerWithContainer(worldObj, tileEntity, player)) return true
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
}

const val CERTUS_TANK_CAPACITY = 32000
const val CERTUS_TANK_NBT_TAG_KEY = "tileEntity"
