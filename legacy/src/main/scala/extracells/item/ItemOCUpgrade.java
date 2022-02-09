package extracells.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import extracells.integration.opencomputers.UpgradeItemAEBase;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemOCUpgrade extends UpgradeItemAEBase {

    private static final ItemOCUpgrade INSTANCE = new ItemOCUpgrade();

    public static ItemOCUpgrade getInstance() {
        return INSTANCE;
    }

    @Override
    public String getUnlocalizedName() {
        return super.getUnlocalizedName().replace("item.extracells", "extracells.item");
    }

    @Override
    public String getUnlocalizedName(ItemStack p_77667_1_) {
        return getUnlocalizedName();
    }

    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unchecked")
    public void getSubItems(Item item, CreativeTabs CreativeTabs, List list) {
        list.add(new ItemStack(item, 1, 2));
        list.add(new ItemStack(item, 1, 1));
        list.add(new ItemStack(item, 1, 0));
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        int tier;
        switch (stack.getItemDamage()) {
            case 0:
                tier = 3;
                break;
            case 1:
                tier = 2;
                break;
            default:
                tier = 1;
        }
        return super.getItemStackDisplayName(stack) + " (Tier " + tier + ")";
    }
}
