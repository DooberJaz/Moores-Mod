package com.dooberjaz.mooresmod.util.recipe;

import com.google.gson.JsonObject;
import net.minecraftforge.common.crafting.JsonContext;
//So this is some code I borrowed from another mod (Avaritia) that I couldnt get working in tandem with this for development.
//It is used mostly for parsing of the moores machine json files and recipe understanding. While it is a fairly small
//Part of this mod, it did help witha  key bit of functionality for the moores machine that would have taken
//me a very long time to do alone. So thank you to the people who contributed to it all!
/**
 * Created by covers1624 on 10/10/2017.
 */
@FunctionalInterface
public interface IRecipeFactory<R> {

    R load(JsonContext ctx, JsonObject json);

}
