package extracells.network;

import appeng.api.parts.IPartHost;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.data.IAEFluidStack;
import cpw.mods.fml.common.network.IGuiHandler;
import extracells.ExtracellsLegacy;
import extracells.api.IFluidInterface;
import extracells.api.IPortableFluidStorageCell;
import extracells.api.IWirelessFluidTermHandler;
import extracells.block.TGuiBlock;
import extracells.container.ContainerFluidCrafter;
import extracells.container.ContainerFluidFiller;
import extracells.container.ContainerFluidInterface;
import extracells.container.ContainerFluidStorage;
import extracells.gui.GuiFluidCrafter;
import extracells.gui.GuiFluidFiller;
import extracells.gui.GuiFluidInterface;
import extracells.gui.GuiFluidStorage;
import extracells.part.PartECBase;
import extracells.registries.BlockEnum;
import extracells.tileentity.TileEntityFluidCrafter;
import extracells.tileentity.TileEntityFluidFiller;
import extracells.tileentity.TileEntityFluidInterface;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class GuiHandler implements IGuiHandler {

    public static Object[] temp = new Object[]{};

    public static Object getGui(int ID, EntityPlayer player) {
        switch (ID) {
            case 0:
                return new GuiFluidStorage(player, "extracells.part.fluid.terminal.name");
            case 1:
                return new GuiFluidStorage(player, "extracells.part.fluid.terminal.name");
            case 3:
                return new GuiFluidStorage(player, "extracells.item.storage.fluid.portable.name");
            default:
                return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static Object getContainer(int ID, EntityPlayer player, Object[] args) {
        switch (ID) {
            case 0:
                return new ContainerFluidStorage((IMEMonitor<IAEFluidStack>) args[0], player);
            case 1:
                return new ContainerFluidStorage((IMEMonitor<IAEFluidStack>) args[0], player, (IWirelessFluidTermHandler) args[1]);
            case 3:
                return new ContainerFluidStorage((IMEMonitor<IAEFluidStack>) args[0], player, (IPortableFluidStorageCell) args[1]);
            default:
                return null;
        }
    }

    public static int getGuiId(int id) {
        return id + 6;
    }

    public static int getGuiId(PartECBase part) {
        return part.getSide().ordinal();
    }

    public static Object getPartContainer(ForgeDirection side, EntityPlayer player, World world, int x, int y, int z) {
        return ((PartECBase) ((IPartHost) world.getTileEntity(x, y, z)).getPart(side)).getServerGuiElement(player);
    }

    public static void launchGui(int ID, EntityPlayer player, Object[] args) {
        temp = args;
        player.openGui(ExtracellsLegacy.instance, ID, null, 0, 0, 0);
    }

    public static void launchGui(int ID, EntityPlayer player, World world, int x, int y, int z) {
        player.openGui(ExtracellsLegacy.instance, ID, world, x, y, z);
    }

    public static Object getPartGui(ForgeDirection side, EntityPlayer player, World world, int x, int y, int z) {
       return ((PartECBase) ((IPartHost) world.getTileEntity(x, y, z)).getPart(side)).getClientGuiElement(player);
    }

    public static Object getGuiBlockElement(EntityPlayer player, World world, int x, int y, int z) {
        if(world == null || player == null) return null;
        Block block = world.getBlock(x, y, z);
        if (block  == null) return null;
        if (block instanceof TGuiBlock) {
            return ((TGuiBlock) block).getClientGuiElement(player, world, x, y, z);
        }
        return null;
    }

    public static Object getContainerBlockElement(EntityPlayer player, World world, int x, int y, int z) {
        if(world == null || player == null) return null;
        Block block = world.getBlock(x, y, z);
        if (block  == null) return null;
        if (block instanceof TGuiBlock) {
            return ((TGuiBlock) block).getServerGuiElement(player, world, x, y, z);
        }
        return null;
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        Object con = getContainerBlockElement(player, world, x, y, z);
        if (con != null) return con;
        if (world == null) return null;
        ForgeDirection side = ForgeDirection.getOrientation(ID);
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (world.getBlock(x, y, z) == BlockEnum.FLUIDCRAFTER.getBlock()) {
            if (tileEntity instanceof TileEntityFluidCrafter) {
                return new ContainerFluidCrafter(player.inventory, ((TileEntityFluidCrafter) tileEntity).getInventory());
            }
        }
        if (world.getBlock(x, y, z) == BlockEnum.ECBASEBLOCK.getBlock()) {
            if (tileEntity instanceof TileEntityFluidInterface) {
                return new ContainerFluidInterface(player, (IFluidInterface) tileEntity);
            } else if (tileEntity instanceof TileEntityFluidFiller) {
                return new ContainerFluidFiller(player.inventory, (TileEntityFluidFiller) tileEntity);
            }
            return null;
        }
        if (side != ForgeDirection.UNKNOWN) {
            return getPartContainer(side, player, world, x, y, z);
        }
        return getContainer(ID - 6, player, temp);
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        Object con = getGuiBlockElement(player, world, x, y, z);
        if (con != null) return con;
        if (world == null) return null;
        ForgeDirection side = ForgeDirection.getOrientation(ID);
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (world.getBlock(x, y, z) == BlockEnum.FLUIDCRAFTER.getBlock()) {
            if (tileEntity instanceof TileEntityFluidCrafter) {
                return new GuiFluidCrafter(player.inventory, ((TileEntityFluidCrafter) tileEntity).getInventory());
            }
        }
        if (world.getBlock(x, y, z) == BlockEnum.ECBASEBLOCK.getBlock()) {
            if (tileEntity instanceof TileEntityFluidInterface) {
                return new GuiFluidInterface(player, (IFluidInterface) tileEntity);
            } else if (tileEntity instanceof TileEntityFluidFiller) {
                return new GuiFluidFiller(player, (TileEntityFluidFiller) tileEntity);
            }
            return null;
        }
        if (side != ForgeDirection.UNKNOWN) {
            return getPartGui(side, player, world, x, y, z);
        }
        return getGui(ID - 6, player);
    }
}
