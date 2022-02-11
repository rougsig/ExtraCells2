package extracells.render.block;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import extracells.tileentity.TileEntityHardMeDrive;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

public class RendererHardMEDrive implements ISimpleBlockRenderingHandler {

    static int renderID = 0;
    static ResourceLocation tex = new ResourceLocation("extracells", "textures/blocks/hardmedrive.png");

    Icon i = new Icon(5, 11, 5, 7);
    Icon i2 = new Icon(5, 11, 8, 10);
    Icon i3 = new Icon(5, 11, 11, 13);

    public static void registerRenderer() {
        renderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new RendererHardMEDrive());
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        Tessellator tessellator = Tessellator.instance;
        renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 0, 3));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 1, 3));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 2, 3));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 3, 3));
        tessellator.draw();
        Minecraft.getMinecraft().renderEngine.bindTexture(tex);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderMinX = .3125D;
        renderer.renderMinY = .25D;
        renderer.renderMaxX = .6875D;
        renderer.renderMaxY = .375D;
        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, i);
        tessellator.draw();
        renderer.renderMinY = .43525D;
        renderer.renderMaxY = .56025D;
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, i);
        tessellator.draw();
        renderer.renderMinY = .62275D;
        renderer.renderMaxY = .75D;
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, i);
        renderer.renderMinX = 0.0D;
        renderer.renderMinY = 0.0D;
        renderer.renderMaxX = 1.0D;
        renderer.renderMaxY = 1.0D;
        tessellator.draw();
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 4, 3));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 5, 3));
        tessellator.draw();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        Tessellator tessellator = Tessellator.instance;
        renderer.renderStandardBlock(block, x, y, z);
        tessellator.addTranslation(x, y, z);
        int meta = world.getBlockMetadata(x, y, z);
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (!(tileEntity instanceof TileEntityHardMeDrive)) return false;
        TileEntityHardMeDrive tileEntityHardMeDrive = (TileEntityHardMeDrive) tileEntity;
        boolean b = true;
        try {
            Tessellator.instance.draw();
        } catch (IllegalStateException e) {
            b = false;
        }
        GL11.glPushMatrix();

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_CULL_FACE);
        tessellator.setColorOpaque_I(block.colorMultiplier(world, x, y, z));
        tessellator.setBrightness(240);
        Minecraft.getMinecraft().renderEngine.bindTexture(tex);
        switch (meta) {
            case 2:
                renderZNeg(renderer, block, generateRenderInformations(tileEntityHardMeDrive));
                break;
            case 3:
                renderZPos(renderer, block, generateRenderInformations(tileEntityHardMeDrive));
                break;
            case 4:
                renderXNeg(renderer, block, generateRenderInformations(tileEntityHardMeDrive));
                break;
            case 5:
                renderXPos(renderer, block, generateRenderInformations(tileEntityHardMeDrive));
                break;
        }
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        GL11.glPopMatrix();
        if (b) tessellator.startDrawingQuads();
        tessellator.addTranslation(-x, -y, -z);
        return true;
    }

    private RenderInformation[] generateRenderInformations(TileEntityHardMeDrive tileEntity) {
        return new RenderInformation[] {
                new RenderInformation(4, tileEntity.getColorByStatus(2)),
                new RenderInformation(7, tileEntity.getColorByStatus(1)),
                new RenderInformation(10, tileEntity.getColorByStatus(0))
        };
    }

    private void renderXPos(RenderBlocks renderer, Block block, RenderInformation[] renderInformations) {
        Tessellator tessellator = Tessellator.instance;
        renderer.renderMinZ = .3125D;
        renderer.renderMaxZ = .6875D;
        for (RenderInformation renderInformation : renderInformations) {
            renderer.renderMinY = 1.0D / 16.0D * renderInformation.getPos();
            renderer.renderMaxY = 1.0D / 16.0D * (renderInformation.getPos() + 2);
            tessellator.startDrawingQuads();
            tessellator.setNormal(1.0F, 0.0F, 0.0F);
            renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, renderInformation.getIcon());
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(1.0F, 0.0F, 0.0F);
            tessellator.setColorOpaque_I(renderInformation.getColor());
            renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, renderInformation.getIcon2());
            tessellator.draw();
        }
        renderer.renderMinX = 0.0D;
        renderer.renderMinY = 0.0D;
        renderer.renderMinZ = 0.0D;
        renderer.renderMaxX = 1.0D;
        renderer.renderMaxY = 1.0D;
        renderer.renderMaxZ = 1.0D;
    }

    private void renderXNeg(RenderBlocks renderer, Block block, RenderInformation[] renderInformations) {
        Tessellator tessellator = Tessellator.instance;
        renderer.renderMinZ = .3125D;
        renderer.renderMaxZ = .6875D;
        for (RenderInformation renderInformation : renderInformations) {
            renderer.renderMinY = 1.0D / 16.0D * renderInformation.getPos();
            renderer.renderMaxY = 1.0D / 16.0D * (renderInformation.getPos() + 2);
            tessellator.startDrawingQuads();
            tessellator.setNormal(-1.0F, 0.0F, 0.0F);
            renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, renderInformation.getIcon());
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(-1.0F, 0.0F, 0.0F);
            tessellator.setColorOpaque_I(renderInformation.getColor());
            renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, renderInformation.getIcon2());
            tessellator.draw();
        }
        renderer.renderMinX = 0.0D;
        renderer.renderMinY = 0.0D;
        renderer.renderMinZ = 0.0D;
        renderer.renderMaxX = 1.0D;
        renderer.renderMaxY = 1.0D;
        renderer.renderMaxZ = 1.0D;
    }

    private void renderZPos(RenderBlocks renderer, Block block, RenderInformation[] renderInformations) {
        Tessellator tessellator = Tessellator.instance;
        renderer.renderMinX = .3125D;
        renderer.renderMaxX = .6875D;
        for (RenderInformation renderInformation : renderInformations) {
            renderer.renderMinY = 1.0D / 16.0D * renderInformation.getPos();
            renderer.renderMaxY = 1.0D / 16.0D * (renderInformation.getPos() + 2.0D);
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, 1.0F);
            renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, renderInformation.getIcon());
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, 1.0F);
            tessellator.setColorOpaque_I(renderInformation.getColor());
            renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, renderInformation.getIcon2());
            tessellator.draw();
        }
        renderer.renderMinX = 0.0D;
        renderer.renderMinY = 0.0D;
        renderer.renderMinZ = 0.0D;
        renderer.renderMaxX = 1.0D;
        renderer.renderMaxY = 1.0D;
        renderer.renderMaxZ = 1.0D;
    }

    private void renderZNeg(RenderBlocks renderer, Block block, RenderInformation[] renderInformations) {
        Tessellator tessellator = Tessellator.instance;
        renderer.renderMinX = .3125D;
        renderer.renderMaxX = .6875D;
        for (RenderInformation renderInformation : renderInformations) {
            renderer.renderMinY = 1.0D / 16.0D * renderInformation.getPos();
            renderer.renderMaxY = 1.0D / 16.0D * (renderInformation.getPos() + 2.0D);
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, -1.0F);
            renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, renderInformation.getIcon());
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, -1.0F);
            tessellator.setColorOpaque_I(renderInformation.getColor());
            renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, renderInformation.getIcon2());
            tessellator.draw();
        }
        renderer.renderMinX = 0.0D;
        renderer.renderMinY = 0.0D;
        renderer.renderMinZ = 0.0D;
        renderer.renderMaxX = 1.0D;
        renderer.renderMaxY = 1.0D;
        renderer.renderMaxZ = 1.0D;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return renderID;
    }

    class RenderInformation {
        double pos;
        int color;

        public RenderInformation(double pos, int color) {
            this.pos = pos;
            this.color = color;
        }

        public Icon getIcon() {
            return i3;
        }

        public Icon getIcon2() {
            return i3;
        }

        public double getPos() {
            return pos;
        }

        public int getColor() {
            return color;
        }
    }

    protected static class Icon implements IIcon {

        float minU;
        float maxU;
        float minV;
        float maxV;

        public Icon(float minU, float maxU, float minV, float maxV) {
            this.minU = minU;
            this.maxU = maxU;
            this.minV = minV;
            this.maxV = maxV;
        }

        @Override
        public int getIconWidth() {
            return 0;
        }

        @Override
        public int getIconHeight() {
            return 0;
        }

        @Override
        public float getMinU() {
            return minU;
        }

        @Override
        public float getMaxU() {
            return maxU;
        }

        @Override
        public float getInterpolatedU(double p_94214_1_) {
            float f = this.getMaxU() - this.getMinU();
            return this.getMinU() + f;
        }

        @Override
        public float getMinV() {
            return minV;
        }

        @Override
        public float getMaxV() {
            return maxV;
        }

        @Override
        public float getInterpolatedV(double p_94207_1_) {
            float f = this.getMaxV() - this.getMinV();
            return this.getMinV() + f;
        }

        @Override
        public String getIconName() {
            return "";
        }
    }
}