package extracells.item;

import extracells.api.ECApi;
import extracells.api.IWirelessFluidTermHandler;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemWirelessTerminalFluid extends WirelessTermBase implements IWirelessFluidTermHandler {

    private IIcon icon = null;

    public ItemWirelessTerminalFluid() {
        ECApi.instance().registerWirelessFluidTermHandler(this);
    }

    @Override
    public IIcon getIconFromDamage(int meta) {
        return this.icon;
    }

    @Override
    public String getUnlocalizedName(ItemStack Stack) {
        return super.getUnlocalizedName(Stack).replace("item.extracells", "extracells.item");
    }

    @Override
    public boolean isItemNormalWirelessTermToo(ItemStack is) {
        return false;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
        return ECApi.instance().openWirelessFluidTerminal(entityPlayer, itemStack, world);
    }

    @Override
    public void registerIcons(IIconRegister iconRegister) {
        this.icon = iconRegister.registerIcon("extracells:" + "terminal.fluid.wireless");
    }
}