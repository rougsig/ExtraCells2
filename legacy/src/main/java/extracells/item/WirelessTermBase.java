package extracells.item;

import appeng.api.config.AccessRestriction;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

import java.util.List;

public abstract class WirelessTermBase extends PowerItem {

    public static final double MAX_POWER = 1600000;

    public WirelessTermBase() {
        setMaxStackSize(1);
    }

    @Override
    public double getMaxPower() {
        return MAX_POWER;
    }

    @Override
    public AccessRestriction getPowerFlow(ItemStack itemStack) {
        return AccessRestriction.READ_WRITE;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack itemStack) {
        return 1 - getAECurrentPower(itemStack) / MAX_POWER;
    }

    public boolean canHandle(ItemStack is) {
        if (is == null) return false;
        return is.getItem() == this;
    }

    public String getEncryptionKey(ItemStack itemStack) {
        if (!itemStack.hasTagCompound()) {
            itemStack.setTagCompound(new NBTTagCompound());
        }
        return itemStack.getTagCompound().getString("key");
    }

    public void setEncryptionKey(ItemStack itemStack, String encKey, String name) {
        if (!itemStack.hasTagCompound()) {
            itemStack.setTagCompound(new NBTTagCompound());
        }
        NBTTagCompound tagCompound = itemStack.getTagCompound();
        tagCompound.setString("key", encKey);
    }

    public boolean hasPower(EntityPlayer player, double amount, ItemStack is) {
        return getAECurrentPower(is) >= amount;
    }

    public boolean usePower(EntityPlayer player, double amount, ItemStack is) {
        extractAEPower(is, amount);
        return true;
    }

    @Override
    public void getSubItems(Item item, CreativeTabs creativeTab, List itemList) {
        ItemStack itemStack = new ItemStack(item);
        injectAEPower(itemStack, MAX_POWER);
        itemList.add(itemStack);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list2, boolean par4) {
        if (!itemStack.hasTagCompound()) itemStack.setTagCompound(new NBTTagCompound());
        String encryptionKey = itemStack.getTagCompound().getString("key");
        double aeCurrentPower = getAECurrentPower(itemStack);

        list2.add(StatCollector.translateToLocal("gui.appliedenergistics2.StoredEnergy") + ": " +
                aeCurrentPower + " AE - " + Math.floor(aeCurrentPower / MAX_POWER * 1e4) / 1e2 + "%");
        list2.add(StatCollector.translateToLocal((encryptionKey != null && !encryptionKey.isEmpty()) ?
                "gui.appliedenergistics2.Linked" : "gui.appliedenergistics2.Unlinked"));
    }
}