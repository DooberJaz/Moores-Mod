package com.dooberjaz.mooresmod.util.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.JsonContext;

import javax.annotation.Nonnull;
import java.util.Iterator;

/**
 * Created by covers1624 on 9/10/2017.
 */
public class MooresShapelessRecipe extends RecipeBase {

    //So this is some code I borrowed from another mod (Avaritia) that I couldnt get working in tandem with this for development.
    //It is used mostly for parsing of the moores machine json files and recipe understanding. While it is a fairly small
    //Part of this mod, it did help witha  key bit of functionality for the moores machine that would have taken
    //me a very long time to do alone. So thank you to the people who contributed to it all!

    protected ItemStack output;
    protected NonNullList<Ingredient> input;

    public MooresShapelessRecipe(NonNullList<Ingredient> input, ItemStack result) {
        this.input = input;
        output = result.copy();
    }

    @Override
    public ItemStack getRecipeOutput() {
        return output.copy();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return input;
    }

    @Override
    public boolean canFit(int p_194133_1_, int p_194133_2_) {
        return p_194133_1_ * p_194133_2_ >= input.size();
    }

    @Override
    public boolean matches(@Nonnull InventoryCrafting var1, @Nonnull World world) {
        NonNullList<Ingredient> required = NonNullList.create();
        required.addAll(input);

        for (int x = 0; x < var1.getSizeInventory(); x++) {
            ItemStack slot = var1.getStackInSlot(x);

            if (!slot.isEmpty()) {
                boolean inRecipe = false;
                Iterator<Ingredient> req = required.iterator();

                while (req.hasNext()) {
                    Ingredient ing = req.next();
                    if (ing.apply(slot)) {
                        for (ItemStack stack : ing.getMatchingStacks()) {
                            if (ItemStack.areItemStackTagsEqual(stack, slot)) {
                                inRecipe = true;
                                req.remove();
                                break;
                            }
                        }
                        if (inRecipe) {
                            break;
                        }
                    }
                }

                if (!inRecipe) {
                    return false;
                }
            }
        }

        return required.isEmpty();
    }

    public static MooresShapelessRecipe fromJson(JsonContext context, JsonObject json) {
        NonNullList<Ingredient> ings = NonNullList.create();
        for (JsonElement ele : JsonUtils.getJsonArray(json, "ingredients")) {
            Ingredient i = CraftingHelper.getIngredient(ele, context);
            if (i != Ingredient.EMPTY) {
                ings.add(i);
            }
        }

        if (ings.isEmpty()) {
            throw new JsonParseException("No ingredients for shapeless recipe");
        }

        ItemStack itemstack = CraftingHelper.getItemStack(JsonUtils.getJsonObject(json, "result"), context);
        return new MooresShapelessRecipe(ings, itemstack);
    }
}
