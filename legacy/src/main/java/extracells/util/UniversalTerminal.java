package extracells.util;

import appeng.api.AEApi;
import extracells.integration.Integration;
import extracells.integration.WirelessCrafting.WirelessCrafting;
import extracells.integration.thaumaticenergistics.ThaumaticEnergistics;
import extracells.item.TerminalType;
import extracells.registries.ItemEnum;
import extracells.registries.PartEnum;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class UniversalTerminal {

    static boolean isThaLoaded = Integration.Mods.THAUMATICENERGISTICS.isEnabled();
    static boolean isWcLLoaded = Integration.Mods.WIRELESSCRAFTING.isEnabled();
    static int arrayLength = isThaLoaded && isWcLLoaded ? 4 : isThaLoaded || isWcLLoaded ? 3 : 2;

    public static ItemStack[] wirelessTerminals() {
        ItemStack[] terminals = new ItemStack[arrayLength];
        terminals[0] = AEApi.instance().definitions().items().wirelessTerminal().maybeStack(1).get();
        terminals[1] = ItemEnum.FLUIDWIRELESSTERMINAL.getSizedStack(1);
        int next = 2;
        if (isThaLoaded) {
            terminals[next++] = ThaumaticEnergistics.getWirelessTerminal();
        }
        if (isWcLLoaded) {
            terminals[next] = WirelessCrafting.getCraftingTerminal();
        }
        return terminals;
    }

    public static ItemStack[] terminals() {
        int currentSize = isWcLLoaded ? arrayLength - 1 : arrayLength;
        ItemStack[] terminals = new ItemStack[currentSize];
        terminals[0] = AEApi.instance().definitions().parts().terminal().maybeStack(1).get();
        terminals[1] = ItemEnum.PARTITEM.getDamagedStack(PartEnum.FLUIDTERMINAL.ordinal());
        if (isThaLoaded) {
            terminals[2] = ThaumaticEnergistics.getTerminal();
        }
        return terminals;
    }

    public static boolean isTerminal(ItemStack stack) {
        if (stack == null) return false;
        Item item = stack.getItem();
        int meta = stack.getItemDamage();
        if (item == null) return false;
        ItemStack aeterm = AEApi.instance().definitions().parts().terminal().maybeStack(1).get();
        if (item == aeterm.getItem() && meta == aeterm.getItemDamage()) return true;
        ItemStack ecterm = ItemEnum.PARTITEM.getDamagedStack(PartEnum.FLUIDTERMINAL.ordinal());
        if (item == ecterm.getItem() && meta == ecterm.getItemDamage()) return true;
        if (Integration.Mods.THAUMATICENERGISTICS.isEnabled()) {
            ItemStack thterm = ThaumaticEnergistics.getTerminal();
            return item == thterm.getItem() && meta == thterm.getItemDamage();
        }
        return false;
    }

    public static boolean isWirelessTerminal(ItemStack stack) {
        if (stack == null) return false;
        Item item = stack.getItem();
        int meta = stack.getItemDamage();
        if (item == null) return false;
        ItemStack aeterm = AEApi.instance().definitions().items().wirelessTerminal().maybeStack(1).get();
        if (item == aeterm.getItem() && meta == aeterm.getItemDamage()) return true;
        ItemStack ecterm = ItemEnum.FLUIDWIRELESSTERMINAL.getDamagedStack(0);
        if (item == ecterm.getItem() && meta == ecterm.getItemDamage()) return true;
        if (Integration.Mods.THAUMATICENERGISTICS.isEnabled()) {
            ItemStack thterm = ThaumaticEnergistics.getWirelessTerminal();
            if (item == thterm.getItem() && meta == thterm.getItemDamage()) return true;
        }
        if (isWcLLoaded) {
            ItemStack wcTerm = WirelessCrafting.getCraftingTerminal();
            return item == wcTerm.getItem() && meta == wcTerm.getItemDamage();
        }
        return false;
    }

    public static TerminalType getTerminalType(ItemStack stack) {
        if (stack == null) return null;
        Item item = stack.getItem();
        int meta = stack.getItemDamage();
        if (item == null) return null;
        ItemStack aeterm = AEApi.instance().definitions().items().wirelessTerminal().maybeStack(1).get();
        if (item == aeterm.getItem() && meta == aeterm.getItemDamage()) return TerminalType.ITEM;
        ItemStack ecterm = ItemEnum.PARTITEM.getDamagedStack(PartEnum.FLUIDTERMINAL.ordinal());
        if (item == ecterm.getItem() && meta == ecterm.getItemDamage()) return TerminalType.FLUID;
        if (Integration.Mods.THAUMATICENERGISTICS.isEnabled()) {
            ItemStack thterm = ThaumaticEnergistics.getTerminal();
            if (item == thterm.getItem() && meta == thterm.getItemDamage()) return TerminalType.ESSENTIA;
        }
        aeterm = AEApi.instance().definitions().items().wirelessTerminal().maybeStack(1).get();
        if (item == aeterm.getItem() && meta == aeterm.getItemDamage()) return TerminalType.ITEM;
        ecterm = ItemEnum.FLUIDWIRELESSTERMINAL.getDamagedStack(0);
        if (item == ecterm.getItem() && meta == ecterm.getItemDamage()) return TerminalType.FLUID;
        if (Integration.Mods.THAUMATICENERGISTICS.isEnabled()) {
            ItemStack thterm = ThaumaticEnergistics.getWirelessTerminal();
            if (item == thterm.getItem() && meta == thterm.getItemDamage()) return TerminalType.ESSENTIA;
        }
        if (isWcLLoaded) {
            ItemStack wcTerm = WirelessCrafting.getCraftingTerminal();
            if (item == wcTerm.getItem() && meta == wcTerm.getItemDamage()) return TerminalType.CRAFTING;
        }
        return null;
    }
}
