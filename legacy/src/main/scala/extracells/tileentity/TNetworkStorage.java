package extracells.tileentity;

import appeng.api.networking.IGrid;
import appeng.api.networking.IGridHost;
import appeng.api.networking.storage.IStorageGrid;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.data.IAEFluidStack;
import appeng.api.storage.data.IAEItemStack;
import net.minecraftforge.common.util.ForgeDirection;

public interface TNetworkStorage {

    default IStorageGrid getStorageGrid(ForgeDirection side) {
        if (!(this instanceof IGridHost)) return null;
        IGridHost host = (IGridHost) this;
        if (host.getGridNode(side) == null) return null;
        IGrid grid = host.getGridNode(side).getGrid();
        if (grid == null) return null;
        return grid.getCache(IStorageGrid.class);
    }

    default IMEMonitor<IAEFluidStack> getFluidInventory(ForgeDirection side) {
        if (getStorageGrid(side) == null) return null;
        return getStorageGrid(side).getFluidInventory();
    }

    default IMEMonitor<IAEItemStack> getItemInventory(ForgeDirection side) {
        if (getStorageGrid(side) == null) return null;
        return getStorageGrid(side).getItemInventory();
    }
}