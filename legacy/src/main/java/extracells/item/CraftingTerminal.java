package extracells.item;

import appeng.core.features.IFeatureHandler;
import cpw.mods.fml.common.Optional;
import extracells.integration.WirelessCrafting.WirelessCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.p455w0rd.wirelesscraftingterminal.api.IWirelessCraftingTerminalItem;

@Optional.Interface(iface = "net.p455w0rd.wirelesscraftingterminal.api.IWirelessCraftingTerminalItem", modid = "ae2wct", striprefs = true)
public interface CraftingTerminal extends IWirelessCraftingTerminalItem {

    @Optional.Method(modid = "ae2wct")
    @Override
    default boolean checkForBooster(ItemStack wirelessTerminal) {
        if (wirelessTerminal.hasTagCompound()) {
            NBTTagList boosterNBTList = wirelessTerminal.getTagCompound().getTagList("BoosterSlot", 10);
            if (boosterNBTList != null) {
                NBTTagCompound boosterTagCompound = boosterNBTList.getCompoundTagAt(0);
                if (boosterTagCompound != null) {
                    ItemStack boosterCard = ItemStack.loadItemStackFromNBT(boosterTagCompound);
                    if (boosterCard != null) {
                        return boosterCard.getItem() == WirelessCrafting.getBoosterItem() && WirelessCrafting.isBoosterEnabled();
                    }
                }
            }
        }
        return false;
    }

    @Optional.Method(modid = "ae2wct")
    default IFeatureHandler handler() {
        return null;
    }

    @Optional.Method(modid = "ae2wct")
    @Override
    default void postInit() {
    }

    @Optional.Method(modid = "ae2wct")
    @Override
    default boolean isWirelessCraftingEnabled(ItemStack itemStack) {
        if (this instanceof ItemWirelessTerminalUniversal) {
            return ((ItemWirelessTerminalUniversal) this).isInstalled(itemStack, TerminalType.CRAFTING);
        } else {
            return true;
        }
    }
}

