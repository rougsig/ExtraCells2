package extracells.util.recipe;

import appeng.api.features.INetworkEncodable;
import appeng.api.implementations.items.IAEItemPowerStorage;
import extracells.item.ItemWirelessTerminalUniversal;
import extracells.item.TerminalType;
import extracells.registries.ItemEnum;
import extracells.util.UniversalTerminal;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class RecipeUniversalTerminal implements IRecipe {
    @Override
    public boolean matches(InventoryCrafting inventory, World world) {
        boolean hasWireless = false;
        boolean isUniversal = false;
        boolean hasTerminal = false;
        ItemWirelessTerminalUniversal itemUniversal = new ItemWirelessTerminalUniversal();
        List<TerminalType> terminals = new ArrayList<>();
        ItemStack terminal = null;
        int size = inventory.getSizeInventory();
        for (int i = 0; i < size; i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (stack != null) {
                Item item = stack.getItem();
                if (item == itemUniversal) {
                    if (hasWireless) {
                        return false;
                    } else {
                        hasWireless = true;
                        isUniversal = true;
                        terminal = stack;
                    }
                } else if (UniversalTerminal.isWirelessTerminal(stack)) {
                    if (hasWireless) return false;
                    hasWireless = true;
                    terminal = stack;
                } else if (UniversalTerminal.isTerminal(stack)) {
                    hasTerminal = true;
                    TerminalType typeTerminal = UniversalTerminal.getTerminalType(stack);
                    if (terminals.contains(typeTerminal)) {
                        return false;
                    } else {
                        terminals.add(typeTerminal);
                    }
                }
            }
        }
        if (!(hasTerminal && hasWireless)) return false;
        if (isUniversal) {
            for (TerminalType terminalType : terminals) {
                if (itemUniversal.isInstalled(terminal, terminalType)) return false;
            }
            return true;
        } else {
            TerminalType term = UniversalTerminal.getTerminalType(terminal);
            for (TerminalType terminalType : terminals) {
                if (terminalType == term) return false;
            }
            return true;
        }
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventory) {
        ItemWirelessTerminalUniversal itemUniversal = new ItemWirelessTerminalUniversal();
        boolean isUniversal = false;
        List<TerminalType> terminals = new ArrayList<>();
        ItemStack terminal = null;
        int size = inventory.getSizeInventory();
        for (int i = 0; i < size; i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (stack != null) {
                Item item = stack.getItem();
                if (item == itemUniversal) {
                    isUniversal = true;
                    terminal = stack.copy();
                } else if (UniversalTerminal.isWirelessTerminal(stack)) {
                    terminal = stack.copy();
                } else if (UniversalTerminal.isTerminal(stack)) {
                    TerminalType typeTerminal = UniversalTerminal.getTerminalType(stack);
                    terminals.add(typeTerminal);
                }
            }
        }
        if (isUniversal) {
            for (TerminalType terminalType : terminals) {
                itemUniversal.installModule(terminal, terminalType);
            }
        } else {
            TerminalType terminalType = UniversalTerminal.getTerminalType(terminal);
            if (terminal == null) return null;
            Item itemTerminal = terminal.getItem();
            ItemStack t = new ItemStack(itemUniversal);
            if (itemTerminal instanceof INetworkEncodable) {
                String key = ((INetworkEncodable) itemTerminal).getEncryptionKey(terminal);
                if (key != null) {
                    itemUniversal.setEncryptionKey(t, key, null);
                }
            }
            if (itemTerminal instanceof IAEItemPowerStorage) {
                double power = ((IAEItemPowerStorage) itemTerminal).getAECurrentPower(terminal);
                itemUniversal.injectAEPower(t, power);
            }
            if (terminal.hasTagCompound()) {
                NBTTagCompound nbt = terminal.getTagCompound();
                if (!t.hasTagCompound()) t.setTagCompound(new NBTTagCompound());
                if (nbt.hasKey("BoosterSlot")) {
                    t.getTagCompound().setTag("BoosterSlot", nbt.getTag("BoosterSlot"));
                }
                if (nbt.hasKey("MagnetSlot")) {
                    t.getTagCompound().setTag("MagnetSlot", nbt.getTag("MagnetSlot"));
                }
            }
            itemUniversal.installModule(t, terminalType);
            t.getTagCompound().setByte("type", (byte) terminalType.ordinal());
            terminal = t;
            for (TerminalType x : terminals) {
                itemUniversal.installModule(terminal, x);
            }
        }
        return terminal;
    }

    @Override
    public int getRecipeSize() {
        return 2;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return ItemEnum.UNIVERSALTERMINAL.getDamagedStack(0);
    }
}
