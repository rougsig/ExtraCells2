package extracells.tileentity;

import appeng.api.config.AccessRestriction;
import appeng.api.config.Actionable;
import appeng.api.config.PowerMultiplier;
import appeng.api.networking.energy.IAEPowerStorage;
import net.minecraft.nbt.NBTTagCompound;

public abstract class TPowerStorage extends TileBase implements IAEPowerStorage {

    PowerInformation powerInformation = new PowerInformation();

    @Override
    public double getAECurrentPower() {
        return powerInformation.currentPower;
    }

    @Override
    public AccessRestriction getPowerFlow() {
        return AccessRestriction.READ_WRITE;
    }

    @Override
    public double getAEMaxPower() {
        return powerInformation.maxPower;
    }

    public void setMaxPower(double power) {
        powerInformation.maxPower = power;
    }

    @Override
    public double injectAEPower(double amt, Actionable mode) {
        double maxStore = powerInformation.maxPower - powerInformation.currentPower;
        double notStorred = (maxStore - amt >= 0) ? 0 : amt - maxStore;
        if (mode == Actionable.MODULATE) {
            powerInformation.currentPower += amt - notStorred;
        }
        return notStorred;
    }

    @Override
    public boolean isAEPublicPowerStorage() {
        return true;
    }

    @Override
    public double extractAEPower(double amount, Actionable mode, PowerMultiplier usePowerMultiplier) {
        double toExtract = Math.min(amount, powerInformation.currentPower);
        if (mode == Actionable.MODULATE) {
            powerInformation.currentPower -= toExtract;
        }
        return toExtract;
    }

    public void readPowerFromNBT(NBTTagCompound tag) {
        if (tag.hasKey("currenPowerBattery")) {
            powerInformation.currentPower = tag.getDouble("currenPowerBattery");
        }
    }

    public void writePowerToNBT(NBTTagCompound tag) {
        tag.setDouble("currenPowerBattery", powerInformation.currentPower);
    }

    static class PowerInformation {
        double currentPower = 0.0D;
        double maxPower = 500.0D;
    }
}