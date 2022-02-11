package extracells.block;

import appeng.api.AEApi;
import appeng.api.config.SecurityPermissions;
import appeng.api.implementations.items.IAEWrench;
import appeng.api.networking.IGridNode;
import buildcraft.api.tools.IToolWrench;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import extracells.container.ContainerHardMEDrive;
import extracells.gui.GuiHardMEDrive;
import extracells.network.GuiHandler;
import extracells.render.block.RendererHardMEDrive;
import extracells.tileentity.TileEntityHardMeDrive;
import extracells.util.PermissionUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

public class BlockHardMEDrive extends BlockEC implements TGuiBlock {

    IIcon frontIcon = null;
    IIcon sideIcon = null;
    IIcon bottomIcon = null;
    IIcon topIcon = null;

    public static BlockHardMEDrive instance() {
        return new BlockHardMEDrive();
    }

    private BlockHardMEDrive() {
        super(Material.rock, 2.0F, 1000000.0F);
        setBlockName("block.hardmedrive");
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileEntityHardMeDrive();
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        dropItems(world, x, y, z);
        super.breakBlock(world, x, y, z, block, meta);
    }

    private void dropItems(World world, int x, int y, int z) {
        Random rand = new Random();
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof TileEntityHardMeDrive) {
            IInventory inventory = ((TileEntityHardMeDrive) tileEntity).getInventory();
            int i = 0;
            while (i < inventory.getSizeInventory()) {
                ItemStack item = inventory.getStackInSlot(i);
                if (item != null && item.stackSize > 0) {
                    float rx = rand.nextFloat() * 0.8F + 0.1F;
                    float ry = rand.nextFloat() * 0.8F + 0.1F;
                    float rz = rand.nextFloat() * 0.8F + 0.1F;

                    EntityItem entityItem = new EntityItem(world, x + rx, y + ry, z + rz, item.copy());

                    if (item.hasTagCompound()) {
                        entityItem.getEntityItem().setTagCompound((NBTTagCompound) item.getTagCompound().copy());
                    }
                    float factor = 0.05F;
                    entityItem.motionX = rand.nextGaussian() * factor;
                    entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
                    entityItem.motionZ = rand.nextGaussian() * factor;
                    world.spawnEntityInWorld(entityItem);
                    item.stackSize = 0;
                }
                i++;
            }
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z,
                                    EntityPlayer player, int side, float p_149727_7_,
                                    float p_149727_8_, float p_149727_9_) {
        if (world.isRemote) return false;
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileEntityHardMeDrive) {
            if (!PermissionUtil.hasPermission(player, SecurityPermissions.BUILD,
                    ((TileEntityHardMeDrive) tile).getGridNode(ForgeDirection.UNKNOWN))) {
                return false;
            }
            ItemStack current = player.inventory.getCurrentItem();
            if (player.isSneaking() && current != null) {
                try {
                    if (current.getItem() instanceof IToolWrench && ((IToolWrench) current.getItem()).canWrench(player, x, y, z)) {
                        dropBlockAsItem(world, x, y, z, new ItemStack(this));
                        world.setBlockToAir(x, y, z);
                        ((IToolWrench) current.getItem()).wrenchUsed(player, x, y, z);
                        return true;
                    }
                } catch (Exception e) {
                    if (current.getItem() instanceof IAEWrench && ((IAEWrench) current.getItem()).canWrench(current, player, x, y, z)) {
                        dropBlockAsItem(world, x, y, z, new ItemStack(this));
                        world.setBlockToAir(x, y, z);
                        return true;
                    }
                }
            }
        }
        GuiHandler.launchGui(0, player, world, x, y, z);
        return true;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
        super.onBlockPlacedBy(world, x, y, z, entity, stack);
        int l = MathHelper.floor_double(entity.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
        if (!entity.isSneaking()) {
            if (l == 0) {
                world.setBlockMetadataWithNotify(x, y, z, 2, 2);
            }
            if (l == 1) {
                world.setBlockMetadataWithNotify(x, y, z, 5, 2);
            }
            if (l == 2) {
                world.setBlockMetadataWithNotify(x, y, z, 3, 2);
            }
            if (l == 3) {
                world.setBlockMetadataWithNotify(x, y, z, 4, 2);
            }
        } else {
            if (l == 0) {
                world.setBlockMetadataWithNotify(x, y, z, ForgeDirection.getOrientation(2).getOpposite().ordinal(), 2);
            }
            if (l == 1) {
                world.setBlockMetadataWithNotify(x, y, z, ForgeDirection.getOrientation(5).getOpposite().ordinal(), 2);
            }
            if (l == 2) {
                world.setBlockMetadataWithNotify(x, y, z, ForgeDirection.getOrientation(3).getOpposite().ordinal(), 2);
            }
            if (l == 3) {
                world.setBlockMetadataWithNotify(x, y, z, ForgeDirection.getOrientation(4).getOpposite().ordinal(), 2);
            }
        }
        if (world.isRemote) return;
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile != null) {
            if (tile instanceof TileEntityHardMeDrive) {
                IGridNode node = ((TileEntityHardMeDrive) tile).getGridNode(ForgeDirection.UNKNOWN);
                if (entity instanceof EntityPlayer) {
                    EntityPlayer player = (EntityPlayer) entity;
                    node.setPlayerID(AEApi.instance().registries().players().getID(player));
                }
                node.updateState();
            }
        }
    }

    @Override
    public void onBlockPreDestroy(World world, int x, int y, int z, int meta) {
        if (world.isRemote) return;
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile != null) {
            if (tile instanceof TileEntityHardMeDrive) {
                IGridNode node = ((TileEntityHardMeDrive) tile).getGridNode(ForgeDirection.UNKNOWN);
                if (node != null) {
                    node.destroy();
                }
            }
        }
    }

    @Override
    public IIcon getIcon(int side, int metadata) {
        if (side == metadata) {
            return frontIcon;
        } else if (side == 0) {
            return bottomIcon;
        } else if (side == 1) {
            return topIcon;
        } else {
            return sideIcon;
        }
    }

    @Override
    public void registerBlockIcons(IIconRegister register) {
        frontIcon = register.registerIcon("extracells:hardmedrive.face");
        sideIcon = register.registerIcon("extracells:hardmedrive.side");
        bottomIcon = register.registerIcon("extracells:machine.bottom");
        topIcon = register.registerIcon("extracells:machine.top");
    }

    @Override
    public int getRenderType() {
        return RendererHardMEDrive.renderID;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Object getClientGuiElement(EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile == null || player == null) return null;
        if (tile instanceof TileEntityHardMeDrive) {
            return new GuiHardMEDrive(player.inventory, (TileEntityHardMeDrive) tile);
        }
        return null;
    }

    @Override
    public Object getServerGuiElement(EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile == null || player == null) return null;
        if (tile instanceof TileEntityHardMeDrive) {
            return new ContainerHardMEDrive(player.inventory, (TileEntityHardMeDrive) tile);
        }
        return null;
    }
}