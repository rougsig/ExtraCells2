package extracells.integration.nei;

import codechicken.nei.NEIClientUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.api.DefaultOverlayRenderer;
import codechicken.nei.api.IOverlayHandler;
import codechicken.nei.api.IRecipeOverlayRenderer;
import codechicken.nei.api.IStackPositioner;
import codechicken.nei.recipe.RecipeInfo;
import codechicken.nei.recipe.TemplateRecipeHandler;
import extracells.registries.ItemEnum;
import extracells.util.UniversalTerminalScala;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class UniversalTerminalRecipe extends TemplateRecipeHandler {
    private final String OUTPUT_ID = "crafting";

    @Override
    public void loadTransferRects() {
        Rectangle rect = new Rectangle(84, 23, 24, 18);
        this.transferRects.add(new TemplateRecipeHandler.RecipeTransferRect(rect, OUTPUT_ID));
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(OUTPUT_ID)) {
            this.loadRecipes(true);
            this.loadRecipes(false);
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        if (result != null && result.getItem() == ItemEnum.UNIVERSALTERMINAL.getItem()) {
            this.loadRecipes(true);
            this.loadRecipes(false);
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        if (ingredient == null || ingredient.getItem() == null) return;

        if (UniversalTerminalScala.isTerminal(ingredient)) {
            this.loadRecipes(true);
            this.loadRecipes(false);
        } else if (UniversalTerminalScala.isWirelessTerminal(ingredient)) {
            this.loadRecipes(false);
        } else if (ingredient.getItem() == ItemEnum.UNIVERSALTERMINAL.getItem()) {
            this.loadRecipes(true);
        }
    }

    @Override
    public String getGuiTexture() {
        return "textures/gui/container/crafting_table.png";
    }

    @Override
    public String getOverlayIdentifier() {
        return "crafting";
    }

    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return GuiCrafting.class;
    }

    @Override
    public boolean hasOverlay(GuiContainer gui, Container container, int recipe) {
        return super.hasOverlay(gui, container, recipe)
                || (this.isRecipe2x2(recipe)
                && RecipeInfo.hasDefaultOverlay(gui, "crafting2x2"));
    }

    @Override
    public IRecipeOverlayRenderer getOverlayRenderer(GuiContainer gui, int recipe) {
        IRecipeOverlayRenderer renderer = super.getOverlayRenderer(gui, recipe);
        if (renderer != null) return renderer;
        IStackPositioner positioner = RecipeInfo.getStackPositioner(gui, "crafting2x2");
        if (positioner == null) return null;
        return new DefaultOverlayRenderer(this.getIngredientStacks(recipe), positioner);
    }

    @Override
    public IOverlayHandler getOverlayHandler(GuiContainer gui, int recipe) {
        IOverlayHandler handler = super.getOverlayHandler(gui, recipe);
        if (handler != null) return handler;
        return RecipeInfo.getOverlayHandler(gui, "crafting2x2");
    }

    private boolean isRecipe2x2(int recipe) {
        for (PositionedStack stack : this.getIngredientStacks(recipe)) {
            if (stack.relx > 43 || stack.rely > 24) return false;
        }
        return true;
    }

    @Override
    public String getRecipeName() {
        return NEIClientUtils.translate("recipe.shapeless");
    }

    private void loadRecipes(boolean isUniversal) {
        CachedShapelessRecipe cachedRecipe = new CachedShapelessRecipe(isUniversal);
        cachedRecipe.computeVisuals();
        arecipes.add(cachedRecipe);
    }

    private class CachedShapelessRecipe extends CachedRecipe {
        private boolean isUniversal;
        private List<PositionedStack> ingredients = new ArrayList<>();
        private PositionedStack result = new PositionedStack(ItemEnum.UNIVERSALTERMINAL.getDamagedStack(0), 119, 24);

        public CachedShapelessRecipe(boolean isUniversal) {
            this.isUniversal = isUniversal;
            setIngredients();
        }

        @Override
        public PositionedStack getResult() {
            return this.result;
        }

        private void setIngredients() {
            PositionedStack stack = isUniversal
                    ? new PositionedStack(ItemEnum.UNIVERSALTERMINAL.getDamagedStack(0), 25, 6, false)
                    : new PositionedStack(UniversalTerminalScala.wirelessTerminals(), 25, 6, false);
            stack.setMaxSize(1);
            this.ingredients.add(stack);

            PositionedStack stack2 = new PositionedStack(UniversalTerminalScala.terminals(), 43, 6, false);
            stack2.setMaxSize(1);
            this.ingredients.add(stack2);
        }

        public void computeVisuals() {
            for (PositionedStack p : this.ingredients) {
                p.generatePermutations();
            }
            this.result.generatePermutations();
        }
    }
}
