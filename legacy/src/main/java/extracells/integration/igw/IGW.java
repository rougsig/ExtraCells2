package extracells.integration.igw;

import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.registry.GameRegistry;
import extracells.ExtracellsLegacy;
import extracells.integration.Integration;
import extracells.registries.BlockEnum;
import extracells.registries.ItemEnum;
import igwmod.api.WikiRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class IGW {

    public static void initNotifier() {
        new IGWSupportNotifier();
    }

    @Optional.Method(modid = "IGWMod")
    public static void init() {
        for (ItemEnum item : ItemEnum.values()) {
            if (item != ItemEnum.CRAFTINGPATTERN && item != ItemEnum.FLUIDITEM) {
                if (item == ItemEnum.FLUIDPATTERN) {
                    WikiRegistry.registerBlockAndItemPageEntry(item.getSizedStack(1),
                            item.getSizedStack(1).getUnlocalizedName().replace(".", "/"));
                } else if (item == ItemEnum.STORAGECOMPONENT || item == ItemEnum.STORAGECASING) {
                    List<ItemStack> list = new ArrayList<>();
                    item.getItem().getSubItems(item.getItem(), ExtracellsLegacy.ModTab, list);
                    for (ItemStack stack : list) {
                        WikiRegistry.registerBlockAndItemPageEntry(stack, "extracells/item/crafting");
                    }
                } else {
                    List<ItemStack> list = new ArrayList<>();
                    item.getItem().getSubItems(item.getItem(), ExtracellsLegacy.ModTab, list);
                    for (ItemStack stack : list) {
                        WikiRegistry.registerBlockAndItemPageEntry(stack, stack.getUnlocalizedName().replace(".", "/"));
                    }
                }
            }
        }
        if (Integration.Mods.OPENCOMPUTERS.isEnabled()) {
            ItemStack stack = GameRegistry.findItemStack("extracells", "oc.upgrade", 1);
            WikiRegistry.registerBlockAndItemPageEntry(stack.getItem(), stack.getUnlocalizedName().replace(".", "/"));
        }
        for (BlockEnum block : BlockEnum.values()) {
            List<ItemStack> list = new ArrayList<>();
            Item.getItemFromBlock(block.getBlock()).getSubItems(Item.getItemFromBlock(block.getBlock()), ExtracellsLegacy.ModTab, list);
            for (ItemStack stack : list) {
                WikiRegistry.registerBlockAndItemPageEntry(stack, stack.getUnlocalizedName().replace(".", "/").replace("tile/", ""));
            }
        }
    }
}
