package extracells;

import extracells.proxy.CommonProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ExtracellsLegacy {

    // Just check for cross compilation
    // Be clear to delete that constructor
    public ExtracellsLegacy() {
        if (System.currentTimeMillis() == 1) {
            MyKotlinCode.INSTANCE.doMagic();
        }
    }

    public static CommonProxy proxy = null;

    public static CreativeTabs ModTab = new CreativeTabs("Extra_Cells") {
        @Override
        public ItemStack getIconItemStack() {
            return new ItemStack(Item.getItemById(1));
        }

        @Override
        public Item getTabIconItem() {
            return Item.getItemById(1);
        }
    };

    public static Object instance;

    public static String VERSION = "";

    public static boolean shortenedBuckets = true;

    public static boolean dynamicTypes = true;
}
