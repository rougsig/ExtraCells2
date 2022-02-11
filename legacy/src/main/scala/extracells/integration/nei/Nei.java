package extracells.integration.nei;

import codechicken.nei.api.API;
import extracells.ExtracellsLegacy;
import extracells.registries.BlockEnum;
import extracells.registries.ItemEnum;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Nei {

    private static void hideItems() {
        API.hideItem(new ItemStack(ItemEnum.FLUIDITEM.getItem()));
        API.hideItem(new ItemStack(ItemEnum.CRAFTINGPATTERN.getItem()));
        for (ItemEnum item : ItemEnum.values()) {
            if (item.getMod() != null && (!item.getMod().isEnabled())) {
                Item i = item.getItem();
                List<ItemStack> list = new ArrayList<>();
                i.getSubItems(i, ExtracellsLegacy.ModTab, list);
                for (ItemStack itemStack : list) {
                    API.hideItem(itemStack);
                }
            }
        }

        for (BlockEnum block : BlockEnum.values()) {
            if (block.getMod() != null && (!block.getMod().isEnabled())) {
                Block b = block.getBlock();
                List<ItemStack> list = new ArrayList<>();
                b.getSubBlocks(Item.getItemFromBlock(b), ExtracellsLegacy.ModTab, list);
                for (ItemStack itemStack : list) {
                    API.hideItem(itemStack);
                }
            }
        }
    }

    public static void init() {
        Nei.hideItems();
        if (ExtracellsLegacy.proxy.isClient()) {
            UniversalTerminalRecipe handler = new UniversalTerminalRecipe();
            API.registerUsageHandler(handler);
            API.registerRecipeHandler(handler);
        }
    }
}