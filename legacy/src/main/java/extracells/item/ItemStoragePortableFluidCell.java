package extracells.item;

import appeng.api.AEApi;
import appeng.api.config.AccessRestriction;
import appeng.api.config.FuzzyMode;
import appeng.api.storage.IMEInventoryHandler;
import appeng.api.storage.StorageChannel;
import appeng.api.storage.data.IAEFluidStack;
import extracells.api.ECApi;
import extracells.api.IHandlerFluidStorage;
import extracells.api.IPortableFluidStorageCell;
import extracells.util.inventory.ECFluidFilterInventory;
import extracells.util.inventory.ECPrivateInventory;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import java.util.ArrayList;
import java.util.List;

public class ItemStoragePortableFluidCell extends PowerItem implements IPortableFluidStorageCell {

    double MAX_POWER = 20000;
    IIcon icon;

    public ItemStoragePortableFluidCell() {
        setMaxStackSize(1);
        setMaxDamage(0);
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return 1 - getAECurrentPower(stack) / MAX_POWER;
    }

    @Override
    public ArrayList<Fluid> getFilter(ItemStack stack) {
        ECFluidFilterInventory inventory = new ECFluidFilterInventory("", 63, stack);
        ItemStack[] stacks = inventory.slots;
        ArrayList<Fluid> filter = new ArrayList<>();
        if (stacks.length == 0) return null;
        for (ItemStack itemStack : stacks) {
            if (itemStack != null) {
                Fluid f = FluidRegistry.getFluid(itemStack.getItemDamage());
                if (f != null) filter.add(f);
            }
        }
        return filter;
    }

    @Override
    public int getMaxBytes(ItemStack is) {
        return 512;
    }

    @Override
    public int getMaxTypes(ItemStack is) {
        return 3;
    }

    @Override
    public boolean hasPower(EntityPlayer player, double amount, ItemStack is) {
        return getAECurrentPower(is) >= amount;
    }

    @Override
    public IIcon getIconFromDamage(int p_77617_1_) {
        return this.icon;
    }

    @Override
    public boolean usePower(EntityPlayer player, double amount, ItemStack is) {
        extractAEPower(is, amount);
        return false;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    @Override
    public boolean isEditable(ItemStack is) {
        if (is == null) return false;
        return is.getItem() == this;
    }

    @Override
    public void getSubItems(Item item, CreativeTabs creativeTab, List itemList) {
        itemList.add(new ItemStack(item));
        ItemStack itemStack = new ItemStack(item);
        injectAEPower(itemStack, MAX_POWER);
        itemList.add(itemStack);
    }

    @Override
    public String getUnlocalizedName(ItemStack p_77667_1_) {
        return "extracells.item.storage.fluid.portable";
    }

    @Override
    public IInventory getUpgradesInventory(ItemStack itemStack) {
        return new ECPrivateInventory("configInventory", 0, 64);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean par4) {
        IMEInventoryHandler<IAEFluidStack> handler = (IMEInventoryHandler<IAEFluidStack>) AEApi.instance().registries().cell().getCellInventory(itemStack, null, StorageChannel.FLUIDS);
        if (!(handler instanceof IHandlerFluidStorage)) {
            return;
        }
        IHandlerFluidStorage cellHandler = (IHandlerFluidStorage) handler;
        boolean partitioned = cellHandler.isFormatted();
        int usedBytes = cellHandler.usedBytes();
        double aeCurrentPower = getAECurrentPower(itemStack);
        list.add(String.format(StatCollector.translateToLocal("extracells.tooltip.storage.fluid.bytes"), (usedBytes / 250), (cellHandler.totalBytes() / 250)));
        list.add(String.format(StatCollector.translateToLocal("extracells.tooltip.storage.fluid.types"), cellHandler.usedTypes(), cellHandler.totalTypes()));
        if (usedBytes != 0) {
            list.add(String.format(StatCollector.translateToLocal("extracells.tooltip.storage.fluid.content"), usedBytes));
        }
        if (partitioned) {
            list.add(StatCollector.translateToLocal("gui.appliedenergistics2.Partitioned") + " - " + StatCollector.translateToLocal("gui.appliedenergistics2.Precise"));
        }
        list.add(StatCollector.translateToLocal("gui.appliedenergistics2.StoredEnergy") + ": " + aeCurrentPower + " AE - " + Math.floor(aeCurrentPower / MAX_POWER * 1e4) / 1e2 + "%");
    }

    @Override
    public IInventory getConfigInventory(ItemStack itemStack) {
        return new ECFluidFilterInventory("configFluidCell", 63, itemStack);
    }

    @Override
    public FuzzyMode getFuzzyMode(ItemStack is) {
        if (is == null) return null;
        if (!is.hasTagCompound()) is.setTagCompound(new NBTTagCompound());
        if (is.getTagCompound().hasKey("fuzzyMode"))
            return FuzzyMode.valueOf(is.getTagCompound().getString("fuzzyMode"));
        is.getTagCompound().setString("fuzzyMode", FuzzyMode.IGNORE_ALL.name());
        return FuzzyMode.IGNORE_ALL;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        return ECApi.instance().openPortableFluidCellGui(player, itemStack, world);
    }

    @Override
    public void registerIcons(IIconRegister iconRegister) {
        this.icon = iconRegister.registerIcon("extracells:storage.fluid.portable");
    }

    @Override
    public void setFuzzyMode(ItemStack is, FuzzyMode fzMode) {
        if (is == null) return;
        if (!is.hasTagCompound()) is.setTagCompound(new NBTTagCompound());
        NBTTagCompound tag = is.getTagCompound();
        tag.setString("fuzzyMode", fzMode.name());
    }

    @Override
    double getMaxPower() {
        return MAX_POWER;
    }

    @Override
    public AccessRestriction getPowerFlow(ItemStack itemStack) {
        return AccessRestriction.READ_WRITE;
    }

    @Override
    public String getOreFilter(ItemStack is) {
        return "";
    }

    @Override
    public void setOreFilter(ItemStack is, String filter) {
    }
}
