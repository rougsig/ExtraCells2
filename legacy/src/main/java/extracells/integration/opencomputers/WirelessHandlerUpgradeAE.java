package extracells.integration.opencomputers;

import appeng.api.features.IWirelessTermHandler;
import appeng.api.util.IConfigManager;
import extracells.item.ItemOCUpgrade;
import li.cil.oc.common.item.data.DroneData;
import li.cil.oc.common.item.data.RobotData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class WirelessHandlerUpgradeAE implements IWirelessTermHandler {

    @Override
    public boolean canHandle(ItemStack itemStack) {
        if (itemStack == null) return false;
        Item item = itemStack.getItem();
        if (item instanceof ItemOCUpgrade) return true;
        return (OCUtils.isRobot(itemStack) && OCUtils.getComponent(new RobotData(itemStack), new ItemOCUpgrade()) != null) ||
                (OCUtils.isDrone(itemStack) && OCUtils.getComponent(new DroneData(itemStack), new ItemOCUpgrade()) != null);
    }

    @Override
    public boolean usePower(EntityPlayer entityPlayer, double v, ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean hasPower(EntityPlayer entityPlayer, double v, ItemStack itemStack) {
        return true;
    }

    @Override
    public IConfigManager getConfigManager(ItemStack itemStack) {
        return null;
    }

    @Override
    public String getEncryptionKey(ItemStack itemStack) {
        if (OCUtils.isRobot(itemStack)) {
            return getEncryptionKeyRobot(itemStack);
        }
        if (OCUtils.isDrone(itemStack)) {
            return getEncryptionKeyDrone(itemStack);
        }
        if (!itemStack.hasTagCompound()) {
            itemStack.setTagCompound(new NBTTagCompound());
        }
        return itemStack.getTagCompound().getString("key");
    }

    @Override
    public void setEncryptionKey(ItemStack itemStack, String encKey, String name) {
        if (OCUtils.isRobot(itemStack)) {
            setEncryptionKeyRobot(itemStack, encKey, name);
            return;
        }
        if (OCUtils.isDrone(itemStack)) {
            setEncryptionKeyDrone(itemStack, encKey, name);
            return;
        }
        if (!itemStack.hasTagCompound()) {
            itemStack.setTagCompound(new NBTTagCompound());
        }
        NBTTagCompound tagCompound = itemStack.getTagCompound();
        tagCompound.setString("key", encKey);
    }

    public void setEncryptionKeyRobot(ItemStack itemStack, String encKey, String name) {
        RobotData robot = new RobotData(itemStack);
        ItemStack component = OCUtils.getComponent(robot, new UpgradeItemAEBase());
        if (component != null) setEncryptionKey(component, encKey, name);
        robot.save(itemStack);
    }

    public String getEncryptionKeyRobot(ItemStack itemStack) {
        RobotData robot = new RobotData(itemStack);
        ItemStack component = OCUtils.getComponent(robot, new UpgradeItemAEBase());
        if (component == null) return "";
        return getEncryptionKey(component);
    }

    public void setEncryptionKeyDrone(ItemStack itemStack, String encKey, String name) {
        DroneData drone = new DroneData(itemStack);
        ItemStack component = OCUtils.getComponent(drone, new UpgradeItemAEBase());
        if (component != null) setEncryptionKey(component, encKey, name);
        drone.save(itemStack);
    }

    public String getEncryptionKeyDrone(ItemStack itemStack) {
        DroneData drone = new DroneData(itemStack);
        ItemStack component = OCUtils.getComponent(drone, new UpgradeItemAEBase());
        if (component == null) return "";
        return getEncryptionKey(component);
    }
}