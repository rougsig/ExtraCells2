package extracells.integration.WirelessCrafting;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.p455w0rd.wirelesscraftingterminal.api.WCTApi;
import net.p455w0rd.wirelesscraftingterminal.reference.Reference;

public class WirelessCrafting {

    public static void openCraftingTerminal(EntityPlayer player) {
        WCTApi.instance().interact().openWirelessCraftingTerminalGui(player);
    }

    public static Item getBoosterItem() {
        return WCTApi.instance().items().InfinityBoosterCard.getItem();
    }

    public static boolean isBoosterEnabled() {
        return Reference.WCT_BOOSTER_ENABLED;
    }

    public static ItemStack getCraftingTerminal() {
        return WCTApi.instance().items().WirelessCraftingTerminal.getStack();
    }
}