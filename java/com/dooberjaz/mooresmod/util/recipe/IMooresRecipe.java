package com.dooberjaz.mooresmod.util.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.registries.IForgeRegistryEntry;

public interface IMooresRecipe extends IForgeRegistryEntry<IMooresRecipe> {
    //So this is some code I borrowed from another mod (Avaritia) that I couldnt get working in tandem with this for development.
    //It is used mostly for parsing of the moores machine json files and recipe understanding. While it is a fairly small
    //Part of this mod, it did help witha  key bit of functionality for the moores machine that would have taken
    //me a very long time to do alone. So thank you to the people who contributed to it all!

    boolean matches(InventoryCrafting inventory, World world);

    default ItemStack getCraftingResult(InventoryCrafting inventory) {
        return getRecipeOutput();
    }

    boolean canFit(int width, int height);

    ItemStack getRecipeOutput();

    default NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        return ForgeHooks.defaultRecipeGetRemainingItems(inv);
    }

    default NonNullList<Ingredient> getIngredients() {
        return NonNullList.create();
    }

    /**
     * If this recipe is shaped.
     * Used for JEI ingredient layout.
     * Used for CraftTweaker recipe removal.
     *
     * @return true or false, default false.
     */
    default boolean isShapedRecipe() {
        return false;
    }

    /**
     * Gets the width for this recipe.
     * Only supported on shaped recipes.
     * Throws an UnsupportedOperationException if isShapedRecipe returns false.
     *
     * @return the width.
     */
    default int getWidth() {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets the height for this recipe.
     * Only supported on shaped recipes.
     * Throws an UnsupportedOperationException if isShapedRecipe returns false.
     *
     * @return the height.
     */
    default int getHeight() {
        throw new UnsupportedOperationException();
    }
}
