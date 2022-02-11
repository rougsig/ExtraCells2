package extracells.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public interface TGuiBlock {

    @SideOnly(Side.CLIENT)
    default Object getClientGuiElement(EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    default Object getServerGuiElement(EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }
}