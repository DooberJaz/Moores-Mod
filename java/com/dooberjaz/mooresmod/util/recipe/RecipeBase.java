package com.dooberjaz.mooresmod.util.recipe;

import net.minecraftforge.registries.IForgeRegistryEntry;
//So this is some code I borrowed from another mod (Avaritia) that I couldnt get working in tandem with this for development.
//It is used mostly for parsing of the moores machine json files and recipe understanding. While it is a fairly small
//Part of this mod, it did help witha  key bit of functionality for the moores machine that would have taken
//me a very long time to do alone. So thank you to the people who contributed to it all!
public abstract class RecipeBase extends IForgeRegistryEntry.Impl<IMooresRecipe> implements IMooresRecipe {

    @Override
    public boolean canFit(int width, int height) {
        return width >= 9 && height >= 9;
    }
}
