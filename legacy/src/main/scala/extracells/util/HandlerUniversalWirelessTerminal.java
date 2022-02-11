package extracells.util;

import appeng.api.features.IWirelessTermHandler;
import appeng.api.util.IConfigManager;
import extracells.api.IWirelessFluidTermHandler;
import extracells.item.ItemWirelessTerminalUniversal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class HandlerUniversalWirelessTerminal implements IWirelessFluidTermHandler, IWirelessTermHandler {

    public static ItemWirelessTerminalUniversal wireless = new ItemWirelessTerminalUniversal();
    
    @Override
    public boolean canHandle(ItemStack is) {
        return wireless.canHandle(is);
    }

    @Override
    public boolean usePower(EntityPlayer player, double amount, ItemStack is) {
        return wireless.usePower(player, amount, is);
    }

    @Override
    public boolean hasPower(EntityPlayer player, double amount, ItemStack is) {
        return wireless.hasPower(player, amount, is);
    }

    @Override
    public boolean isItemNormalWirelessTermToo(ItemStack is) {
        return wireless.isItemNormalWirelessTermToo(is);
    }

    @Override
    public IConfigManager getConfigManager(ItemStack is) {
        return wireless.getConfigManager(is);
    }

    @Override
    public String getEncryptionKey(ItemStack item) {
        return wireless.getEncryptionKey(item);
    }

    @Override
    public void setEncryptionKey(ItemStack item, String encKey, String name) {
        wireless.setEncryptionKey(item, encKey, name);
    }
}