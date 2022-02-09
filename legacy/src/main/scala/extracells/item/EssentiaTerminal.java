package extracells.item;

import cpw.mods.fml.common.Optional;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.jetbrains.annotations.NotNull;
import thaumicenergistics.api.IThEWirelessEssentiaTerminal;

@Optional.Interface(iface = "thaumicenergistics.api.IThEWirelessEssentiaTerminal", modid = "thaumicenergistics", striprefs = true)
interface EssentiaTerminal extends IThEWirelessEssentiaTerminal {

    @NotNull
    @Override
    default NBTTagCompound getWETerminalTag(@NotNull ItemStack terminalItemstack) {
        NBTTagCompound tag = ensureTagCompound(terminalItemstack);
        if (!tag.hasKey("essentia")) {
            tag.setTag("essentia", new NBTTagCompound());
        }
        return tag.getCompoundTag("essentia");
    }

    default NBTTagCompound ensureTagCompound(ItemStack itemStack) {
        if (!itemStack.hasTagCompound())
            itemStack.stackTagCompound = new NBTTagCompound();
        return itemStack.getTagCompound();
    }
}