package com.dooberjaz.mooresmod.util.recipe;

import net.minecraftforge.registries.IForgeRegistryEntry;

public abstract class RecipeBase extends IForgeRegistryEntry.Impl<IMooresRecipe> implements IMooresRecipe {

    @Override
    public boolean canFit(int width, int height) {
        return width >= 9 && height >= 9;
    }
}
