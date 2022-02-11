package extracells.item;

import appeng.api.AEApi;
import appeng.api.features.IWirelessTermHandler;
import appeng.api.util.IConfigManager;
import extracells.api.ECApi;
import extracells.api.IWirelessFluidTermHandler;
import extracells.integration.Integration;
import extracells.integration.WirelessCrafting.WirelessCrafting;
import extracells.integration.thaumaticenergistics.ThaumaticEnergistics;
import extracells.util.HandlerUniversalWirelessTerminal;
import extracells.wireless.ConfigManager;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.List;

public class ItemWirelessTerminalUniversal extends WirelessTermBase
        implements IWirelessFluidTermHandler, IWirelessTermHandler, EssentiaTerminal, CraftingTerminal {

    static boolean isTeEnabled = Integration.Mods.THAUMATICENERGISTICS.isEnabled();
    static boolean isWcEnabled = Integration.Mods.WIRELESSCRAFTING.isEnabled();
    private IIcon icon;

    public ItemWirelessTerminalUniversal() {
        if (isWcEnabled) {
            ECApi.instance().registerWirelessFluidTermHandler(this);
            AEApi.instance().registries().wireless().registerWirelessHandler(this);
        } else {
            ECApi.instance().registerWirelessFluidTermHandler(new HandlerUniversalWirelessTerminal());
            AEApi.instance().registries().wireless().registerWirelessHandler(new HandlerUniversalWirelessTerminal());
        }
    }

    @Override
    public boolean isItemNormalWirelessTermToo(ItemStack is) {
        return true;
    }

    @Override
    public IConfigManager getConfigManager(ItemStack itemStack) {
        NBTTagCompound nbt = ensureTagCompound(itemStack);
        if (!nbt.hasKey("settings")) {
            nbt.setTag("settings", new NBTTagCompound());
        }
        NBTTagCompound tag = nbt.getCompoundTag("settings");
        return new ConfigManager(tag);
    }

    @Override
    public NBTTagCompound ensureTagCompound(ItemStack itemStack) {
        if (!itemStack.hasTagCompound()) {
            itemStack.setTagCompound(new NBTTagCompound());
        }
        return itemStack.getTagCompound();
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        return super.getUnlocalizedName(itemStack).replace("item.extracells", "extracells.item");
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
        if (world.isRemote) {
            if (entityPlayer.isSneaking()) {
                return itemStack;
            }
            NBTTagCompound tag = ensureTagCompound(itemStack);
            if (!tag.hasKey("type")) {
                tag.setByte("type", (byte) 0);
            }
            if (tag.getByte("type") == 4 && isWcEnabled) {
                WirelessCrafting.openCraftingTerminal(entityPlayer);
            }
            return itemStack;
        }
        NBTTagCompound tag = ensureTagCompound(itemStack);
        if (!tag.hasKey("type")) {
            tag.setByte("type", (byte) 0);
        }
        if (entityPlayer.isSneaking()) {
            return changeMode(itemStack, entityPlayer, tag);
        }
        byte matched = tag.getByte("type");
        switch (matched) {
            case 0:
                AEApi.instance().registries().wireless().openWirelessTerminalGui(itemStack, world, entityPlayer);
                break;
            case 1:
                ECApi.instance().openWirelessFluidTerminal(entityPlayer, itemStack, world);
                break;
            case 3:
                if (isTeEnabled) ThaumaticEnergistics.openEssentiaTerminal(entityPlayer, this);
                break;
        }
        return itemStack;
    }

    public ItemStack changeMode(ItemStack itemStack, EntityPlayer player, NBTTagCompound tag) {
        EnumSet<TerminalType> installed = getInstalledModules(itemStack);
        byte matched = tag.getByte("type");
        switch (matched) {
            case 0: {
                if (installed.contains(TerminalType.FLUID)) {
                    tag.setByte("type", (byte) 1);
                } else if (isTeEnabled && installed.contains(TerminalType.ESSENTIA)) {
                    tag.setByte("type", (byte) 3);
                } else if (isWcEnabled && installed.contains(TerminalType.CRAFTING)) {
                    tag.setByte("type", (byte) 4);
                }
            }
            break;
            case 1: {
                if (isTeEnabled && installed.contains(TerminalType.ESSENTIA)) {
                    tag.setByte("type", (byte) 3);
                } else if (isWcEnabled && installed.contains(TerminalType.CRAFTING)) {
                    tag.setByte("type", (byte) 4);
                } else if (installed.contains(TerminalType.ITEM)) {
                    tag.setByte("type", (byte) 0);
                }
            }
            break;
            case 2: {
                if (isTeEnabled && installed.contains(TerminalType.ESSENTIA)) {
                    tag.setByte("type", (byte) 3);
                } else if (isWcEnabled && installed.contains(TerminalType.CRAFTING)) {
                    tag.setByte("type", (byte) 4);
                } else if (installed.contains(TerminalType.ITEM)) {
                    tag.setByte("type", (byte) 0);
                } else if (installed.contains(TerminalType.FLUID)) {
                    tag.setByte("type", (byte) 1);
                }
            }
            break;
            case 3: {
                if (isWcEnabled && installed.contains(TerminalType.CRAFTING)) {
                    tag.setByte("type", (byte) 4);
                } else if (installed.contains(TerminalType.ITEM)) {
                    tag.setByte("type", (byte) 0);
                } else if (installed.contains(TerminalType.FLUID)) {
                    tag.setByte("type", (byte) 1);
                }
            }
            break;
            default: {
                if (installed.contains(TerminalType.ITEM)) {
                    tag.setByte("type", (byte) 0);
                } else if (installed.contains(TerminalType.FLUID)) {
                    tag.setByte("type", (byte) 1);
                } else if (isTeEnabled && installed.contains(TerminalType.ESSENTIA)) {
                    tag.setByte("type", (byte) 3);
                } else if (isWcEnabled && installed.contains(TerminalType.CRAFTING)) {
                    tag.setByte("type", (byte) 4);
                } else tag.setByte("type", (byte) 0);
            }
            break;
        }
        return itemStack;
    }

    @Override
    public void registerIcons(IIconRegister iconRegister) {
        this.icon = iconRegister.registerIcon("extracells:" + "terminal.universal.wireless");
    }

    @Override
    public IIcon getIconFromDamage(int meta) {
        return this.icon;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list2, boolean par4) {
        NBTTagCompound tag = ensureTagCompound(itemStack);
        if (!tag.hasKey("type")) {
            tag.setByte("type", (byte) 0);
        }
        list2.add(StatCollector.translateToLocal("extracells.tooltip.mode") + ": "
                + StatCollector.translateToLocal("extracells.tooltip."
                + TerminalType.values()[tag.getByte("type")].toString().toLowerCase()));
        list2.add(StatCollector.translateToLocal("extracells.tooltip.installed"));
        for (TerminalType it : getInstalledModules(itemStack)) {
            list2.add("- " + StatCollector.translateToLocal("extracells.tooltip." + it.name().toLowerCase()));
        }
        super.addInformation(itemStack, player, list2, par4);
    }

    public void installModule(ItemStack itemStack, TerminalType module) {
        if (isInstalled(itemStack, module)) return;
        byte install = (byte) (1 << module.ordinal());
        NBTTagCompound tag = ensureTagCompound(itemStack);
        byte installed = tag.hasKey("modules") ? (byte) (tag.getByte("modules") + install) : install;
        tag.setByte("modules", installed);
    }

    public EnumSet<TerminalType> getInstalledModules(ItemStack itemStack) {
        if (itemStack == null || itemStack.getItem() == null) {
            return EnumSet.noneOf(TerminalType.class);
        }
        NBTTagCompound tag = ensureTagCompound(itemStack);
        byte installed = tag.hasKey("modules") ? tag.getByte("modules") : 0;
        EnumSet<TerminalType> set = EnumSet.noneOf(TerminalType.class);
        for (TerminalType x : TerminalType.values()) {
            if (1 == (installed >> x.ordinal()) % 2) {
                set.add(x);
            }
        }
        return set;
    }

    public boolean isInstalled(ItemStack itemStack, TerminalType module) {
        if (itemStack == null || itemStack.getItem() == null) return false;
        NBTTagCompound tag = ensureTagCompound(itemStack);
        byte installed = tag.hasKey("modules") ? tag.getByte("modules") : 0;
        return 1 == (installed >> module.ordinal()) % 2;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void getSubItems(Item item, CreativeTabs creativeTab, List itemList2) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setByte("modules", (byte) 31);
        ItemStack itemStack = new ItemStack(item);
        itemStack.setTagCompound(tag);
        itemList2.add(itemStack.copy());
        injectAEPower(itemStack, WirelessTermBase.MAX_POWER);
        itemList2.add(itemStack);
    }
}
