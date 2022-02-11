package extracells.gridblock;

import appeng.api.networking.*;
import appeng.api.util.AEColor;
import appeng.api.util.DimensionalCoord;
import extracells.tileentity.TileEntityVibrationChamberFluid;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.EnumSet;

public class ECGridBlockVibrantChamber implements IGridBlock {

    private final TileEntityVibrationChamberFluid host;
    private IGrid grid;
    private int usedChannels;

    public ECGridBlockVibrantChamber(TileEntityVibrationChamberFluid host) {
        this.host = host;
    }

    @Override
    public EnumSet<ForgeDirection> getConnectableSides() {
        return EnumSet.of(
                ForgeDirection.DOWN,
                ForgeDirection.UP,
                ForgeDirection.NORTH,
                ForgeDirection.EAST,
                ForgeDirection.SOUTH,
                ForgeDirection.WEST
        );
    }

    @Override
    public EnumSet<GridFlags> getFlags() {
        return EnumSet.noneOf(GridFlags.class);
    }

    @Override
    public AEColor getGridColor() {
        return AEColor.Transparent;
    }

    @Override
    public double getIdlePowerUsage() {
        return this.host.getPowerUsage();
    }

    @Override
    public DimensionalCoord getLocation() {
        return this.host.getLocation();
    }

    @Override
    public IGridHost getMachine() {
        return this.host;
    }

    @Override
    public ItemStack getMachineRepresentation() {
        DimensionalCoord loc = this.getLocation();
        if (loc == null) return null;
        return new ItemStack(
                loc.getWorld().getBlock(loc.x, loc.y, loc.z),
                1,
                loc.getWorld().getBlockMetadata(loc.x, loc.y, loc.z)
        );
    }

    @Override
    public void gridChanged() {
        // no-op
    }

    @Override
    public boolean isWorldAccessible() {
        return true;
    }

    @Override
    public void onGridNotification(GridNotification gridNotification) {
        // no-op
    }

    @Override
    public void setNetworkStatus(IGrid iGrid, int i) {
        this.grid = iGrid;
        this.usedChannels = i;
    }
}