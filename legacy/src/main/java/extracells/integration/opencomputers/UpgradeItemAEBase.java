package extracells.integration.opencomputers;

import cpw.mods.fml.common.Optional.Interface;
import cpw.mods.fml.common.Optional.Method;
import extracells.item.ItemECBase;
import li.cil.oc.api.CreativeTab;
import li.cil.oc.api.driver.EnvironmentProvider;
import li.cil.oc.api.driver.item.*;
import li.cil.oc.api.network.EnvironmentHost;
import li.cil.oc.api.network.ManagedEnvironment;
import net.minecraft.creativetab.CreativeTabs;
import li.cil.oc.api.internal.*;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

@Interface(iface = "li.cil.oc.api.driver.item.HostAware", modid = "OpenComputers", striprefs = true)
public class UpgradeItemAEBase extends ItemECBase implements HostAware {

    @Method(modid = "OpenComputers")
    @Override
    public Item setCreativeTab(CreativeTabs creativeTabs) {
        return super.setCreativeTab(CreativeTab.instance);
    }

    @Override
    public boolean worksWith(ItemStack stack, Class<? extends EnvironmentHost> host) {
        return worksWith(stack) && host != null &&
                (Robot.class.isAssignableFrom(host) || Drone.class.isAssignableFrom(host));
    }

    @Override
    public boolean worksWith(ItemStack stack) {
        return stack != null && stack.getItem() == this;
    }

    @Override
    public ManagedEnvironment createEnvironment(ItemStack stack, EnvironmentHost host) {
        if (stack != null && stack.getItem() == this && worksWith(stack, host.getClass())) {
            return new UpgradeAE(host);
        }
        return null;
    }

    @Method(modid = "OpenComputers")
    @Override
    public String slot(ItemStack itemStack) {
        return Slot.Upgrade;
    }

    @Method(modid = "OpenComputers")
    @Override
    public int tier(ItemStack itemStack) {
        switch (itemStack.getItemDamage()) {
            case 0:
                return 2;
            case 1:
                return 1;
            default:
                return 0;
        }
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        switch (stack.getItemDamage()) {
            case 0:
                return EnumRarity.rare;
            case 1:
                return EnumRarity.uncommon;
            default:
                return super.getRarity(stack);
        }
    }

    @Method(modid = "OpenComputers")
    @Override
    public NBTTagCompound dataTag(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
        NBTTagCompound nbt = stack.getTagCompound();
        if (!nbt.hasKey("oc:data")) {
            nbt.setTag("oc:data", new NBTTagCompound());
        }
        return nbt.getCompoundTag("oc:data");
    }

    public static class Provider implements EnvironmentProvider {
        @Override
        public Class<?> getEnvironment(ItemStack stack) {
            if (stack != null && stack.getItem() instanceof UpgradeItemAEBase) {
                return UpgradeAE.class;
            }
            return null;
        }
    }
}
