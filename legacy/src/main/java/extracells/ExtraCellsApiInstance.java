package extracells;

import appeng.api.AEApi;
import appeng.api.implementations.tiles.IWirelessAccessPoint;
import appeng.api.networking.IGrid;
import appeng.api.networking.IGridHost;
import appeng.api.networking.IGridNode;
import appeng.api.networking.storage.IStorageGrid;
import appeng.api.storage.*;
import appeng.api.storage.data.IAEFluidStack;
import appeng.api.util.WorldCoord;
import extracells.api.ExtraCellsApi;
import extracells.api.IPortableFluidStorageCell;
import extracells.api.IWirelessFluidTermHandler;
import extracells.api.definitions.IBlockDefinition;
import extracells.api.definitions.IItemDefinition;
import extracells.api.definitions.IPartDefinition;
import extracells.definitions.BlockDefinition;
import extracells.definitions.ItemDefinition;
import extracells.definitions.PartDefinition;
import extracells.inventory.HandlerItemStorageFluid;
import extracells.network.GuiHandler;
import extracells.util.FluidCellHandler;
import extracells.util.FuelBurnTime;
import extracells.wireless.WirelessTermRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;

import java.util.ArrayList;
import java.util.List;

import static extracells.wireless.WirelessTermRegistry.getWirelessTermHandler;

public class ExtraCellsApiInstance implements ExtraCellsApi {

	public static final ExtraCellsApi instance = new ExtraCellsApiInstance();

	private final List<Class<? extends Fluid>> blacklistShowClass = new ArrayList<Class<? extends Fluid>>();
	private final List<Fluid> blacklistShowFluid = new ArrayList<Fluid>();
	private final List<Class<? extends Fluid>> blacklistStorageClass = new ArrayList<Class<? extends Fluid>>();
	private final List<Fluid> blacklistStorageFluid = new ArrayList<Fluid>();

	@Override
	public void addFluidToShowBlacklist(Class<? extends Fluid> clazz) {
		if (clazz == null || clazz == Fluid.class)
			return;
		this.blacklistShowClass.add(clazz);
	}

	@Override
	public void addFluidToShowBlacklist(Fluid fluid) {
		if (fluid == null)
			return;
		this.blacklistShowFluid.add(fluid);
	}

	@Override
	public void addFluidToStorageBlacklist(Class<? extends Fluid> clazz) {
		if (clazz == null || clazz == Fluid.class)
			return;
		this.blacklistStorageClass.add(clazz);
	}

	@Override
	public void addFluidToStorageBlacklist(Fluid fluid) {
		if (fluid == null)
			return;
		this.blacklistStorageFluid.add(fluid);
	}

	@Override
	public IBlockDefinition blocks() {
		return BlockDefinition.instance;
	}

	@Override
	public boolean canFluidSeeInTerminal(Fluid fluid) {
		if (fluid == null)
			return false;
		if (this.blacklistShowFluid.contains(fluid))
			return false;
		for (Class<? extends Fluid> clazz : this.blacklistShowClass) {
			if (clazz.isInstance(fluid))
				return false;
		}
		return true;
	}

	@Override
	public boolean canStoreFluid(Fluid fluid) {
		if (fluid == null)
			return false;
		if (this.blacklistStorageFluid.contains(fluid))
			return false;
		for (Class<? extends Fluid> clazz : this.blacklistStorageClass) {
			if (clazz.isInstance(fluid))
				return false;
		}
		return true;
	}

	/**
	 * @deprecated Incorrect spelling
	 */
	@Override
	@Deprecated
	public String getVerion() {
		return ExtracellsLegacy.VERSION;
	}

	@Override
	public String getVersion() {
		return ExtracellsLegacy.VERSION;
	}

	@Override
	public IWirelessFluidTermHandler getWirelessFluidTermHandler(ItemStack is) {
		return (IWirelessFluidTermHandler) getWirelessTermHandler(is);
	}

	@Override
	public boolean isWirelessFluidTerminal(ItemStack is) {
		return WirelessTermRegistry.isWirelessItem(is);
	}

	@Override
	public IItemDefinition items() {
		return ItemDefinition.instance;
	}

	@Override
	public ItemStack openPortableCellGui (EntityPlayer player, ItemStack stack, World world){
		return openPortableFluidCellGui(player, stack, world);
	}

	@Override
	public ItemStack openPortableFluidCellGui(EntityPlayer player, ItemStack stack, World world) {
		if (world.isRemote || stack == null || stack.getItem() == null)
			return stack;
		Item item = stack.getItem();
		if (!(item instanceof IPortableFluidStorageCell))
			return stack;
		ICellHandler cellHandler = AEApi.instance().registries().cell().getHandler(stack);
		if (!(cellHandler instanceof FluidCellHandler))
			return stack;
		IMEInventoryHandler<IAEFluidStack> handler = ((FluidCellHandler) cellHandler).getCellInventoryPlayer(stack, player);
		if (!(handler instanceof HandlerItemStorageFluid)) {
			return stack;
		}
		IMEMonitor<IAEFluidStack> fluidInventory = new MEMonitorHandler<IAEFluidStack>(handler, StorageChannel.FLUIDS);
		GuiHandler.launchGui(GuiHandler.getGuiId(3), player, new Object[]{fluidInventory, item});
		return stack;
	}

	@Override
	public ItemStack openWirelessTerminal(EntityPlayer player, ItemStack stack, World world) {
		return openWirelessFluidTerminal(player, stack, world);
	}

	@Override
	public ItemStack openWirelessFluidTerminal(EntityPlayer player, ItemStack stack, World world) {
		if (world.isRemote)
			return stack;
		if (!isWirelessFluidTerminal(stack))
			return stack;
		IWirelessFluidTermHandler handler = getWirelessTermHandler(stack);
		if (!handler.hasPower(player, 1.0D, stack))
			return stack;
		Long key;
		try {
			key = Long.parseLong(handler.getEncryptionKey(stack));
		} catch (Throwable ignored) {
			return stack;
		}
		return openWirelessTerminal(player, stack, world, (int) player.posX, (int) player.posY, (int) player.posZ, key);
	}

	@Deprecated
	@Override
	public ItemStack openWirelessTerminal(EntityPlayer player, ItemStack itemStack, World world, int x, int y, int z, Long key) {
		if (world.isRemote)
			return itemStack;
		IGridHost securityTerminal = (IGridHost) AEApi.instance().registries().locatable().getLocatableBy(key);
		if (securityTerminal == null)
			return itemStack;
		IGridNode gridNode = securityTerminal
				.getGridNode(ForgeDirection.UNKNOWN);
		if (gridNode == null)
			return itemStack;
		IGrid grid = gridNode.getGrid();
		if (grid == null)
			return itemStack;
		for (IGridNode node : grid.getMachines((Class<? extends IGridHost>) AEApi.instance().definitions().blocks().wireless().maybeEntity().get())) {
			IWirelessAccessPoint accessPoint = (IWirelessAccessPoint) node
					.getMachine();
			WorldCoord distance = accessPoint.getLocation().subtract(x, y, z);
			int squaredDistance = distance.x * distance.x + distance.y * distance.y + distance.z * distance.z;
			if (squaredDistance <= accessPoint.getRange() * accessPoint.getRange()) {
				IStorageGrid gridCache = grid.getCache(IStorageGrid.class);
				if (gridCache != null) {
					IMEMonitor<IAEFluidStack> fluidInventory = gridCache.getFluidInventory();
					if (fluidInventory != null) {
						GuiHandler.launchGui(GuiHandler.getGuiId(1), player, new Object[]{
								fluidInventory, getWirelessFluidTermHandler(itemStack)});
					}
				}
			}
		}
		return itemStack;
	}

	@Override
	public IPartDefinition parts() {
		return PartDefinition.instance;
	}

	@Override
	public void registerWirelessFluidTermHandler(IWirelessFluidTermHandler handler) {
		WirelessTermRegistry.registerWirelessTermHandler(handler);
	}

	/**
	 * @deprecated Incorrect spelling
	 */
	@Override
	@Deprecated
	public void registryWirelessFluidTermHandler(IWirelessFluidTermHandler handler) {
		registerWirelessFluidTermHandler(handler);
	}

	@Override
	public void registerFuelBurnTime(Fluid fuel, int burnTime) {
		FuelBurnTime.registerFuel(fuel, burnTime);
	}
}