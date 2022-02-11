package extracells.integration.opencomputers;

import appeng.api.AEApi;
import appeng.api.config.Actionable;
import appeng.api.implementations.tiles.IWirelessAccessPoint;
import appeng.api.networking.IGrid;
import appeng.api.networking.IGridBlock;
import appeng.api.networking.IGridHost;
import appeng.api.networking.IGridNode;
import appeng.api.networking.security.MachineSource;
import appeng.api.networking.storage.IStorageGrid;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.data.IAEFluidStack;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.util.DimensionalCoord;
import appeng.api.util.WorldCoord;
import appeng.tile.misc.TileSecurity;
import extracells.item.ItemOCUpgrade;
import li.cil.oc.api.Network;
import li.cil.oc.api.internal.*;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.*;
import li.cil.oc.api.prefab.ManagedEnvironment;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;

import java.util.Objects;

public class UpgradeAE extends ManagedEnvironment {

    public final Robot robot;
    public final Drone drone;
    public final Agent agent;
    public final EnvironmentHost host;
    public boolean isActive = false;

    public UpgradeAE(EnvironmentHost host) {
        this.robot = host instanceof Robot ? (Robot) host : null;
        this.drone = host instanceof Drone ? (Drone) host : null;
        this.agent = (Agent) host;
        this.host = host;
        setNode(Network
                .newNode(this, Visibility.Network)
                .withConnector()
                .withComponent("upgrade_me", Visibility.Neighbors)
                .create());
    }

    public ItemStack getComponent() {
        if (robot != null) {
            return robot.getStackInSlot(robot.componentSlot(node().address()));
        } else if (drone != null) {
            for (ItemStack stack : drone.internalComponents()) {
                if (stack != null && stack.getItem() instanceof ItemOCUpgrade) {
                    return stack;
                }
            }
        }
        return null;
    }

    public IGridHost getSecurity() {
        if (host.world().isRemote) return null;
        ItemStack component = getComponent();
        IGridHost sec = (IGridHost) AEApi.instance().registries().locatable().getLocatableBy(getAEKey(component));
        if (checkRange(component, sec)) {
            return sec;
        }
        return null;
    }

    public boolean checkRange(ItemStack stack, IGridHost sec) {
        if (sec == null) return false;
        IGridNode gridNode = sec.getGridNode(ForgeDirection.UNKNOWN);
        if (gridNode == null) return false;
        IGrid grid = gridNode.getGrid();
        if (grid == null) return false;
        IGridBlock gridBlock = null;
        DimensionalCoord loc = null;
        switch (stack.getItemDamage()) {
            case 0:
                return grid.getMachines((Class<? extends IGridHost>) AEApi.instance().definitions().blocks().wireless().maybeEntity().get()).iterator().hasNext();
            case 1:
                gridBlock = gridNode.getGridBlock();
                if (gridBlock == null) return false;
                loc = gridBlock.getLocation();
                if (loc == null) return false;
                for (IGridNode node : grid.getMachines((Class<? extends IGridHost>) AEApi.instance().definitions().blocks().wireless().maybeEntity().get())) {
                    IWirelessAccessPoint accessPoint = (IWirelessAccessPoint) node.getMachine();
                    WorldCoord distance = accessPoint.getLocation().subtract((int) agent.xPosition(), (int) agent.yPosition(), (int) agent.zPosition());
                    int squaredDistance = distance.x * distance.x + distance.y * distance.y + distance.z * distance.z;
                    double range = accessPoint.getRange();
                    if (squaredDistance <= range * range) return true;
                }
                return false;
            default:
                gridBlock = gridNode.getGridBlock();
                if (gridBlock == null) return false;
                loc = gridBlock.getLocation();
                if (loc == null) return false;
                for (IGridNode node : grid.getMachines((Class<? extends IGridHost>) AEApi.instance().definitions().blocks().wireless().maybeEntity().get())) {
                    IWirelessAccessPoint accessPoint = (IWirelessAccessPoint) node.getMachine();
                    WorldCoord distance = accessPoint.getLocation().subtract((int) agent.xPosition(), (int) agent.yPosition(), (int) agent.zPosition());
                    int squaredDistance = distance.x * distance.x + distance.y * distance.y + distance.z * distance.z;
                    double range = accessPoint.getRange() / 2;
                    if (squaredDistance <= range * range) return true;
                }
                return false;
        }
    }

    public IGrid getGrid() {
        if (host.world().isRemote) return null;
        IGridHost securityTerminal = getSecurity();
        if (securityTerminal == null) return null;
        IGridNode gridNode = securityTerminal.getGridNode(ForgeDirection.UNKNOWN);
        if (gridNode == null) return null;
        return gridNode.getGrid();
    }

    public long getAEKey(ItemStack stack) {
        try {
            return Long.parseLong(new WirelessHandlerUpgradeAE().getEncryptionKey(stack));
        } catch (Exception e) {
            return 0;
        }
    }

    public TileSecurity tile() {
        IGridHost sec = getSecurity();
        if (sec == null) throw new SecurityException("No Security Station");
        IGridNode node = sec.getGridNode(ForgeDirection.UNKNOWN);
        if (node == null) throw new SecurityException("No Security Station");
        IGridBlock gridBlock = node.getGridBlock();
        if (gridBlock == null) throw new SecurityException("No Security Station");
        DimensionalCoord coord = gridBlock.getLocation();
        if (coord == null) throw new SecurityException("No Security Station");
        TileSecurity tileSecurity = (TileSecurity) coord.getWorld().getTileEntity(coord.x, coord.y, coord.z);
        if (tileSecurity == null) throw new SecurityException("No Security Station");
        return tileSecurity;
    }

    public IMEMonitor<IAEFluidStack> getFluidInventory() {
        IGrid grid = getGrid();
        if (grid == null) return null;
        IStorageGrid storage = grid.getCache(IStorageGrid.class);
        if (storage == null) return null;
        return storage.getFluidInventory();
    }

    public IMEMonitor<IAEItemStack> getItemInventory() {
        IGrid grid = getGrid();
        if (grid == null) return null;
        IStorageGrid storage = grid.getCache(IStorageGrid.class);
        if (storage == null) return null;
        return storage.getItemInventory();
    }

    @Callback(doc = "function([number:amount]):number -- Transfer selected items to your ae system.")
    public Object[] sendItems(Context context, Arguments args) {
        int selected = agent.selectedSlot();
        IInventory invRobot = agent.mainInventory();
        if (invRobot.getSizeInventory() <= 0) return new Object[]{0};
        ItemStack stack = invRobot.getStackInSlot(selected);
        IMEMonitor<IAEItemStack> inv = getItemInventory();
        if (stack == null || inv == null) return new Object[]{0};
        int amount = Math.min(args.optInteger(0, 64), stack.stackSize);
        ItemStack stack2 = stack.copy();
        stack2.stackSize = amount;
        IAEItemStack notInjected = inv.injectItems(AEApi.instance().storage().createItemStack(stack2),
                Actionable.MODULATE, new MachineSource(tile()));
        if (notInjected == null) {
            stack.stackSize -= amount;
            if (stack.stackSize <= 0) {
                invRobot.setInventorySlotContents(selected, null);
            } else {
                invRobot.setInventorySlotContents(selected, stack);
            }
            return new Object[]{amount};
        } else {
            stack.stackSize = (int) (stack.stackSize - amount + notInjected.getStackSize());
            if (stack.stackSize <= 0)
                invRobot.setInventorySlotContents(selected, null);
            else
                invRobot.setInventorySlotContents(selected, stack);
            return new Object[]{stack2.stackSize - notInjected.getStackSize()};
        }
    }

    @Callback(doc = "function(database:address, entry:number[, number:amount]):number -- Get items from your ae system.")
    public Object[] requestItems(Context context, Arguments args) {
        String address = args.checkString(0);
        int entry = args.checkInteger(1);
        int amount = args.optInteger(2, 64);
        int selected = agent.selectedSlot();
        IInventory invRobot = agent.mainInventory();
        if (invRobot.getSizeInventory() <= 0) return new Object[]{0};
        IMEMonitor<IAEItemStack> inv = getItemInventory();
        if (inv == null) return new Object[]{amount};
        Node n = node().network().node(address);
        if (n == null) throw new IllegalArgumentException("no such component");
        if (!(n instanceof Component)) throw new IllegalArgumentException("no such component");
        Environment env = n.host();
        if (!(env instanceof Database)) throw new IllegalArgumentException("not a database");
        Database database = (Database) env;
        ItemStack sel = invRobot.getStackInSlot(selected);
        int inSlot = sel == null ? 0 : sel.stackSize;
        int maxSize = sel == null ? 64 : sel.getMaxStackSize();
        ItemStack stack = database.getStackInSlot(entry - 1);
        if (stack == null) return new Object[]{0};
        stack.stackSize = Math.min(amount, maxSize - inSlot);
        ItemStack stack2 = stack.copy();
        stack2.stackSize = 1;
        ItemStack sel2;
        if (sel != null) {
            ItemStack sel3 = sel.copy();
            sel3.stackSize = 1;
            sel2 = sel3;
        } else {
            sel2 = null;
        }
        if (sel != null && !ItemStack.areItemStacksEqual(sel2, stack2)) return new Object[]{0};
        IAEItemStack extracted = inv.extractItems(AEApi.instance().storage().createItemStack(stack), Actionable.MODULATE, new MachineSource(tile()));
        if (extracted == null) return new Object[]{0};
        int ext = (int) extracted.getStackSize();
        stack.stackSize = inSlot + ext;
        invRobot.setInventorySlotContents(selected, stack);
        return new Object[]{ext};
    }

    @Callback(doc = "function([number:amount]):number -- Transfer selecte fluid to your ae system.")
    public Object[] sendFluids(Context context, Arguments args) {
        int selected = agent.selectedTank();
        MultiTank tanks = agent.tank();
        if (tanks.tankCount() <= 0) return new Object[]{0};
        IFluidTank tank = tanks.getFluidTank(selected);
        IMEMonitor<IAEFluidStack> inv = getFluidInventory();
        if (tank == null || inv == null || tank.getFluid() == null) return new Object[]{0};
        int amount = Math.min(args.optInteger(0, tank.getCapacity()), tank.getFluidAmount());
        FluidStack fluid = tank.getFluid();
        FluidStack fluid2 = fluid.copy();
        fluid2.amount = amount;
        IAEFluidStack notInjectet = inv.injectItems(AEApi.instance().storage().createFluidStack(fluid2), Actionable.MODULATE, new MachineSource(tile()));
        if (notInjectet == null) {
            tank.drain(amount, true);
            return new Object[]{amount};
        } else {
            tank.drain((int) (amount - notInjectet.getStackSize()), true);
            return new Object[]{amount - notInjectet.getStackSize()};
        }
    }

    @Callback(doc = "function(database:address, entry:number[, number:amount]):number -- Get fluid from your ae system.")
    public Object[] requestFluids(Context context, Arguments args) {
        String address = args.checkString(0);
        int entry = args.checkInteger(1);
        int amount = args.optInteger(2, FluidContainerRegistry.BUCKET_VOLUME);
        MultiTank tanks = agent.tank();
        int selected = agent.selectedTank();
        if (tanks.tankCount() <= 0) return new Object[]{0};
        IFluidTank tank = tanks.getFluidTank(selected);
        IMEMonitor<IAEFluidStack> inv = getFluidInventory();
        if (tank == null || inv == null) return new Object[]{0};
        Node n = node().network().node(address);
        if (n == null) throw new IllegalArgumentException("no such component");
        if (!(n instanceof Component)) throw new IllegalArgumentException("no such component");
        Environment env = n.host();
        if (!(env instanceof Database)) throw new IllegalArgumentException("not a database");
        Database database = (Database) env;
        FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(database.getStackInSlot(entry - 1));
        fluid.amount = amount;
        FluidStack fluid2 = fluid.copy();
        fluid2.amount = tank.fill(fluid, false);
        if (fluid2.amount == 0) return new Object[]{0};
        IAEFluidStack extracted = inv.extractItems(AEApi.instance().storage().createFluidStack(fluid2), Actionable.MODULATE, new MachineSource(tile()));
        if (extracted == null) return new Object[]{0};
        return new Object[]{tank.fill(extracted.getFluidStack(), true)};
    }

    @Override
    public void update() {
        super.update();
        if (host.world().getTotalWorldTime() % 10 == 0 && isActive) {
            if (!((Connector) node()).tryChangeBuffer(-getEnergy())) {
                isActive = false;
            }
        }
    }

    private double getEnergy() {
        ItemStack c = getComponent();
        if (c == null) return 0;
        switch (c.getItemDamage()) {
            case 0:
                return .6;
            case 1:
                return .3;
            default:
                return .05;
        }
    }

    @Override
    public void onMessage(Message message) {
        super.onMessage(message);
        if (Objects.equals(message.name(), "computer.stopped")) {
            isActive = false;
        } else if (Objects.equals(message.name(), "computer.started")) {
            isActive = true;
        }
    }
}