package extracells.util;

import appeng.api.features.IWirelessTermHandler;
import appeng.api.util.IConfigManager;
import extracells.api.IWirelessFluidTermHandler;
import extracells.item.ItemWirelessTerminalUniversal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class HandlerUniversalWirelessTerminal implements IWirelessFluidTermHandler, IWirelessTermHandler {
    
    @Override
    public boolean canHandle(ItemStack is) {
        return ItemWirelessTerminalUniversal.canHandle(is);
    }

    @Override
    public boolean usePower(EntityPlayer player, double amount, ItemStack is) {
        return ItemWirelessTerminalUniversal.usePower(player, amount, is);
    }

    @Override
    public boolean hasPower(EntityPlayer player, double amount, ItemStack is) {
        return ItemWirelessTerminalUniversal.hasPower(player, amount, is);
    }

    @Override
    public boolean isItemNormalWirelessTermToo(ItemStack is) {
        return ItemWirelessTerminalUniversal.isItemNormalWirelessTermToo(is);
    }

    @Override
    public IConfigManager getConfigManager(ItemStack is) {
        return ItemWirelessTerminalUniversal.getConfigManager(is);
    }

    @Override
    public String getEncryptionKey(ItemStack item) {
        return ItemWirelessTerminalUniversal.getEncryptionKey(item);
    }

    @Override
    public void setEncryptionKey(ItemStack item, String encKey, String name) {
        ItemWirelessTerminalUniversal.setEncryptionKey(item, encKey, name);
    }
}