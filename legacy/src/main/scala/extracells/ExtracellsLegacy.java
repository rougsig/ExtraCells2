package extracells;

import extracells.proxy.CommonProxy;
import extracells.registries.ItemEnum;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ExtracellsLegacy {

    public static CommonProxy proxy = null;

    public static CreativeTabs ModTab = new CreativeTabs("Extra_Cells") {
        @Override
        public ItemStack getIconItemStack() {
            return new ItemStack(ItemEnum.FLUIDSTORAGE.getItem());
        }

        @Override
        public Item getTabIconItem() {
            return ItemEnum.FLUIDSTORAGE.getItem();
        }
    };

    public static Object instance;

    public static String VERSION = "";

    public static boolean shortenedBuckets = true;

    public static boolean dynamicTypes = true;
}