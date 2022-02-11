package extracells.integration.thaumaticenergistics;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import thaumicenergistics.api.ThEApi;

public class ThaumaticEnergistics {

    public static void openEssentiaTerminal(EntityPlayer player, Object terminal) {
        ThEApi.instance().interact().openWirelessTerminalGui(player);
    }

    public static ItemStack getTerminal() {
        return ThEApi.instance().parts().Essentia_Terminal.getStack();
    }

    public static ItemStack getWirelessTerminal() {
        return ThEApi.instance().items().WirelessEssentiaTerminal.getStack();
    }
}