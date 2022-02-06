package extracells.container;

import appeng.api.AEApi;
import extracells.container.slot.SlotRespective;
import extracells.tileentity.TileEntityHardMeDrive;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerHardMEDrive extends Container {
    private InventoryPlayer inventory;
    private TileEntityHardMeDrive tile;

    public ContainerHardMEDrive(InventoryPlayer inventory, TileEntityHardMeDrive tile) {
        this.inventory = inventory;
        this.tile = tile;

        for (int i = 0; i < 3; i++) {
            addSlotToContainer(new SlotRespective(tile.getInventory(), i, 80, 17 + i * 18) {
                @Override
                public boolean isItemValid(ItemStack itemstack) {
                    return AEApi.instance().registries().cell().isCellHandled(itemstack);
                }
            });
        }
        bindPlayerInventory();
    }

    private void bindPlayerInventory() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 8; j++) {
                addSlotToContainer(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 8; i++) {
            addSlotToContainer(new Slot(inventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer p, int i) {
        ItemStack itemstack = null;
        Slot slot = (Slot) inventorySlots.get(i);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (AEApi.instance().registries().cell().isCellHandled(itemstack)) {
                if (i < 3) {
                    if (!mergeItemStack(itemstack1, 3, 38, false)) {
                        return null;
                    }
                } else if (!mergeItemStack(itemstack1, 0, 3, false)) {
                    return null;
                }
                if (itemstack1.stackSize == 0) {
                    slot.putStack(null);
                } else {
                    slot.onSlotChanged();
                }
            }
        }
        return itemstack;
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return tile.hasWorldObj()
                && tile.getWorldObj().getTileEntity(tile.xCoord, tile.yCoord, tile.zCoord) == this.tile;
    }
}
