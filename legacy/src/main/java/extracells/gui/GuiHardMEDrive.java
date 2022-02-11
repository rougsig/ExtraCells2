package extracells.gui;

import extracells.container.ContainerHardMEDrive;
import extracells.registries.BlockEnum;
import extracells.tileentity.TileEntityHardMeDrive;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiHardMEDrive extends GuiContainer {

    private static final ResourceLocation guiTexture = new ResourceLocation("extracells", "textures/gui/hardmedrive.png");

    public GuiHardMEDrive(InventoryPlayer inventory, TileEntityHardMeDrive tile) {
        super(new ContainerHardMEDrive(inventory, tile));
        xSize = 176;
        ySize = 166;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        drawDefaultBackground();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().renderEngine.bindTexture(guiTexture);
        int posX = (width - xSize) / 2;
        int posY = (height - ySize) / 2;
        drawTexturedModalRect(posX, posY, 0, 0, xSize, ySize);
        for (Object s : this.inventorySlots.inventorySlots) {
            if (s instanceof Slot) {
                renderBackground((Slot) s);
            }
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int i, int j) {
        fontRendererObj.drawString(BlockEnum.BLASTRESISTANTMEDRIVE.getStatName(), 5, 5, 0x000000);
    }

    private void renderBackground(Slot slot) {
        if (slot.getStack() == null && slot.slotNumber < 3) {
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
            this.mc.getTextureManager().bindTexture(new ResourceLocation("appliedenergistics2", "textures/guis/states.png"));
            this.drawTexturedModalRect(this.guiLeft + slot.xDisplayPosition,
                    this.guiTop + slot.yDisplayPosition, 240, 0, 16, 16);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_LIGHTING);
        }
    }
}