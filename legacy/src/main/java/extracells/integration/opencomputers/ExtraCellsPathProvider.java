package extracells.integration.opencomputers;

import extracells.item.ItemOCUpgrade;
import extracells.registries.ItemEnum;
import li.cil.oc.api.Manual;
import li.cil.oc.api.manual.PathProvider;
import li.cil.oc.api.prefab.ItemStackTabIconRenderer;
import li.cil.oc.api.prefab.ResourceContentProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ExtraCellsPathProvider implements PathProvider {

    public ExtraCellsPathProvider() {
        Manual.addProvider(this);
        Manual.addProvider(new ResourceContentProvider("extracells", "doc/"));
        Manual.addTab(new ItemStackTabIconRenderer(new ItemStack(ItemEnum.FLUIDSTORAGE.getItem())),
                "itemGroup.Extra_Cells", "extracells/%LANGUAGE%/index.md");
    }

    @Override
    public String pathFor(ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemOCUpgrade) {
            return "extracells/%LANGUAGE%/me_upgrade.md";
        }
        return null;
    }

    @Override
    public String pathFor(World world, int i, int i1, int i2) {
        return null;
    }
}
