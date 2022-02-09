package extracells.item;

import appeng.api.config.PowerUnits;
import appeng.api.implementations.items.IAEItemPowerStorage;
import cofh.api.energy.IEnergyContainerItem;
import cpw.mods.fml.common.Optional;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

@Optional.Interface(iface = "cofh.api.energy.IEnergyContainerItem", modid = "CoFHAPI|energy", striprefs = true)
public abstract class PowerItem extends ItemECBase implements IAEItemPowerStorage, IEnergyContainerItem {

    abstract double getMaxPower();

    @Optional.Method(modid = "CoFHAPI|energy")
    @Override
    public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {
        if (container == null) return 0;
        if (simulate) {
            double current = PowerUnits.AE.convertTo(PowerUnits.RF, getAECurrentPower(container));
            double max = PowerUnits.AE.convertTo(PowerUnits.RF, getAEMaxPower(container));
            if (max - current >= maxReceive) {
                return maxReceive;
            } else {
                return (int) (max - current);
            }
        } else {
            double currentAEPower = getAECurrentPower(container);
            if (currentAEPower < getAEMaxPower(container)) {
                return (int) PowerUnits.AE.convertTo(PowerUnits.RF,
                        injectAEPower(container, PowerUnits.RF.convertTo(PowerUnits.AE, maxReceive)));
            } else {
                return 0;
            }
        }
    }

    @Optional.Method(modid = "CoFHAPI|energy")
    @Override
    public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {
        if (container == null) return 0;
        if (simulate) {
            if (getEnergyStored(container) >= maxExtract) {
                return maxExtract;
            } else {
                return getEnergyStored(container);
            }
        } else {
            return (int) PowerUnits.AE.convertTo(PowerUnits.RF, extractAEPower(container, PowerUnits.RF.convertTo(PowerUnits.AE, maxExtract)));
        }
    }

    @Optional.Method(modid = "CoFHAPI|energy")
    @Override
    public int getEnergyStored(ItemStack container) {
        return (int) PowerUnits.AE.convertTo(PowerUnits.RF, getAECurrentPower(container));
    }

    @Optional.Method(modid = "CoFHAPI|energy")
    @Override
    public int getMaxEnergyStored(ItemStack container) {
        return (int) PowerUnits.AE.convertTo(PowerUnits.RF, getAEMaxPower(container));
    }

    @Override
    public double injectAEPower(ItemStack itemStack, double amt) {
        NBTTagCompound tagCompound = ensureTagCompound(itemStack);
        double currentPower = tagCompound.getDouble("power");
        double toInject = Math.min(amt, getMaxPower() - currentPower);
        tagCompound.setDouble("power", currentPower + toInject);
        return toInject;
    }

    @Override
    public double extractAEPower(ItemStack itemStack, double amt) {
        NBTTagCompound tagCompound = ensureTagCompound(itemStack);
        double currentPower = tagCompound.getDouble("power");
        double toExtract = Math.min(amt, currentPower);
        tagCompound.setDouble("power", currentPower - toExtract);
        return toExtract;
    }

    @Override
    public double getAEMaxPower(ItemStack itemStack) {
        return getMaxPower();
    }

    @Override
    public double getAECurrentPower(ItemStack itemStack) {
        NBTTagCompound tagCompound = ensureTagCompound(itemStack);
        return tagCompound.getDouble("power");
    }

    private NBTTagCompound ensureTagCompound(ItemStack itemStack) {
        if (!itemStack.hasTagCompound()) {
            itemStack.setTagCompound(new NBTTagCompound());
        }
        return itemStack.getTagCompound();
    }
}