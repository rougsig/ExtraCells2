package extracells.feature.certustank

import extracells.core.storage.FluidTank
import extracells.tileentity.TileEntityCertusTank
import net.minecraft.block.Block
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.world.World
import net.minecraftforge.fluids.IFluidContainerItem
import net.minecraftforge.fluids.FluidStack as ForgeFluidStack

internal class CertusTankItem(block: Block) : ItemBlock(block), IFluidContainerItem {
  override fun getFluid(container: ItemStack): ForgeFluidStack? {
    val tag = container.stackTagCompound?.getCompoundTag(CERTUS_TANK_NBT_TAG_KEY) ?: return null
    return ForgeFluidStack.loadFluidStackFromNBT(tag)
  }

  override fun fill(container: ItemStack, resource: ForgeFluidStack?, doFill: Boolean): Int {
    resource ?: return 0

    val tank = FluidTank(CERTUS_TANK_CAPACITY)
    val nbtTag = container.stackTagCompound.getCompoundTag(CERTUS_TANK_NBT_TAG_KEY) ?: NBTTagCompound()

    tank.readFromNBT(nbtTag)
    val result = tank.fill(resource.getFluid().name, resource.amount, doFill)

    if (doFill) {
      tank.writeToNBT(nbtTag)
      container.stackTagCompound.setTag(CERTUS_TANK_NBT_TAG_KEY, nbtTag)
    }

    return result
  }

  override fun drain(container: ItemStack, amount: Int, doDrain: Boolean): ForgeFluidStack? {
    val tank = FluidTank(CERTUS_TANK_CAPACITY)
    val nbtTag = container.stackTagCompound.getCompoundTag(CERTUS_TANK_NBT_TAG_KEY) ?: NBTTagCompound()

    tank.readFromNBT(nbtTag)
    val result = tank.drain(fluidName = null, amount, doDrain)

    if (doDrain) {
      tank.writeToNBT(nbtTag)
      container.stackTagCompound.setTag(CERTUS_TANK_NBT_TAG_KEY, nbtTag)
    }

    return result.toForgeFluidStack()
  }

  override fun getCapacity(container: ItemStack?): Int {
    return CERTUS_TANK_CAPACITY
  }

  override fun placeBlockAt(
    stack: ItemStack?,
    player: EntityPlayer?,
    world: World,
    x: Int,
    y: Int,
    z: Int,
    side: Int,
    hitX: Float,
    hitY: Float,
    hitZ: Float,
    metadata: Int
  ): Boolean {
    val result = super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata)

    if (result && stack != null && stack.hasTagCompound()) {
      (world.getTileEntity(x, y, z) as CertusTankTileEntity)
        .readTankFromNBT(stack.tagCompound.getCompoundTag(CERTUS_TANK_NBT_TAG_KEY))
    }

    return result
  }
}
